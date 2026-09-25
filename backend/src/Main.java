import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controller.AuthController;
import controller.BookController;
import controller.ExchangeController;
import service.AuthService;
import service.BookService;
import service.ExchangeService;
import service.FileManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.concurrent.Executors;

public class Main {
	public static void main(String[] args) throws IOException {
		int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
		FileManager files = new FileManager();
		AuthService auth = new AuthService(files);
		BookService books = new BookService(files, auth);
		ExchangeService exchanges = new ExchangeService(files, books);
		AuthController authController = new AuthController(auth);
		BookController bookController = new BookController(books);
		ExchangeController exchangeController = new ExchangeController(exchanges);

		HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
		server.createContext("/api/auth/register", request -> route(request, "POST", authController::register));
		server.createContext("/api/auth/login", request -> route(request, "POST", authController::login));
		server.createContext("/api/books/recommended", request -> route(request, "GET", bookController::recommended));
		server.createContext("/api/books", request -> {
			if (request.getRequestMethod().equalsIgnoreCase("POST")) {
				route(request, "POST", bookController::create);
			} else {
				route(request, "GET", bookController::list);
			}
		});
		server.createContext("/api/exchanges", request -> {
			if (request.getRequestMethod().equalsIgnoreCase("POST")) {
				route(request, "POST", exchangeController::create);
			} else {
				route(request, "GET", exchangeController::list);
			}
		});
		server.setExecutor(Executors.newFixedThreadPool(8));
		server.start();

		System.out.println("BOOKSWAP API running at http://localhost:" + port);
		System.out.println("Data files: " + Path.of("data").toAbsolutePath());
	}

	private static void route(HttpExchange exchange, String method, Handler handler) throws IOException {
		if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
			exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
			exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
			exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
			exchange.sendResponseHeaders(204, -1);
			exchange.close();
			return;
		}
		if (!exchange.getRequestMethod().equalsIgnoreCase(method)) {
			AuthController.send(exchange, 405, AuthController.error("Method not allowed"));
			return;
		}
		try {
			handler.handle(exchange);
		} catch (Exception error) {
			AuthController.send(exchange, 500, AuthController.error("Server error: " + error.getMessage()));
		}
	}

	@FunctionalInterface
	private interface Handler {
		void handle(HttpExchange exchange) throws IOException;
	}
}

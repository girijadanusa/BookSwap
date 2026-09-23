package controller;

import com.sun.net.httpserver.HttpExchange;
import model.User;
import service.AuthService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AuthController {
	private final AuthService auth;

	public AuthController(AuthService auth) {
		this.auth = auth;
	}

	public void register(HttpExchange exchange) throws IOException {
		Map<String, String> data = parse(read(exchange));
		try {
			User user = auth.register(data.getOrDefault("name", ""), data.getOrDefault("email", ""),
					data.getOrDefault("password", ""), data.getOrDefault("phone", ""),
					data.getOrDefault("college", ""), data.getOrDefault("department", ""),
					data.getOrDefault("year", ""), data.getOrDefault("city", ""),
					data.getOrDefault("state", ""), data.getOrDefault("languages", ""));
			send(exchange, 201, userJson(user));
		} catch (IllegalArgumentException exception) {
			send(exchange, 400, error(exception.getMessage()));
		}
	}

	public void login(HttpExchange exchange) throws IOException {
		Map<String, String> data = parse(read(exchange));
		User user = auth.login(data.getOrDefault("email", ""), data.getOrDefault("password", ""));
		if (user == null) {
			send(exchange, 401, error("Invalid email or password"));
			return;
		}
		send(exchange, 200, userJson(user));
	}

	private String userJson(User user) {
		return "{\"id\":" + quote(user.getId()) + ",\"name\":" + quote(user.getName())
				+ ",\"email\":" + quote(user.getEmail()) + ",\"college\":" + quote(user.getCollege())
				+ ",\"city\":" + quote(user.getCity()) + "}";
	}

	static String read(HttpExchange exchange) throws IOException {
		return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
	}

	static Map<String, String> parse(String json) {
		Map<String, String> result = new HashMap<>();
		if (json == null) {
			return result;
		}
		String value = json.trim();
		if (value.startsWith("{") && value.endsWith("}")) {
			value = value.substring(1, value.length() - 1);
		}
		for (String entry : value.split(",(?=\\\")")) {
			String[] pair = entry.split(":", 2);
			if (pair.length == 2) {
				result.put(unquote(pair[0].trim()), unquote(pair[1].trim()));
			}
		}
		return result;
	}

	static String unquote(String value) {
		String result = value.trim();
		if (result.startsWith("\"") && result.endsWith("\"")) {
			result = result.substring(1, result.length() - 1);
		}
		return result.replace("\\\"", "\"").replace("\\\\", "\\");
	}

	static String quote(String value) {
		return "\"" + (value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"")) + "\"";
	}

	static String error(String message) {
		return "{\"error\":" + quote(message) + "}";
	}

	static void send(HttpExchange exchange, int status, String body) throws IOException {
		byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
		exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
		exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
		exchange.sendResponseHeaders(status, bytes.length);
		try (var output = exchange.getResponseBody()) {
			output.write(bytes);
		}
	}
}

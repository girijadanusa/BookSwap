package controller;

import com.sun.net.httpserver.HttpExchange;
import model.ExchangeRequest;
import service.ExchangeService;

import java.io.IOException;
import java.util.Map;

public class ExchangeController {
	private final ExchangeService exchanges;

	public ExchangeController(ExchangeService exchanges) {
		this.exchanges = exchanges;
	}

	public void create(HttpExchange exchange) throws IOException {
		Map<String, String> data = AuthController.parse(AuthController.read(exchange));
		try {
			ExchangeRequest request = exchanges.create(data.getOrDefault("bookId", ""),
					data.getOrDefault("requesterId", "guest"), data.getOrDefault("requesterName", "Guest"),
					data.getOrDefault("type", "Exchange"), data.getOrDefault("message", ""));
			String response = "{\"id\":" + AuthController.quote(request.getId()) + ",\"status\":"
					+ AuthController.quote(request.getStatus()) + "}";
			AuthController.send(exchange, 201, response);
		} catch (IllegalArgumentException exception) {
			AuthController.send(exchange, 400, AuthController.error(exception.getMessage()));
		}
	}

	public void list(HttpExchange exchange) throws IOException {
		StringBuilder json = new StringBuilder("[");
		int index = 0;
		for (ExchangeRequest request : exchanges.all()) {
			if (index++ > 0) {
				json.append(',');
			}
			json.append("{\"id\":").append(AuthController.quote(request.getId()))
					.append(",\"bookId\":").append(AuthController.quote(request.getBookId()))
					.append(",\"requesterName\":").append(AuthController.quote(request.getRequesterName()))
					.append(",\"type\":").append(AuthController.quote(request.getType()))
					.append(",\"status\":").append(AuthController.quote(request.getStatus())).append('}');
		}
		AuthController.send(exchange, 200, json.append(']').toString());
	}
}

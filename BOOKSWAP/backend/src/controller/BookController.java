package controller;

import com.sun.net.httpserver.HttpExchange;
import model.Book;
import service.BookService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BookController {
	private final BookService books;

	public BookController(BookService books) {
		this.books = books;
	}

	public void list(HttpExchange exchange) throws IOException {
		sendBooks(exchange, books.all());
	}

	public void recommended(HttpExchange exchange) throws IOException {
		sendBooks(exchange, books.all().stream().limit(6).toList());
	}

	public void create(HttpExchange exchange) throws IOException {
		Map<String, String> data = AuthController.parse(AuthController.read(exchange));
		Book book = books.add(data.getOrDefault("title", ""), data.getOrDefault("author", ""),
				data.getOrDefault("ownerId", "guest"), data.getOrDefault("ownerName", "Guest"),
				data.getOrDefault("category", "Other"), data.getOrDefault("type", "Exchange"),
				data.getOrDefault("condition", "Good"), data.getOrDefault("country", "India"),
				data.getOrDefault("state", ""), data.getOrDefault("city", ""),
				data.getOrDefault("college", ""));
		AuthController.send(exchange, 201, toJson(book));
	}

	private void sendBooks(HttpExchange exchange, List<Book> books) throws IOException {
		StringBuilder json = new StringBuilder("[");
		for (int index = 0; index < books.size(); index++) {
			if (index > 0) {
				json.append(',');
			}
			json.append(toJson(books.get(index)));
		}
		json.append(']');
		AuthController.send(exchange, 200, json.toString());
	}

	private String toJson(Book book) {
		return "{\"id\":" + AuthController.quote(book.getId()) + ",\"title\":"
				+ AuthController.quote(book.getTitle()) + ",\"author\":" + AuthController.quote(book.getAuthor())
				+ ",\"owner\":" + AuthController.quote(book.getOwnerName()) + ",\"category\":"
				+ AuthController.quote(book.getCategory()) + ",\"type\":" + AuthController.quote(book.getType())
				+ ",\"condition\":" + AuthController.quote(book.getCondition()) + ",\"city\":"
				+ AuthController.quote(book.getCity()) + ",\"status\":" + AuthController.quote(book.getStatus())
				+ ",\"initial\":" + AuthController.quote(book.getTitle().substring(0, 1).toUpperCase()) + "}";
	}
}

package service;

import model.ExchangeRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExchangeService {
	private final FileManager files;
	private final BookService books;

	public ExchangeService(FileManager files, BookService books) {
		this.files = files;
		this.books = books;
	}

	public ExchangeRequest create(String bookId, String requesterId, String requesterName, String type,
								  String message) {
		if (books.find(bookId) == null) {
			throw new IllegalArgumentException("Book not found");
		}
		ExchangeRequest request = new ExchangeRequest(UUID.randomUUID().toString(), bookId,
				FileManager.clean(requesterId), FileManager.clean(requesterName), FileManager.clean(type),
				FileManager.clean(message), "Pending");
		files.append("exchanges.txt", serialize(request));
		books.markUnavailable(bookId);
		return request;
	}

	public List<ExchangeRequest> all() {
		List<ExchangeRequest> result = new ArrayList<>();
		for (String row : files.read("exchanges.txt")) {
			if (!row.isBlank()) {
				result.add(from(FileManager.columns(row)));
			}
		}
		return result;
	}

	private ExchangeRequest from(String[] columns) {
		String[] values = new String[7];
		for (int index = 0; index < values.length; index++) {
			values[index] = index < columns.length ? columns[index] : "";
		}
		return new ExchangeRequest(values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
	}

	private String serialize(ExchangeRequest request) {
		return String.join("\t", request.getId(), request.getBookId(), request.getRequesterId(),
				request.getRequesterName(), request.getType(), request.getMessage(), request.getStatus());
	}
}

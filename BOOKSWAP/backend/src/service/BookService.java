package service;

import model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookService {
	private final FileManager files;
	private final AuthService auth;

	public BookService(FileManager files, AuthService auth) {
		this.files = files;
		this.auth = auth;
		seedBooks();
	}

	private void seedBooks() {
		if (!files.read("books.txt").isEmpty()) {
			return;
		}
		addRaw(new Book("1", "Atomic Habits", "James Clear", "seed-1", "Maya S.", "Skills", "Exchange",
				"Good", "India", "Karnataka", "Bangalore", "City College", "Available"));
		addRaw(new Book("2", "The Psychology of Money", "Morgan Housel", "seed-2", "Riya M.", "Skills",
				"Borrow", "New", "India", "Karnataka", "Bangalore", "City College", "Available"));
		addRaw(new Book("3", "The Design of Everyday Things", "Don Norman", "seed-3", "Sam K.", "Academic",
				"Give Away", "Used", "India", "Karnataka", "Bangalore", "City College", "Available"));
	}

	private void addRaw(Book book) {
		files.append("books.txt", serialize(book));
	}

	public List<Book> all() {
		List<Book> result = new ArrayList<>();
		for (String row : files.read("books.txt")) {
			if (!row.isBlank()) {
				result.add(from(FileManager.columns(row)));
			}
		}
		return result;
	}

	public Book find(String id) {
		return all().stream().filter(book -> book.getId().equals(id)).findFirst().orElse(null);
	}

	public Book add(String title, String author, String ownerId, String ownerName, String category, String type,
					String condition, String country, String state, String city, String college) {
		Book book = new Book(UUID.randomUUID().toString(), clean(title), clean(author), clean(ownerId),
				clean(ownerName), clean(category), clean(type), clean(condition), clean(country), clean(state),
				clean(city), clean(college), "Available");
		addRaw(book);
		return book;
	}

	public void markUnavailable(String id) {
		List<String> updated = new ArrayList<>();
		for (String row : files.read("books.txt")) {
			String[] columns = FileManager.columns(row);
			if (columns.length > 12 && columns[0].equals(id)) {
				columns[12] = "Requested";
			}
			updated.add(String.join("\t", columns));
		}
		files.replace("books.txt", updated);
	}

	private Book from(String[] columns) {
		String[] values = new String[14];
		for (int index = 0; index < values.length; index++) {
			values[index] = index < columns.length ? columns[index] : "";
		}
		return new Book(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7],
				values[8], values[9], values[10], values[11], values[12]);
	}

	private String serialize(Book book) {
		return String.join("\t", book.getId(), clean(book.getTitle()), clean(book.getAuthor()), book.getOwnerId(),
				clean(book.getOwnerName()), clean(book.getCategory()), clean(book.getType()), clean(book.getCondition()),
				clean(book.getCountry()), clean(book.getState()), clean(book.getCity()), clean(book.getCollege()),
				clean(book.getStatus()));
	}

	private String clean(String value) {
		return FileManager.clean(value);
	}
}

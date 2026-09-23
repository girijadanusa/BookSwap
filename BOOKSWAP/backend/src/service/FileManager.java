package service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
	private final Path dataDirectory;

	public FileManager() {
		this(Path.of("data"));
	}

	public FileManager(Path dataDirectory) {
		this.dataDirectory = dataDirectory;
		ensureFiles();
	}

	private void ensureFiles() {
		try {
			Files.createDirectories(dataDirectory);
			for (String name : List.of("users.txt", "books.txt", "exchanges.txt")) {
				Path file = dataDirectory.resolve(name);
				if (!Files.exists(file)) {
					Files.createFile(file);
				}
			}
		} catch (IOException exception) {
			throw new IllegalStateException("Could not prepare data files", exception);
		}
	}

	public synchronized List<String> read(String fileName) {
		try {
			return new ArrayList<>(Files.readAllLines(dataDirectory.resolve(fileName), StandardCharsets.UTF_8));
		} catch (IOException exception) {
			throw new IllegalStateException("Could not read " + fileName, exception);
		}
	}

	public synchronized void append(String fileName, String row) {
		try {
			Files.writeString(dataDirectory.resolve(fileName), row + System.lineSeparator(), StandardCharsets.UTF_8,
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException exception) {
			throw new IllegalStateException("Could not write " + fileName, exception);
		}
	}

	public synchronized void replace(String fileName, List<String> rows) {
		try {
			Files.write(dataDirectory.resolve(fileName), rows, StandardCharsets.UTF_8,
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException exception) {
			throw new IllegalStateException("Could not update " + fileName, exception);
		}
	}

	public static String clean(String value) {
		return value == null ? "" : value.replace("\t", " ").replace("\r", " ").replace("\n", " ").trim();
	}

	public static String[] columns(String row) {
		return row.split("\\t", -1);
	}
}

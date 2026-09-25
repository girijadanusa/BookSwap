package service;

import model.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

public class AuthService {
	private final FileManager files;

	public AuthService(FileManager files) {
		this.files = files;
	}

	public User register(String name, String email, String password, String phone, String college,
						 String department, String year, String city, String state, String languages) {
		if (name.isBlank() || email.isBlank() || password.length() < 6) {
			throw new IllegalArgumentException("Name, email and a 6 character password are required");
		}
		if (findByEmail(email) != null) {
			throw new IllegalArgumentException("An account with this email already exists");
		}
		User user = new User(UUID.randomUUID().toString(), clean(name), clean(email).toLowerCase(), hash(password),
				clean(phone), clean(college), clean(department), clean(year), clean(city), clean(state), clean(languages));
		files.append("users.txt", String.join("\t", user.getId(), user.getName(), user.getEmail(),
				user.getPasswordHash(), user.getPhone(), user.getCollege(), user.getDepartment(), user.getYear(),
				user.getCity(), user.getState(), user.getLanguages()));
		return user;
	}

	public User login(String email, String password) {
		User user = findByEmail(email);
		if (user == null || !user.getPasswordHash().equals(hash(password))) {
			return null;
		}
		return user;
	}

	public User findByEmail(String email) {
		for (String row : files.read("users.txt")) {
			String[] columns = FileManager.columns(row);
			if (columns.length >= 4 && columns[2].equalsIgnoreCase(email.trim())) {
				return from(columns);
			}
		}
		return null;
	}

	public User findById(String id) {
		for (String row : files.read("users.txt")) {
			String[] columns = FileManager.columns(row);
			if (columns.length >= 4 && columns[0].equals(id)) {
				return from(columns);
			}
		}
		return null;
	}

	private User from(String[] columns) {
		String[] values = new String[11];
		for (int index = 0; index < values.length; index++) {
			values[index] = index < columns.length ? columns[index] : "";
		}
		return new User(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7],
				values[8], values[9], values[10]);
	}

	private String clean(String value) {
		return FileManager.clean(value);
	}

	public static String hash(String value) {
		try {
			byte[] bytes = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
			StringBuilder result = new StringBuilder();
			for (byte valueByte : bytes) {
				result.append(String.format("%02x", valueByte));
			}
			return result.toString();
		} catch (NoSuchAlgorithmException exception) {
			throw new IllegalStateException(exception);
		}
	}

	public List<User> all() {
		return files.read("users.txt").stream().filter(row -> !row.isBlank())
				.map(row -> from(FileManager.columns(row))).toList();
	}
}

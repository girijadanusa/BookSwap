package model;

public class Admin {
	private final String username;
	private final String passwordHash;

	public Admin(String username, String passwordHash) {
		this.username = username;
		this.passwordHash = passwordHash;
	}

	public String getUsername() { return username; }
	public String getPasswordHash() { return passwordHash; }
}

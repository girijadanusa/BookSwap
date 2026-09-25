package model;

public class User {
	private final String id;
	private final String name;
	private final String email;
	private final String passwordHash;
	private final String phone;
	private final String college;
	private final String department;
	private final String year;
	private final String city;
	private final String state;
	private final String languages;

	public User(String id, String name, String email, String passwordHash, String phone, String college,
				String department, String year, String city, String state, String languages) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.passwordHash = passwordHash;
		this.phone = phone;
		this.college = college;
		this.department = department;
		this.year = year;
		this.city = city;
		this.state = state;
		this.languages = languages;
	}

	public String getId() { return id; }
	public String getName() { return name; }
	public String getEmail() { return email; }
	public String getPasswordHash() { return passwordHash; }
	public String getPhone() { return phone; }
	public String getCollege() { return college; }
	public String getDepartment() { return department; }
	public String getYear() { return year; }
	public String getCity() { return city; }
	public String getState() { return state; }
	public String getLanguages() { return languages; }
}

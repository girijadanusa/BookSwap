package model;

public class Book {
	private final String id;
	private final String title;
	private final String author;
	private final String ownerId;
	private final String ownerName;
	private final String category;
	private final String type;
	private final String condition;
	private final String country;
	private final String state;
	private final String city;
	private final String college;
	private String status;

	public Book(String id, String title, String author, String ownerId, String ownerName, String category,
				String type, String condition, String country, String state, String city, String college,
				String status) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.ownerId = ownerId;
		this.ownerName = ownerName;
		this.category = category;
		this.type = type;
		this.condition = condition;
		this.country = country;
		this.state = state;
		this.city = city;
		this.college = college;
		this.status = status;
	}

	public String getId() { return id; }
	public String getTitle() { return title; }
	public String getAuthor() { return author; }
	public String getOwnerId() { return ownerId; }
	public String getOwnerName() { return ownerName; }
	public String getCategory() { return category; }
	public String getType() { return type; }
	public String getCondition() { return condition; }
	public String getCountry() { return country; }
	public String getState() { return state; }
	public String getCity() { return city; }
	public String getCollege() { return college; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
}

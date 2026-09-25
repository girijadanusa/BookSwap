package model;

public class ExchangeRequest {
	private final String id;
	private final String bookId;
	private final String requesterId;
	private final String requesterName;
	private final String type;
	private final String message;
	private String status;

	public ExchangeRequest(String id, String bookId, String requesterId, String requesterName,
						   String type, String message, String status) {
		this.id = id;
		this.bookId = bookId;
		this.requesterId = requesterId;
		this.requesterName = requesterName;
		this.type = type;
		this.message = message;
		this.status = status;
	}

	public String getId() { return id; }
	public String getBookId() { return bookId; }
	public String getRequesterId() { return requesterId; }
	public String getRequesterName() { return requesterName; }
	public String getType() { return type; }
	public String getMessage() { return message; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
}

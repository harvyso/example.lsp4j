package example.lsp4j.chat.shared;


public class UserMessage {

	/**
	 * A user posted this message.
	 */
	private String user;

	/**
	 * A content of this message.
	 */
	private String content;

	public UserMessage(String user, String message) {
		super();
		this.user = user;
		this.content = message;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
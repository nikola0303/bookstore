package bookstore.dto;

public class MessageDTO {

	private String sendersNameAndSurname;
	private String sendersEmail;
	private String textMessage;
	
	public MessageDTO() {}

	public String getSendersNameAndSurname() {
		return sendersNameAndSurname;
	}

	public void setSendersNameAndSurname(String sendersNameAndSurname) {
		this.sendersNameAndSurname = sendersNameAndSurname;
	}

	public String getSendersEmail() {
		return sendersEmail;
	}

	public void setSendersEmail(String sendersEmail) {
		this.sendersEmail = sendersEmail;
	}

	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}
	
}

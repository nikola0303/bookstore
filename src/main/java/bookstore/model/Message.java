package bookstore.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false) 
	private String sendersNameAndSurname;
	
	@Column(nullable = false) 
	private String sendersEmail;
	
	@Column(nullable = false) 
	private String textMessage;
	
	public Message() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

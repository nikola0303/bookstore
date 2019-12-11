package bookstore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bookstore.dto.MessageDTO;
import bookstore.model.Message;
import bookstore.service.EmailService;
import bookstore.service.MessageService;

@RestController
@RequestMapping(value = "/message")
public class MessageController {

	@Autowired
	MessageService messageService;
	
	@Autowired
	EmailService emailService;
	
	private static final Logger logger = LoggerFactory.getLogger(MyUserController.class);
	
	@RequestMapping(value = "/send", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Message> send(@RequestBody MessageDTO request) {		
		if(!request.getSendersNameAndSurname().isEmpty() && !request.getSendersEmail().isEmpty() && !request.getTextMessage().isEmpty()) {
			Message message = new Message();
			message.setSendersNameAndSurname(request.getSendersNameAndSurname());
			message.setSendersEmail(request.getSendersEmail());
			message.setTextMessage(request.getTextMessage());	
			
			messageService.save(message);
			emailService.sendMessageMail(message);
			logger.info("\n\t\tYour message was successfully sent.\n");
			return new ResponseEntity<Message>(message, HttpStatus.OK);
		}
			
		logger.info("\n\t\tFailed to send message.\n");
		return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
	}
	
}

package bookstore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bookstore.model.Message;
import bookstore.repository.MessageRepository;
import bookstore.service.MessageService;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

	@Autowired
	MessageRepository messageRepository;
	
	@Override
	public Message findOne(Long id) {
		return messageRepository.findById(id);
	}

	@Override
	public List<Message> findAll() {
		return messageRepository.findAll();
	}

	@Override
	public Message save(Message message) {
		return messageRepository.save(message);
	}

	@Override
	public Message delete(Long id) {
		Message message = messageRepository.findById(id);
		if(message == null) {
			throw new IllegalArgumentException("Attempt to delete non-existent message.");
		}
		messageRepository.delete(message);
		return message;
	}	
	
}

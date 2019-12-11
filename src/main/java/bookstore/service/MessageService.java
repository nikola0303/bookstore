package bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bookstore.model.Message;

@Service
@Transactional
public interface MessageService {

	Message findOne(Long id);	
	List<Message> findAll();
	
	Message save(Message message);
	
	Message delete(Long id);
	
}

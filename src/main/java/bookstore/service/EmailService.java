package bookstore.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bookstore.model.Message;
import bookstore.model.MyUser;

@Service
@Transactional
public interface EmailService {
	
	void sendMailToActivateAccount(MyUser user);
	void sendMessageMail(Message message);
	
}

package bookstore.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bookstore.Encryptor;
import bookstore.model.Message;
import bookstore.model.MyUser;
import bookstore.service.EmailService;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private Environment env;
	
	@Async
	@Override
	public void sendMailToActivateAccount(MyUser user) {		
		String decryptedString;
		
		try {
			decryptedString = Encryptor.decrypt(user.getEmail());			
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo("nijemidosadno@gmail.com");
			mail.setFrom(env.getProperty("spring.mail.username"));
			mail.setSubject("[BOOKSTORE] User account activation");
			mail.setText("To activate your user account, you need to visit the following link:\n https://localhost:9081/myUser/activateUserAccount/" + decryptedString);
			javaMailSender.send(mail);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    }

	@Async
	@Override
	public void sendMessageMail(Message message) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo("nijemidosadno@gmail.com");
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("[BOOKSTORE] Message");
		mail.setText(message.getTextMessage());
		javaMailSender.send(mail);
	}
	
}

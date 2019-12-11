package bookstore.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bookstore.Encryptor;
import bookstore.annotation.PermissionAnnotation;
import bookstore.dto.MyUserDTO;
import bookstore.model.MyUser;
import bookstore.model.ShoppingCart;
import bookstore.model.UserType;
import bookstore.repository.MyUserRepository;
import bookstore.service.EmailService;
import bookstore.service.MyUserService;
import bookstore.service.RoleService;
import bookstore.service.ShoppingCartService;

@RestController
@RequestMapping(value = "/myUser")
public class MyUserController {
	
	@Autowired
	MyUserService myUserService;
	
	@Autowired
	MyUserRepository myUserRepository;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	ShoppingCartService shoppingCartService;
	
	private static final Logger logger = LoggerFactory.getLogger(MyUserController.class);
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<MyUser> getOne(@PathVariable Long id) {
		MyUser myUser = myUserService.findOne(id);
		
		if (myUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(myUser, HttpStatus.OK);
	}
			
	@RequestMapping(value="/getAll", method = RequestMethod.GET)
	public ResponseEntity<List<MyUser>> getAll() {
		List<MyUser> myUsers = myUserService.findAll();	
		
		if(myUsers.equals(null)) {
			return new ResponseEntity<>(myUsers, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(myUsers, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getAllVisitors", method = RequestMethod.GET)
	public ResponseEntity<List<MyUser>> getAllVisitors() {
		List<MyUser> myUsers = myUserService.findAll();
		List<MyUser> visitors = new ArrayList<MyUser>();
		
		if(myUsers.equals(null)) {
			return new ResponseEntity<>(myUsers, HttpStatus.NOT_FOUND);
		}
		
		for(MyUser user : myUsers) {
			if(user.getUserType().equals(UserType.VISITOR)) {
				visitors.add(user);
			}
		}
		
		return new ResponseEntity<>(visitors, HttpStatus.OK);
	}
		
	@RequestMapping(value = "/registration", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MyUser> registration(@RequestBody MyUserDTO request) throws MailException, InterruptedException, MessagingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, ParseException {		
		String encryptedString = Encryptor.encrypt(request.getEmail());
		
		if(!request.getName().isEmpty() && !request.getSurname().isEmpty() && !request.getAddress().isEmpty() && !request.getPhone().isEmpty() && !request.getEmail().isEmpty() && !request.getPassword().isEmpty()) {
			MyUser myUser = new MyUser();
			ShoppingCart shoppingCart = new ShoppingCart();
			myUser.setName(request.getName());
			myUser.setSurname(request.getSurname());
			myUser.setAddress(request.getAddress());
			myUser.setPhone(request.getPhone());
			myUser.setEmail(encryptedString);
			myUser.setPassword(request.getPassword());		
			myUser.setUserType(UserType.VISITOR);
			myUser.setRole(roleService.findById(2L)); //default: visitor
			myUser.setActivatedAccount(false);
			myUser.setShoppingCart(shoppingCart);
			
			for(MyUser u : myUserService.findAll()) {
				if(!u.getEmail().equals(myUser.getEmail())) {
					shoppingCartService.save(shoppingCart);
					myUserService.save(myUser);
					emailService.sendMailToActivateAccount(myUser);
					logger.info("\n\t\tUser " + request.getEmail() + " is successfully registered.\n");
					return new ResponseEntity<MyUser>(myUser, HttpStatus.OK);
				}
			}
		}
			
		logger.info("\n\t\tFailed to register user.\n");
		return new ResponseEntity<MyUser>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/activateUserAccount/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MyUser> activateUserAccount(@PathVariable String email) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
		String concatenated = email + ".com"; //zakucano...		
		String encryptedString = Encryptor.encrypt(concatenated);
		List<MyUser> myUsers = myUserService.findAll();
		
		for(int i = 0; i < myUsers.size(); i++) {
			if(myUsers.get(i).getEmail().equals(encryptedString)) {
				MyUser u = myUsers.get(i);
				u.setActivatedAccount(true);
				myUserService.save(u);
				logger.info("\n\t\tUser " + concatenated + " has activated his user account.\n");
				return new ResponseEntity<MyUser>(u, HttpStatus.OK);
			}
		}
		
		logger.info("\n\t\tUser account activation failed.\n");
		return new ResponseEntity<MyUser>(HttpStatus.NOT_FOUND);
	}
		
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MyUser> login(@RequestBody MyUserDTO request) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException{
		String encryptedString = Encryptor.encrypt(request.getEmail());
		MyUser myUser = myUserService.findByEmail(encryptedString);
		
		if(myUser != null) {
			if(myUser.isActivatedAccount() == false) {
				logger.info("\n\t\tUser account not activated.\n");
				return new ResponseEntity<MyUser>(myUser, HttpStatus.NOT_FOUND);
			} else {
				if (myUser.getEmail().equals(encryptedString) /*&& bCryptPasswordEncoder.matches(request.getPassword(), myUser.getPassword())*/) {			
					myUserService.setCurrentUser(myUser);
					logger.info("\n\t\tUser " + request.getEmail() + " logged on to the system.\n");
					return new ResponseEntity<MyUser>(myUser, HttpStatus.OK);
				} else {
					logger.info("\n\t\tWrong credentials.\n");
					return new ResponseEntity<MyUser>(myUser, HttpStatus.NOT_FOUND);
				}
			}
		}
			
		logger.info("\n\t\tUser not registered.\n");
		return new ResponseEntity<MyUser>(myUser, HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/getCurrentlyActive", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MyUser> getCurrentlyActive(){
		MyUser myUser = myUserService.getCurrentUser();
		
		if(myUser == null)
			return new ResponseEntity<MyUser>(myUser, HttpStatus.NOT_FOUND);
			
		return new ResponseEntity<MyUser>(myUser, HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ResponseEntity<MyUser> logout() throws InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
		MyUser myUser = myUserService.getCurrentUser();
		
		if(myUser != null) {
			String decryptedString = Encryptor.decrypt(myUser.getEmail());			
			SecurityContextHolder.clearContext();
			logger.info("\n\t\tUser " + decryptedString + " logged out of the system.\n");
		}
		
		logger.info("\n\t\tNo one is logged in.\n");
		return new ResponseEntity<MyUser>(myUser, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/encryptEmail/{email}", method = RequestMethod.GET)
	public ResponseEntity<String> encryptEmail(@PathVariable String email) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException{
		String encryptedString = Encryptor.encrypt(email);
		return new ResponseEntity<String>(encryptedString, HttpStatus.OK);
	}
		
	@RequestMapping(value = "/decryptEmail/{email}", method = RequestMethod.GET)
	public ResponseEntity<String> decryptEmail(@PathVariable String email) throws InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException{
		String decryptedString = Encryptor.decrypt(email);
		return new ResponseEntity<String>(decryptedString, HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/numberOfBooksPurchased", method = RequestMethod.GET)
	public int numberOfBooksPurchased() {
		MyUser myUser = myUserService.getCurrentUser();
		
		if (myUser == null) {
			return -1;
		}
		
		return myUser.getShoppingCart().getBooks().size();
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value="/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MyUser> update(@RequestBody MyUserDTO request) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
		MyUser myUser = myUserService.getCurrentUser();
		String decryptedString = Encryptor.decrypt(myUser.getEmail());
		
		if(myUser != null) {
			myUser.setName(request.getName());
			myUser.setSurname(request.getSurname());
			myUser.setAddress(request.getAddress());
			myUser.setPhone(request.getPhone());
						
			myUserService.save(myUser);
			logger.info("\n\t\tUser " + decryptedString + " has updated his account.\n");
			return new ResponseEntity<MyUser>(myUser, HttpStatus.OK);
		}
		
		logger.info("\n\t\tFailed to update user " + decryptedString +".");
		return new ResponseEntity<MyUser>(myUser, HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value="/changePassword", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MyUser> changePassword(@RequestBody MyUserDTO request) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
		MyUser myUser = myUserService.getCurrentUser();
		String decryptedString = Encryptor.decrypt(myUser.getEmail());
		
		if(myUser != null) {
			myUser.setPassword(request.getPassword());
						
			myUserService.save(myUser);
			logger.info("\n\t\tUser " + decryptedString + " has changed his password.\n");
			return new ResponseEntity<MyUser>(myUser, HttpStatus.OK);
		}
		
		logger.info("\n\t\tAn error occurred when " + decryptedString + " tried to change his password.");
		return new ResponseEntity<MyUser>(myUser, HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value="/deactivateAccount", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MyUser> deactivateAccount() throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
		MyUser myUser = myUserService.getCurrentUser();
		String decryptedString = Encryptor.decrypt(myUser.getEmail());
		
		if(myUser != null) {
			myUser.setActivatedAccount(false);
						
			myUserService.save(myUser);
			logger.info("\n\t\tUser " + decryptedString + " has deactivated his account.\n");
			return new ResponseEntity<MyUser>(myUser, HttpStatus.OK);
		}
		
		logger.info("\n\t\tAn error occurred when " + decryptedString + " tried to deactivate his account.");
		return new ResponseEntity<MyUser>(myUser, HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PermissionAnnotation(name = "DELETE_VISITOR")
	@RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MyUser> delete(@PathVariable Long id) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
		MyUser myUser = myUserService.getCurrentUser();
		String decryptedString = Encryptor.decrypt(myUser.getEmail());
		
		MyUser deleteUser = myUserService.findOne(id);
		String decryptedStringDeleteUser = Encryptor.decrypt(deleteUser.getEmail());
		
		if(myUser != null && myUser.getUserType().equals(UserType.ADMIN) && deleteUser != null && deleteUser.getUserType().equals(UserType.VISITOR)) {
			myUserService.delete(deleteUser.getId());
			logger.info("\n\t\tUser " + decryptedString + " has deleted visitor " + decryptedStringDeleteUser + ".\n");
			return new ResponseEntity<MyUser>(deleteUser, HttpStatus.OK);
		}
		
		logger.info("\n\t\tFailed to delete visitor " + decryptedStringDeleteUser + ".");
		return new ResponseEntity<MyUser>(deleteUser, HttpStatus.NOT_FOUND);
	}
	
}

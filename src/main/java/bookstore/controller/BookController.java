package bookstore.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bookstore.Encryptor;
import bookstore.annotation.PermissionAnnotation;
import bookstore.dto.BookDTO;
import bookstore.model.Book;
import bookstore.model.MyUser;
import bookstore.model.UserType;
import bookstore.service.BookService;
import bookstore.service.MyUserService;

@RestController
@RequestMapping(value = "/book")
public class BookController {

	@Autowired
	BookService bookService;
	
	@Autowired
	MyUserService myUserService;
	
	private static final Logger logger = LoggerFactory.getLogger(MyUserController.class);
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Book> getOne(@PathVariable Long id) {
		Book book = bookService.findOne(id);
		
		if (book == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(book, HttpStatus.OK);
	}
			
	@RequestMapping(value="/getAll", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> getAll() {
		List<Book> books = bookService.findAll();	
		
		if(books.equals(null)) {
			return new ResponseEntity<>(books, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(books, HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PermissionAnnotation(name = "ADD_BOOK")
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> add(@RequestBody BookDTO request) throws InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {		
		MyUser myUser = myUserService.getCurrentUser();	
		String decryptedString = Encryptor.decrypt(myUser.getEmail());
		
		if(myUser != null && myUser.getUserType().equals(UserType.ADMIN)) {
			if(!request.getTitle().isEmpty() && !request.getAuthor().isEmpty() && !request.getDescription().isEmpty() && !request.getPublishingYear().isEmpty() && !request.getPublisher().isEmpty() && !request.getPrice().isEmpty()) {
				Book book = new Book();
				book.setTitle(request.getTitle());
				book.setAuthor(request.getAuthor());
				book.setDescription(request.getDescription());
				book.setPublishingYear(Integer.parseInt(request.getPublishingYear()));
				book.setPublisher(request.getPublisher());
				book.setPrice(Double.parseDouble(request.getPrice()));
				book.setImage(request.getImage());
				
				bookService.save(book);
				logger.info("\n\t\tUser " + decryptedString + " added a book " + book.getTitle() + ".\n");
				return new ResponseEntity<Book>(book, HttpStatus.OK);
			}
		}
			
		logger.info("\n\t\tFailed to add book.\n");
		return new ResponseEntity<Book>(HttpStatus.BAD_REQUEST);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PermissionAnnotation(name = "UPDATE_BOOK")
	@RequestMapping(value="/update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> update(@RequestBody BookDTO request, @PathVariable Long id) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
		MyUser myUser = myUserService.getCurrentUser();
		String decryptedString = Encryptor.decrypt(myUser.getEmail());
		
		Book book = bookService.findOne(id);
		
		if(myUser != null && myUser.getUserType().equals(UserType.ADMIN) && book != null) {
			book.setTitle(request.getTitle());
			book.setAuthor(request.getAuthor());
			book.setDescription(request.getDescription());
			book.setPublishingYear(Integer.parseInt(request.getPublishingYear()));
			book.setPublisher(request.getPublisher());
			book.setPrice(Double.parseDouble(request.getPrice()));
			
			bookService.save(book);
			logger.info("\n\t\tUser " + decryptedString + " has updated book " + book.getTitle() + ".\n");
			return new ResponseEntity<Book>(book, HttpStatus.OK);
		}
		
		logger.info("\n\t\tFailed to update book " + book.getTitle() +".");
		return new ResponseEntity<Book>(book, HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PermissionAnnotation(name = "DELETE_BOOK")
	@RequestMapping(value="/delete/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> delete(@PathVariable Long id) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
		MyUser myUser = myUserService.getCurrentUser();
		String encryptedString = Encryptor.decrypt(myUser.getEmail());
		
		Book deleteBook = bookService.findOne(id);
		boolean purchased = false;
		
		if(myUser != null && myUser.getUserType().equals(UserType.ADMIN) && deleteBook != null) {
			List<MyUser> visitors = myUserService.findByUserType(UserType.VISITOR);
			for(int i = 0; i < visitors.size(); i++) {
				for(int j = 0; j < visitors.get(i).getShoppingCart().getBooks().size(); j++) {
					if(visitors.get(i).getShoppingCart().getBooks().get(j).equals(deleteBook)) {
						purchased = true;
						
						if(purchased) {
							logger.info("\n\t\tIt is not possible to delete book " + deleteBook.getTitle() + " because it has already been ordered by the buyer.\n");
							return new ResponseEntity<Book>(deleteBook, HttpStatus.ACCEPTED); //202
						}
					}
				}
			}
			
			
			bookService.delete(deleteBook.getId());
			logger.info("\n\t\tUser " + encryptedString + " has deleted book " + deleteBook.getTitle() + ".\n");
			return new ResponseEntity<Book>(deleteBook, HttpStatus.OK);
		}
		
		logger.info("\n\t\tFailed to delete book " + deleteBook.getTitle() + ".");
		return new ResponseEntity<Book>(deleteBook, HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("isAuthenticated()")
	@PermissionAnnotation(name = "BUY_BOOK")
	@RequestMapping(value="/buy/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> buy(@PathVariable Long id) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
		MyUser myUser = myUserService.getCurrentUser();
		String encryptedString = Encryptor.decrypt(myUser.getEmail());
		
		Book book = bookService.findOne(id);
					
		boolean iPurchasedTheBook = false;
		boolean somebodyElsePurchasedTheBook = false;
		
		if(myUser != null && myUser.getUserType().equals(UserType.VISITOR) && book != null) {			
			List<MyUser> otherVisitors = myUserService.findAll();
			for(int i = 0; i < otherVisitors.size(); i++) {
				if(otherVisitors.get(i).equals(myUser)) {
					otherVisitors.remove(i);
				}
			}
			
			for(int j = 0; j < otherVisitors.size(); j++) {
				for(int k = 0 ; k < otherVisitors.get(j).getShoppingCart().getBooks().size(); k++) {
					if(otherVisitors.get(j).getShoppingCart().getBooks().get(k).equals(book)) {
						somebodyElsePurchasedTheBook = true;
					}
				}
			}			
			
			for(int i = 0; i < myUser.getShoppingCart().getBooks().size(); i++) {
				if(myUser.getShoppingCart().getBooks().get(i).getId().equals(book.getId())) {
					iPurchasedTheBook = true;
				}
			}
			
			if(!iPurchasedTheBook && !somebodyElsePurchasedTheBook) {
				myUser.getShoppingCart().getBooks().add(book);
				myUserService.save(myUser);
				logger.info("\n\t\tUser " + encryptedString + " has purchased a book " + book.getTitle() + ".\n");
				return new ResponseEntity<Book>(book, HttpStatus.OK);
			} else if(iPurchasedTheBook) {
				logger.info("\n\t\tUser " + encryptedString + " has already purchased the book " + book.getTitle() + ".\n");
				return new ResponseEntity<Book>(book, HttpStatus.ACCEPTED); //202
			} else {
				logger.info("\n\t\tUser " + encryptedString + " has already purchased the book " + book.getTitle() + ".\n");
				return new ResponseEntity<Book>(book, HttpStatus.NON_AUTHORITATIVE_INFORMATION); //203
			}
		}
		
		logger.info("\n\t\tFailed to buy book " + book.getTitle() + ".");
		return new ResponseEntity<Book>(book, HttpStatus.NOT_FOUND);
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/{idCurrentlyActive}/booksInMyShoppingCart", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> booksInMyShoppingCart(@PathVariable Long idCurrentlyActive) {
		MyUser myUser = myUserService.findOne(idCurrentlyActive);
		
		if (myUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Book>>(myUser.getShoppingCart().getBooks(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/search/{title}/{author}", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> search(@PathVariable String title, @PathVariable String author) {
		if(title.equals("noInput") && author.equals("noInput")) {
			return new ResponseEntity<List<Book>>(bookService.findAll(), HttpStatus.OK);
		} else if(!title.equals("noInput") && author.equals("noInput")) {
			return new ResponseEntity<List<Book>>(bookService.findByTitleIgnoreCaseContaining(title), HttpStatus.OK);
		} else if(title.equals("noInput") && !author.equals("noInput")) {
			return new ResponseEntity<List<Book>>(bookService.findByAuthorIgnoreCaseContaining(author), HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Book>>(bookService.findByTitleIgnoreCaseContainingAndAuthorIgnoreCaseContaining(title, author), HttpStatus.OK);
		}
	}
	
}

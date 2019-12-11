package bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bookstore.model.Book;

@Service
@Transactional
public interface BookService {

	Book findOne(Long id);	
	List<Book> findAll();	
	List<Book> findByTitleIgnoreCaseContaining(String title);	
	List<Book> findByAuthorIgnoreCaseContaining(String author);
	List<Book> findByTitleIgnoreCaseContainingAndAuthorIgnoreCaseContaining(String title, String author);	
	
	Book save(Book book);
	
	Book delete(Long id);
	
}

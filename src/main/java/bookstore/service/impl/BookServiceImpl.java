package bookstore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bookstore.model.Book;
import bookstore.repository.BookRepository;
import bookstore.service.BookService;

@Service
@Transactional
public class BookServiceImpl implements BookService {

	@Autowired
	BookRepository bookRepository;
	
	@Override
	public Book findOne(Long id) {
		return bookRepository.findById(id);
	}

	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
	}

	@Override
	public List<Book> findByTitleIgnoreCaseContaining(String title) {
		return bookRepository.findByTitleIgnoreCaseContaining(title);
	}

	@Override
	public List<Book> findByAuthorIgnoreCaseContaining(String author) {
		return bookRepository.findByAuthorIgnoreCaseContaining(author);
	}
	
	@Override
	public List<Book> findByTitleIgnoreCaseContainingAndAuthorIgnoreCaseContaining(String title, String author) {
		return bookRepository.findByTitleIgnoreCaseContainingAndAuthorIgnoreCaseContaining(title, author);
	}

	@Override
	public Book save(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public Book delete(Long id) {
		Book book = bookRepository.findById(id);
		if(book == null) {
			throw new IllegalArgumentException("Attempt to delete non-existent book.");
		}
		bookRepository.delete(book);
		return book;
	}
	
}

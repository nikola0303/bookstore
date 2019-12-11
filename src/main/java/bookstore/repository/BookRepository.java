package bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bookstore.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	Book findById(Long id);
	List<Book> findAll();
	List<Book> findByTitleIgnoreCaseContaining(String title);
	List<Book> findByAuthorIgnoreCaseContaining(String author);
	List<Book> findByTitleIgnoreCaseContainingAndAuthorIgnoreCaseContaining(String title, String author);
	
}

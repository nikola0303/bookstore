package bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bookstore.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
	
	Message findById(Long id);
	List<Message> findAll();

}

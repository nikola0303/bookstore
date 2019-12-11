package bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bookstore.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	
	
}

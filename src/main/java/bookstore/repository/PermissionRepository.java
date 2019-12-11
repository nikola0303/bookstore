package bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import bookstore.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long>  {

	List<Permission> findByRoleId(Long id);
	
}

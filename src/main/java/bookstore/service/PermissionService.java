package bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bookstore.model.Permission;

@Service
@Transactional
public interface PermissionService {

	Permission findById(Long id);
	List<Permission> findByRoleId(Long id);
	
	Permission save(Permission permission);
	void delete(Permission permission);
	
}

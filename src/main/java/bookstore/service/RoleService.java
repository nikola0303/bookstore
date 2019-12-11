package bookstore.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bookstore.model.Role;

@Service
@Transactional
public interface RoleService {

	Role findById(Long id);

	Role save(Role role);
	void delete(Role role);

}

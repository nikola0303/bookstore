package bookstore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bookstore.model.Role;
import bookstore.repository.RoleRepository;
import bookstore.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	public Role findById(Long id) {
		return roleRepository.findOne(id);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public Role save(Role role) {
		return roleRepository.save(role);
	}

	public void delete(Role role) {
		roleRepository.delete(role);
	}
	
}

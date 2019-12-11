package bookstore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bookstore.model.Permission;
import bookstore.repository.PermissionRepository;
import bookstore.service.PermissionService;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	public Permission findById(Long id) {
		return permissionRepository.findOne(id);
	}

	public List<Permission> findByRoleId(Long id) {
		return permissionRepository.findByRoleId(id);
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public Permission save(Permission permission) {
		return permissionRepository.save(permission);
	}

	public void delete(Permission permission) {
		permissionRepository.delete(permission);
	}
	
}

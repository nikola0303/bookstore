package bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bookstore.model.MyUser;
import bookstore.model.Role;
import bookstore.model.UserType;

@Service
@Transactional
public interface MyUserService {

	void setCurrentUser(MyUser user);	
	MyUser getCurrentUser();
	
	MyUser findOne(Long id);	
	List<MyUser> findAll();	
	MyUser findByEmail(String email);
	List<MyUser> findByUserType(UserType userType);
	List<MyUser> findByRole(Role role);
	
	MyUser save(MyUser user);
	
	MyUser delete(Long id);
	
}

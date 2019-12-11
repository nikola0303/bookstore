package bookstore.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bookstore.model.MyUser;
import bookstore.model.Role;
import bookstore.model.UserType;
import bookstore.repository.MyUserRepository;
import bookstore.repository.ShoppingCartRepository;
import bookstore.service.MyUserService;

@Service
@Transactional
public class MyUserServiceImpl implements MyUserService {

	@Autowired
	MyUserRepository myUserRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	ShoppingCartRepository shoppingCartRepository;
	
	public void setCurrentUser(MyUser user) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        Authentication authentication = new PreAuthenticatedAuthenticationToken(user.getId(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
	
	public MyUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            Long id = Long.parseLong(auth.getName());
            return myUserRepository.findOne(id);
        } catch (Exception e) {
            return null;
        }
    }
	
	public MyUser findOne(Long id) {
		return myUserRepository.findById(id);
	}
	
	public List<MyUser> findAll() {
		return myUserRepository.findAll();
	}
	
	public MyUser findByEmail(String email) {
		return myUserRepository.findByEmail(email);
	}
	
	public List<MyUser> findByUserType(UserType userType) {
		return myUserRepository.findByUserType(userType);
	}
	
	public List<MyUser> findByRole(Role role) {
		return myUserRepository.findByRole(role);
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public MyUser save(MyUser user) {
		String file ="commonlyUsedPasswords.txt";
		List<String> pattern = new ArrayList<String>();
		
	    try{
	        InputStream ips = new FileInputStream(file); 
	        InputStreamReader ipsr = new InputStreamReader(ips);
	        BufferedReader br = new BufferedReader(ipsr);
	        String line;
	        
	        while ((line = br.readLine()) != null){
	        	pattern.add(line);
	        }
	        
	        br.close();
	    }       
	    catch (Exception e){
	        System.out.println("\n\n\t\tError trying to read from a file with frequently used passwords.\n");
	    } 
	    
	    //plaintext vs plaintext
	    for(int i = 0; i < pattern.size(); i++) {
	    	if(user.getPassword().equals(pattern.get(i))) {
	    		System.out.println("\n\n\t\t\tThe password is in the list of commonly used ones. You must select a new password.");
	    		return null;
	    	}
	    }
	    
	    //encoded
	    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
	    
	    shoppingCartRepository.save(user.getShoppingCart());
		return myUserRepository.save(user);
	}
	
	public MyUser delete(Long id) {
		MyUser user = myUserRepository.findById(id);
		if(user == null) {
			throw new IllegalArgumentException("Attempt to delete non-existent user.");
		}
		myUserRepository.delete(user);
		return user;
	}
	
}

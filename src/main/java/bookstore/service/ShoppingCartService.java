package bookstore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bookstore.model.ShoppingCart;

@Service
@Transactional
public interface ShoppingCartService {

	ShoppingCart findOne(Long id);	
	List<ShoppingCart> findAll();
	
	ShoppingCart save(ShoppingCart shoppingCart);
	
	ShoppingCart delete(Long id);
	
}

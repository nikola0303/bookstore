package bookstore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bookstore.model.ShoppingCart;
import bookstore.repository.ShoppingCartRepository;
import bookstore.service.ShoppingCartService;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	ShoppingCartRepository shoppingCartRepository;
	
	@Override
	public ShoppingCart findOne(Long id) {
		return shoppingCartRepository.findById(id);
	}

	@Override
	public List<ShoppingCart> findAll() {
		return shoppingCartRepository.findAll();
	}

	@Override
	public ShoppingCart save(ShoppingCart shoppingCart) {
		return shoppingCartRepository.save(shoppingCart);
	}

	@Override
	public ShoppingCart delete(Long id) {
		ShoppingCart shoppingCart = shoppingCartRepository.findById(id);
		if(shoppingCart == null) {
			throw new IllegalArgumentException("Attempt to delete non-existent shopping cart.");
		}
		shoppingCartRepository.delete(shoppingCart);
		return shoppingCart;
	}
	
}

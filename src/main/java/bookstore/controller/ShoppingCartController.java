package bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bookstore.model.ShoppingCart;
import bookstore.service.ShoppingCartService;

@RestController
@RequestMapping(value = "/shoppingCart")
public class ShoppingCartController {

	@Autowired
	ShoppingCartService shoppingCartService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ShoppingCart> getOne(@PathVariable Long id) {
		ShoppingCart shoppingCart = shoppingCartService.findOne(id);
		
		if (shoppingCart == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(shoppingCart, HttpStatus.OK);
	}
	
}

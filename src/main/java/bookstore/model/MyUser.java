package bookstore.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class MyUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false) 
	private String name;
	
	@Column(nullable = false) 
	private String surname;
	
	@Column(nullable = false) 
	private String address;
	
	@Column(nullable = false) 
	private String phone;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false) 
	private String password; 
	
	@Column(nullable = false)
	private boolean activatedAccount;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserType userType;
	
	/*@Column(nullable = false)
	@Enumerated(EnumType.STRING)*/
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_role", nullable = false)
	private Role role;
	
	@OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JoinColumn(name = "id_shopping_cart", nullable = false)
	private ShoppingCart shoppingCart;
	
	public MyUser() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActivatedAccount() {
		return activatedAccount;
	}

	public void setActivatedAccount(boolean activatedAccount) {
		this.activatedAccount = activatedAccount;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

}

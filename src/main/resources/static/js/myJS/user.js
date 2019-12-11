$(document).ready(function() {
	$.ajax({
		url: "/myUser/getCurrentlyActive",
		type: "GET"
	}).then(function(data){
		$('#nameUpdateAccount').val(data.name);
		$('#surnameUpdateAccount').val(data.surname);
		$('#addressUpdateAccount').val(data.address);
		$('#phoneUpdateAccount').val(data.phone);
		
		$('#currentlyActive').append(data.name + " " + data.surname);
	});
});



function register(){
	var check = true;
	var forma = $('form[id="registrationForm"]');
	var name = forma.find('[name=name]').val();
	var surname = forma.find('[name=surname]').val();
	var address = forma.find('[name=address]').val();
	var phone = forma.find('[name=phone]').val();
	var email = forma.find('[name=email]').val();
	var password = forma.find('[name=password]').val();
	var password2 = forma.find('[name=password2]').val();
		
	var emailRegex = /^[a-zA-Z]+[0-9a-zA-Z.!#$_*]+[@][0-9a-zA-Z]+[.][0-9a-zA-Z]+/;
	if(email.length > 50 || email.length < 4 || (!emailRegex.test(email))){
		$('#divValidation').empty();
		$('#divValidation').append('<p style="color:red"><b>The email address must follow a specific form.</b></p>');
		check = false;
	}

	var passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{10,30}/;
	if(password.length > 30 || password.length < 10 || !isNaN(password) || (!passwordRegex.test(password))
	|| password == "123456" || password == "password" || password == "12345678" || password == "qwerty" || password == "12345" || 
	password == "123456789" || password == "letmein" || password == "1234567" || password == "football" || password == "iloveyou" || 
	password == "admin" || password == "welcome" || password == "monkey" || password == "login" || password == "abc123" || 
	password == "starwars" || password == "123123" || password == "dragon" || password == "passw0rd" || password == "master" || 
	password == "hello" || password == "freedom" || password == "whatever" || password == "qazwsx" || password == "trustno1" || 
	password == "123456789@Bs"){
	 	$('#divValidation').empty();
		$('#divValidation').append('<p style="color:red"><b>The password you entered should not be on the list of commonly used passwords.</b></p>');
		check = false;
	}
	if(password2.length > 30 || password2.length < 10 || !isNaN(password2) || (!passwordRegex.test(password2))
	|| password2 == "123456" || password2 == "password" || password2 == "12345678" || password2 == "qwerty" || password2 == "12345" || 
	password2 == "123456789" || password2 == "letmein" || password2 == "1234567" || password2 == "football" || password2 == "iloveyou" || 
	password2 == "admin" || password2 == "welcome" || password2 == "monkey" || password2 == "login" || password2 == "abc123" || 
	password2 == "starwars" || password2 == "123123" || password2 == "dragon" || password2 == "passw0rd" || password2 == "master" || 
	password2 == "hello" || password2 == "freedom" || password2 == "whatever" || password2 == "qazwsx" || password2 == "trustno1" || 
	password2 == "123456789@Bs"){
		$('#divValidation').empty();
		$('#divValidation').append('<p style="color:red"><b>The password you entered should not be on the list of commonly used passwords.</b></p>');
		check = false;
	}
	if(password != password2) {
		$('#divValidation').empty();
		$('#divValidation').append('<p style="color:red"><b>The password must be the same in both fields.</b></p>');
		check = false;
	}		
			
	if(check) {
		$("#btnRegister").prop("disabled",true);			
		$('#divValidation').empty();
				
		formData = JSON.stringify({
			name:$("#registrationForm [name='name']").val(),
			surname:$("#registrationForm [name='surname']").val(),
			address:$("#registrationForm [name='address']").val(),
			phone:$("#registrationForm [name='phone']").val(),
			email:$("#registrationForm [name='email']").val(),
			password:$("#registrationForm [name='password']").val()
		});
				
		$.ajax({
			url: "/myUser/registration",
			type: "POST",
			data: formData,
			contentType: "application/json",
			datatype: 'json',
			crossDomain: true,
			headers: {  'Access-Control-Allow-Origin': '*' },
			xhrFields: {
				withCredentials: true
			},
			success: function(data){
				if(data){
					swal({
					     title: "",
					     text: "You have successfully registered.",
					     icon: "success",
					     timer: 2000,
					     buttons: false
					}).then(() => {
						location.href = "/index.html"
					});
				}else{
					swal({
						  title: "",
						  text: "Failed to register user!",
						  icon: "error",
						  timer: 2000,
						  buttons: false
					});
				}
			},
			error: function(data){
				swal({
					  title: "",
					  text: "ERROR!!!",
					  icon: "error",
					  timer: 2000,
					  buttons: false
				});
			}
		});
	}
}



function login(){	
	var forma = $('form[id="loginForm"]');
	var email = forma.find('[name=email]').val();
	var password = forma.find('[name=password]').val();
			
	formData = JSON.stringify({
		email:$("#loginForm [name='email']").val(),
		password:$("#loginForm [name='password']").val()
	});
			
	$.ajax({
		url: "/myUser/login",
		type: "POST",
		data: formData,
		contentType: "application/json",
		datatype: 'json',
		crossDomain: true,
	    headers: {  'Access-Control-Allow-Origin': '*' },
		xhrFields: {
			withCredentials: true
		},
		success: function(data){
			if(data){
				window.swal({
					title: "Checking...",
					text: "Please wait",
					imageUrl: "images/load.gif",
					showConfirmButton: false,
					allowOutsideClick: false
				});
					
				setTimeout(() => {
					if(data.userType == "ADMIN")
						location.href = "/home-page-admin.html"
					else
						location.href = "/home-page-visitor.html"
				}, 2000);
			} else {
				swal({
					  title: "",
					  text: "Failed to login!",
					  icon: "error",
					  timer: 2000,
					  buttons: false
				});
			}
		},
		error: function(data){
			window.swal({
				title: "",
				text: "ERROR!!!\n\n1. user account not activated\n2. wrong credentials\n3. user not registered",
				imageUrl: "images/error.gif",
				showConfirmButton: false,
				allowOutsideClick: true,
				timer: 2500
			});
		}
	});
}



function logout() {
	$.ajax({
		url: "/myUser/logout",
		type: "GET",
		contentType: "application/json",
		datatype: 'json',
		crossDomain: true,
		headers: {  'Access-Control-Allow-Origin': '*' },
		xhrFields: {
			withCredentials: true
		},
		success: function(data){
			if(data){
				window.swal({
					title: "Logging out...",
					text: "Please wait",
					icon: "images/logout.gif",
					timer: 2000,
					buttons: false
				});
					
				setTimeout(() => {
					location.href = "/index.html"
				}, 2000);
				
			}else{
				swal({
					  title: "",
					  text: "Failed logout!",
					  icon: "error",
					  timer: 2000,
					  buttons: false
				});
			}
		},
		error: function(data){
			swal({
				  title: "",
				  text: "ERROR!!!",
				  icon: "error",
				  timer: 2000,
				  buttons: false
			});
		}
	});	
}



function updateAccount() {
	var forma = $('form[id="updateAccountForm"]');
	var name = forma.find('[name=nameUpdate]').val();
	var surname = forma.find('[name=surnameUpdate]').val();
	var address = forma.find('[name=addressUpdate]').val();
	var phone = forma.find('[name=phoneUpdate]').val();
	
	formData = JSON.stringify({
		name:$("#updateAccountForm [name='nameUpdate']").val(),
		surname:$("#updateAccountForm [name='surnameUpdate']").val(),
		address:$("#updateAccountForm [name='addressUpdate']").val(),
		phone:$("#updateAccountForm [name='phoneUpdate']").val(),
		password:$("#updateAccountForm [name='passwordUpdate']").val()
	});
		
	$.ajax({
		url: "/myUser/update",
		type: "PUT",
		data: formData,
		contentType: "application/json",
		datatype: 'json',
		crossDomain: true,
		headers: {  'Access-Control-Allow-Origin': '*' },
		xhrFields: {
			withCredentials: true
		},
		success: function(data){
			if(data){
				swal({
				     title: "",
				     text: "Your account was successfully updated!",
				     icon: "success",
				     timer: 2000,
				     buttons: false
				});
								
				$('#modalUpdateUser').modal('toggle');
				window.setTimeout(function(){location.reload();},1500);
			}else{
				swal({
					  title: "",
					  text: "Failed to update your account!",
					  icon: "error",
					  timer: 2000,
					  buttons: false
				});
			}
		},
		error: function(data){
			swal({
				  title: "",
				  text: "ERROR!!!",
				  icon: "error",
				  timer: 2000,
				  buttons: false
			});
		}
	});
}



function changePassword() {
	var check = true;
	var forma = $('form[id="changePasswordForm"]');
	var password = forma.find('[name=passwordUpdate]').val();
	var password2 = forma.find('[name=passwordUpdate2]').val();
	
	var passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{10,30}/;
	if(password.length > 30 || password.length < 10 || !isNaN(password) || (!passwordRegex.test(password))
	|| password == "123456" || password == "password" || password == "12345678" || password == "qwerty" || password == "12345" || 
	password == "123456789" || password == "letmein" || password == "1234567" || password == "football" || password == "iloveyou" || 
	password == "admin" || password == "welcome" || password == "monkey" || password == "login" || password == "abc123" || 
	password == "starwars" || password == "123123" || password == "dragon" || password == "passw0rd" || password == "master" || 
	password == "hello" || password == "freedom" || password == "whatever" || password == "qazwsx" || password == "trustno1" || 
	password == "123456789@Bs"){
	 	$('#divValidationChangePassword').empty();
		$('#divValidationChangePassword').append('<p style="color:red"><b>The password you entered should not be on the list of commonly used passwords.</b></p>');
		check = false;
	}
	if(password2.length > 30 || password2.length < 10 || !isNaN(password2) || (!passwordRegex.test(password2))
	|| password2 == "123456" || password2 == "password" || password2 == "12345678" || password2 == "qwerty" || password2 == "12345" || 
	password2 == "123456789" || password2 == "letmein" || password2 == "1234567" || password2 == "football" || password2 == "iloveyou" || 
	password2 == "admin" || password2 == "welcome" || password2 == "monkey" || password2 == "login" || password2 == "abc123" || 
	password2 == "starwars" || password2 == "123123" || password2 == "dragon" || password2 == "passw0rd" || password2 == "master" || 
	password2 == "hello" || password2 == "freedom" || password2 == "whatever" || password2 == "qazwsx" || password2 == "trustno1" || 
	password2 == "123456789@Bs"){
		$('#divValidationChangePassword').empty();
		$('#divValidationChangePassword').append('<p style="color:red"><b>The password you entered should not be on the list of commonly used passwords.</b></p>');
		check = false;
	}
	if(password != password2) {
		$('#divValidationChangePassword').empty();
		$('#divValidationChangePassword').append('<p style="color:red"><b>The password must be the same in both fields.</b></p>');
		check = false;
	}
	
	formData = JSON.stringify({
		password:$("#changePasswordForm [name='passwordUpdate']").val()
	});
	
	if(check) {
		$('#divValidationChangePassword').empty();
		
		$.ajax({
			url: "/myUser/changePassword",
			type: "PUT",
			data: formData,
			contentType: "application/json",
			datatype: 'json',
			crossDomain: true,
		    headers: {  'Access-Control-Allow-Origin': '*' },
			xhrFields: {
				withCredentials: true
			},
			success: function(data){
				if(data){
					swal({
					     title: "",
					     text: "Your password was successfully changed. You will be logged out of the system!",
					     icon: "success",
					     timer: 2000,
					     buttons: false
					});
					
					$('#modalUpdateUser').modal('toggle');
					window.setTimeout(function(){ logout(); }, 2000);
				}else{
					swal({
						  title: "",
						  text: "Failed to change password!",
						  icon: "error",
						  timer: 2000,
						  buttons: false
					});
				}
			},
			error: function(data){
				swal({
					  title: "",
					  text: "ERROR!!!",
					  icon: "error",
					  timer: 2000,
					  buttons: false
				});
			}
		});
	}
}



function deactivateAccount() {	
	$.ajax({
		url: "/myUser/deactivateAccount",
		type: "PUT",
		contentType: "application/json",
		datatype: 'json',
		crossDomain: true,
	    headers: {  'Access-Control-Allow-Origin': '*' },
		xhrFields: {
			withCredentials: true
		},
		success: function(data){
			if(data){
				swal({
				     title: "",
				     text: "Your account was successfully deactivated. You will be logged out of the system!",
				     icon: "success",
				     timer: 2000,
				     buttons: false
				});
				
				$('#modalUpdateUser').modal('toggle');
				window.setTimeout(function(){ logout(); }, 2000);
			}else{
				swal({
					  title: "",
					  text: "Failed to deactivate account!",
					  icon: "error",
					  timer: 2000,
					  buttons: false
				});
			}
		},
		error: function(data){
			swal({
				  title: "",
				  text: "ERROR!!!",
				  icon: "error",
				  timer: 2000,
				  buttons: false
			});
		}
	});
}




function sendMessage() {
	var forma = $('form[id="sendMessageForm"]');
	var nameAndSurname = forma.find('[name=nameAndSurname]').val();
	var email = forma.find('[name=email]').val();
	var message = forma.find('[name=message]').val();
			
	formData = JSON.stringify({
		sendersNameAndSurname:$("#sendMessageForm [name='nameAndSurname']").val(),
		sendersEmail:$("#sendMessageForm [name='email']").val(),
		textMessage:$("#sendMessageForm [name='message']").val()
	});
			
	$.ajax({
		url: "/message/send",
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
				     text: "Your message was successfully sent.",
				     icon: "success",
				     timer: 2000,
				     buttons: false
				}).then(() => {
					location.reload();
				});
			}else{
				swal({
					  title: "",
					  text: "Failed to send message!",
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



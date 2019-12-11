$(document).ready(function() {
	$.ajax({
		url : "/myUser/getAllVisitors",
		type : "GET",
		contentType : "application/json",
		datatype : 'json',
		success : function(data) {
			if (data) {
				if (data.length == 0) {
					$("#labelNoVisitors").empty();
					$("#divBodyModalVisitors").empty();
					
					red = "<i>No registered visitors available.</i>";
					$("#labelNoVisitors").append(red);
				} else {
					$("#labelNoVisitors").empty();
					$("#divBodyModalVisitors").empty();
					
					for (i = 0; i < data.length; i++) {
						noviRed = "<div class=\"row\" style=\"padding:30px\"><img src=\"images/user.png\" width=\"30px\" height=\"40px\"><h6 style=\"padding:15px\">" + data[i].name + " " + data[i].surname + "</h6><button class=\"btn black\" onclick=\"javaScript:deleteVisitor(" + data[i].id + ")\">Delete</button></div>";
						$("#divBodyModalVisitors").append(noviRed);
					}
				}
			} else{
				swal({
					  title: "",
					  text: "Error trying to display the list of visitors!",
					  icon: "error",
					  timer: 2000,
					  buttons: false
				});
			}
		},
		error : function(data) {
			swal({
				  title: "",
				  text: "ERROR!!!",
				  icon: "error",
				  timer: 2000,
				  buttons: false
			});
		}
	});
});



function addBook() {
	var forma = $('form[id="addBookForm"]');
	var title = forma.find('[name=title]').val();
	var author = forma.find('[name=author]').val();
	var description = forma.find('[name=description]').val();
	var publishingYear = forma.find('[name=publishingYear]').val();
	var publisher = forma.find('[name=publisher]').val();
	var price = forma.find('[name=price]').val();
	
	var path = document.getElementById("image");
	image = "images/" + path.value.split("\\")[2];
			
	formData = JSON.stringify({
		title:$("#addBookForm [name='title']").val(),
		author:$("#addBookForm [name='author']").val(),
		description:$("#addBookForm [name='description']").val(),
		publishingYear:$("#addBookForm [name='publishingYear']").val(),
		publisher:$("#addBookForm [name='publisher']").val(),
		price:$("#addBookForm [name='price']").val(),
		image:image
	});
			
	$.ajax({
		url: "/book/add",
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
				     text: "Successfully adding a new book.",
				     icon: "success",
				     timer: 2000,
				     buttons: false
				});
				
				$('#modalAddBook').modal('toggle');
				window.setTimeout(function(){location.reload();},1500);
			} else {
				swal({
					  title: "",
					  text: "Failed to add book!",
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



function modalUpdateBook(id) {
	$("#modalUpdateBook").modal();
	$("#divUpdateBookForm").empty();
	
	form = "<form action=\"javascript:updateBook(" + id + ")\" id=\"updateBookForm\" method=\"POST\"><div style=\"padding: 15px\"><input type=\"text\" id=\"titleUpdate\" name=\"titleU\" placeholder=\"title\" required><span class=\"required-star\" style=\"color:red\"> *</span></div><div style=\"padding: 15px\"><input type=\"text\" id=\"authorUpdate\" name=\"authorU\" placeholder=\"author\" required><span class=\"required-star\" style=\"color:red\"> *</span></div><div style=\"padding: 15px\"><input type=\"text\" id=\"descriptionUpdate\" name=\"descriptionU\" placeholder=\"description\" required><span class=\"required-star\" style=\"color:red\"> *</span></div><div style=\"padding: 15px\"><input type=\"number\" id=\"publishingYearUpdate\" name=\"publishingYearU\" placeholder=\"publishing year\" required><span class=\"required-star\" style=\"color:red\"> *</span></div><div style=\"padding: 15px\"><input type=\"text\" id=\"publisherUpdate\" name=\"publisherU\" placeholder=\"publisher\" required><span class=\"required-star\" style=\"color:red\"> *</span></div><div style=\"padding: 15px\"><input type=\"number\" id=\"priceUpdate\" name=\"priceU\" placeholder=\"price\" required><span class=\"required-star\" style=\"color:red\"> *</span></div><div style=\"padding: 15px\" id=\"divValidationUpdate\"></div><div class=\"col-md-2 col-md-3\"><button class=\"btn yellow\" id=\"btnUpdate\" type=\"submit\">Update</button></div></form>";
	$("#divUpdateBookForm").append(form);

	$.ajax({
		url: "/book/" + id,
		type: "GET"
	}).then(function(data){
		$('#titleUpdate').val(data.title);
		$('#authorUpdate').val(data.author);
		$('#descriptionUpdate').val(data.description);
		$('#publishingYearUpdate').val(data.publishingYear);
		$('#publisherUpdate').val(data.publisher);
		$('#priceUpdate').val(data.price);
	});
}



function updateBook(id) {
	var forma = $('form[id="updateBookForm"]');
	var title = forma.find('[name=titleU]').val();
	var author = forma.find('[name=authorU]').val();
	var description = forma.find('[name=descriptionU]').val();
	var publishingYear = forma.find('[name=publishingYearU]').val();
	var publisher = forma.find('[name=publisherU]').val();
	var price = forma.find('[name=priceU]').val();
			
	formData = JSON.stringify({
		title:$("#updateBookForm [name='titleU']").val(),
		author:$("#updateBookForm [name='authorU']").val(),
		description:$("#updateBookForm [name='descriptionU']").val(),
		publishingYear:$("#updateBookForm [name='publishingYearU']").val(),
		publisher:$("#updateBookForm [name='publisherU']").val(),
		price:$("#updateBookForm [name='priceU']").val()
	});
			
	$.ajax({
		url: "/book/update/" + id,
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
				     text: "Successfully updating a book.",
				     icon: "success",
				     timer: 2000,
				     buttons: false
				});
				
				$('#modalUpdateBook').modal('toggle');
				window.setTimeout(function(){location.reload();},1500);
			}else{
				swal({
					  title: "",
					  text: "Failed to update book!",
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



function deleteBook(id) {
	$("#modalDeleteBook").modal();
		
	$("#btnYesDeleteBookModal").click(function(){
		$.ajax({
			url: "/book/delete/" + id,
			type: "DELETE",
			contentType: "application/json",
			datatype: 'json',
			crossDomain: true,
		    headers: {  'Access-Control-Allow-Origin': '*' },
			xhrFields: {
				withCredentials: true
			},
			success: function(data, statusText, xhr){
				if(data){
					var status = xhr.status;
					
					if(status == 200){					
						swal({
						     title: "",
						     text: "Successfully deleting a book.",
						     icon: "success",
						     timer: 2000,
						     buttons: false
						});
					} else {
						swal({
						     title: "Deletion disabled",
						     text: "The book has already been ordered!",
						     icon: "error",
						     timer: 2000,
						     buttons: false
						});
					}
					
					$('#modalDeleteBook').modal('toggle');
					window.setTimeout(function(){location.reload();},1500);
				}else{
					swal({
						  title: "",
						  text: "Failed to delete book!",
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
	});
	
	$("#btnNoDeleteBookModal").click(function(){
		$('#modalDeleteBook').modal('toggle');
	});
}



function deleteVisitor(id){
	$.ajax({
		url: "/myUser/delete/" + id,
		type: "DELETE",
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
				     text: "User successfully deleted.",
				     icon: "success",
				     timer: 2000,
				     buttons: false
				}).then(() => {
					location.reload();
				});
			}else{
				swal({
					  title: "",
					  text: "Failed to delete user!",
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



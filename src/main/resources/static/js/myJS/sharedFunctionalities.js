$(document).ready(function() {
	$.ajax({
		url : "/book/getAll",
		type : "GET",
		contentType : "application/json",
		datatype : 'json',
		success : function(data) {
			if (data) {
				if (data.length == 0) {
					red = "<p style=\"color:orange\"><i><b>There are currently no products available.</b></i></p>";
					$("#noBooksForBuyingLabel").append(red);
					$("#noBooksForBuyingLabel").show();
				} else {
					$("#noBooksForBuyingLabel").empty();
					$("#noBooksForBuyingLabel").show();
					
					for (i = 0; i < data.length; i++) {
						//admin
						htmlRowAdmin = "<div class=\"col-lg-3 col-md-6\"><div class=\"item\"><img src=" + data[i].image + " width=\"90px\" height=\"150px\" alt=\"img\"><h3><a href=\"javascript:details(" + data[i].id + ")\"><strong>" + data[i].title + "</strong></a></h3><h6 style=\"padding-bottom:15px\"><i>" + data[i].author + "<i></h6><h6><span class=\"price\">" + data[i].price + " RSD</span></h6><h6 style=\"padding:15px; color:orange\"><a href=\"javascript:modalUpdateBook(" + data[i].id + ")\">Update</a> / <a href=\"javaScript:deleteBook(" + data[i].id + ")\">Delete</a></h6></div><div style=\"padding-bottom: 30px\"></div></div>";
						$("#bookItemsAdmin").append(htmlRowAdmin);
						
						//visitor
						htmlRowVisitor = "<div class=\"col-lg-3 col-md-6\"><div class=\"item\"><img src=" + data[i].image + " width=\"90px\" height=\"150px\" alt=\"img\"><h3><a href=\"javascript:details(" + data[i].id + ")\"><strong>" + data[i].title + "</strong></a></h3><h6 style=\"padding-bottom:15px\"><i>" + data[i].author + "<i></h6><h6><span class=\"price\">" + data[i].price + " RSD / <a href=\"javaScript:buyBook(" + data[i].id + ")\">Buy now</a></span></h6></div><div style=\"padding-bottom: 30px\"></div></div>";
						$("#bookItemsVisitor").append(htmlRowVisitor);
						
						//other (guest)
						htmlRow = "<div class=\"col-lg-3 col-md-6\"><div class=\"item\"><img src=" + data[i].image + " width=\"90px\" height=\"150px\" alt=\"img\"><h3><a href=\"javascript:details(" + data[i].id + ")\"><strong>" + data[i].title + "</strong></a></h3><h6 style=\"padding-bottom:15px\"><i>" + data[i].author + "<i></h6><h6><span class=\"price\">" + data[i].price + " RSD</span></h6></div><div style=\"padding-bottom: 30px\"></div></div>";
						$("#bookItems").append(htmlRow);
					}
				}
			} else {
				swal({
					  title: "",
					  text: "Error trying to display the list of available books!",
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



function details(id) {
	$("#bookDetails").empty();
	
	$.ajax({
		url: "/book/" + id,
		type: "GET",
		contentType: "application/json",
		datatype: 'json',
		success: function(data){
			if(data){
				modalBookDetailHTML = "<div class=\"modal-dialog modal-lg modal-dialog-centered\" role=\"document\"><div class=\"modal-content\"><div class=\"modal-header\"><h5 class=\"modal-title\">" + data.title + "</h5><button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button></div><div class=\"modal-body\"><div class=\"container\"><div class=\"row\"><div><div id=\"myCarousel\" class=\"carousel slide\"><div class=\"carousel-inner\"><div class=\"active item carousel-item\" data-slide-number=\"0\"><img src=\"" + data.image + "\" style=\"width:235px; height:390px\"></div><div class=\"item carousel-item\" data-slide-number=\"1\"><img src=\"images/product2.jpg\" style=\"width:235px; height:390px\"></div><div class=\"item carousel-item\" data-slide-number=\"2\"><img src=\"images/product3.jpg\" style=\"width:235px; height:390px\"></div><ul class=\"carousel-indicators list-inline\"><li class=\"list-inline-item active\"><a id=\"carousel-selector-0\" class=\"selected\" data-slide-to=\"0\" data-target=\"#myCarousel\"><img src=\"" + data.image + "\" style=\"width:100px; height:130px\"></a></li><li class=\"list-inline-item\"><a id=\"carousel-selector-1\" data-slide-to=\"1\" data-target=\"#myCarousel\"><img src=\"images/product2.jpg\" style=\"width:100px; height:130px\"></a></li><li class=\"list-inline-item\"><a id=\"carousel-selector-2\" data-slide-to=\"2\" data-target=\"#myCarousel\"><img src=\"images/product3.jpg\" style=\"width:100px; height:130px\"></a></li></ul></div></div></div><div class=\"col-md-2 col-md-3 col-md-4 col-md-2\"><p><h6>" + data.author + "</h6></p><p><h6>" + data.publisher + ", " + data.publishingYear + "</h6></p><br><p><h6><i>" + data.description + "</i></h6></p><br><p><span class=\"name\">Price</span><span class=\"clm\">: </span><span style=\"color:red\"><b>" + data.price + " RSD</b></span></p></div></div></div></div></div></div></div>";
				$("#bookDetails").append(modalBookDetailHTML);
				$("#bookDetails").modal();
			}else{
				swal({
					  title: "",
					  text: "Failed to show details about book!",
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



function searchBooks() {
	var forma = $('form[id="searchForm"]');
	var title = forma.find('[name=searchTitle]').val();
	var author = forma.find('[name=searchAuthor]').val();
			
	if(title == ""){
		title = "noInput";
	}
	if(author == ""){
		author = "noInput";
	}
	
	$.ajax({
		url: "/book/search/" + title + "/" + author,
		type: "GET",
		contentType: "application/json",
		crossDomain: true,
	    headers: {  'Access-Control-Allow-Origin': '*' },
		xhrFields: {
			withCredentials: true
		},
		success: function(data){			
			$("#noBooksForBuyingLabel").empty();
			$("#bookItemsAdmin").empty();
			$("#bookItemsVisitor").empty();
			$("#bookItems").empty();
			
			if (data) {
				if (data.length == 0) {
					red = "<p style=\"color:orange\"><i><b>No search results.</b></i></p>";
					$("#noBooksForBuyingLabel").append(red);
					$("#noBooksForBuyingLabel").show();
				} else {
					$("#noBooksForBuyingLabel").empty();
					$("#noBooksForBuyingLabel").show();
					
					for (i = 0; i < data.length; i++) {
						//admin
						htmlRowAdmin = "<div class=\"col-lg-3 col-md-6\"><div class=\"item\"><img src=" + data[i].image + " width=\"90px\" height=\"150px\" alt=\"img\"><h3><a href=\"javascript:details(" + data[i].id + ")\"><strong>" + data[i].title + "</strong></a></h3><h6 style=\"padding-bottom:15px\"><i>" + data[i].author + "<i></h6><h6><span class=\"price\">" + data[i].price + " RSD</span></h6><h6 style=\"padding:15px; color:orange\"><a href=\"javascript:modalUpdateBook(" + data[i].id + ")\">Update</a> / <a href=\"javaScript:deleteBook(" + data[i].id + ")\">Delete</a></h6></div><div style=\"padding-bottom: 30px\"></div></div>";
						$("#bookItemsAdmin").append(htmlRowAdmin);
						
						//visitor
						htmlRowVisitor = "<div class=\"col-lg-3 col-md-6\"><div class=\"item\"><img src=" + data[i].image + " width=\"90px\" height=\"150px\" alt=\"img\"><h3><a href=\"javascript:details(" + data[i].id + ")\"><strong>" + data[i].title + "</strong></a></h3><h6 style=\"padding-bottom:15px\"><i>" + data[i].author + "<i></h6><h6><span class=\"price\">" + data[i].price + " RSD / <a href=\"javaScript:buyBook(" + data[i].id + ")\">Buy now</a></span></h6></div><div style=\"padding-bottom: 30px\"></div></div>";
						$("#bookItemsVisitor").append(htmlRowVisitor);
											
						//other (guest)
						htmlRow = "<div class=\"col-lg-3 col-md-6\"><div class=\"item\"><img src=" + data[i].image + " width=\"90px\" height=\"150px\" alt=\"img\"><h3><a href=\"javascript:details(" + data[i].id + ")\"><strong>" + data[i].title + "</strong></a></h3><h6 style=\"padding-bottom:15px\"><i>" + data[i].author + "<i></h6><h6><span class=\"price\">" + data[i].price + " RSD</span></h6></div><div style=\"padding-bottom: 30px\"></div></div>";
						$("#bookItems").append(htmlRow);
					}
				}
			} else{
				swal({
					  title: "",
					  text: "Error when trying to display books with a given title and/or author!",
					  icon: "error",
					  timer: 2000,
					  buttons: false
				});
			}
		},
		error: function(data){
			alert(data)
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



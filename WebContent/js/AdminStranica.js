$(document).ready(function(){
	$.get('AdminStranicaServlet',{'prvaPretraga':true},function(data){
		if(data.ulogovaniKorisnik == null){
			window.location.href = "http://localhost:8080/Youtube/";
		}
		else{
			if(data.ulogovaniKorisnik.vrstaKorisnika != "ADMIN"){
				window.location.href = "http://localhost:8080/Youtube/";
			}else{
				var navbarLogin = $('#navbarLogin');
				navbarLogin.append(
						'<li><a href="KanalStranica.html?channel='+data.ulogovaniKorisnik.korisnickoIme+'" ><span class="glyphicon glyphicon-user"></span> My channel</a></li>'+
	     				'<li><a href="LogoutServlet" ><span class="glyphicon glyphicon-log-out"></span> Log out</a></li>'
				);
				
				if(data.ulogovaniKorisnik.vrstaKorisnika =='ADMIN'){
					var navbarHeader = $('.navbar-header');
					navbarHeader.append(
							'<a href="AdminStranica.html" class="navbar-brand">Admin page</a>'
					);
				}
				popuniDiv(data);
			}
		}
	});
});

function popuniDiv(data){
	var usersDiv = $('#usersDiv');
	usersDiv.empty();
	for(it in data.listaKorisnika){
		if(data.listaKorisnika[it].blokiran == true){
			usersDiv.append(
					'<div class="col-lg-3">'+
	                    '<div class="container" id="containerUser">'+
	                    	'<div class="container">'+
								'<a href="KanalStranica.html?channel='+data.listaKorisnika[it].korisnickoIme+'">'+
				                    '<img src="images/avatar.png" class="avatar">'+
				                    '<div class="cufCaption">'+
					                	'<h3 style="margin-left:20px;">'+data.listaKorisnika[it].korisnickoIme+'</h3>'+
				                    '</div>'+
		                        '</a>'+
		                    '</div>'+
		                '</div>'+
		                '<button id="btnPromote" onclick="promote('+data.listaKorisnika[it].id+')">Promote</button>'+
						'<button id="btnBlock" onclick="unBlock('+data.listaKorisnika[it].id+')">Unblock</button>'+
						'<button id="btnBlock" onclick="delete('+data.listaKorisnika[it].id+')">Delete</button>'+
		                '<div class="container" id="alignCenter">'+
		                	'<h3>First name: '+data.listaKorisnika[it].ime+'</h3>'+
		                	'<h3>Last name: '+data.listaKorisnika[it].prezime+'</h3>'+
		                	'<h3>Email: '+data.listaKorisnika[it].email+'</h3>'+
		                	'<h3>Role: '+data.listaKorisnika[it].vrstaKorisnika+'</h3>'+
		                	'<h3 style="margin-bottom:1em;">Block: '+data.listaKorisnika[it].blokiran+'</h3><hr>'+
		                '</div>'+
	                '</div>'
			);
		}else{
			usersDiv.append(
					'<div class="col-lg-3">'+
	                    '<div class="container" id="containerUser">'+
	                    	'<div class="container">'+
								'<a href="KanalStranica.html?channel='+data.listaKorisnika[it].korisnickoIme+'">'+
				                    '<img src="images/avatar.png" class="avatar">'+
				                    '<div class="cufCaption">'+
					                	'<h3 style="margin-left:20px;">'+data.listaKorisnika[it].korisnickoIme+'</h3>'+
				                    '</div>'+
		                        '</a>'+
		                    '</div>'+
		                '</div>'+
		                '<button id="btnPromote" onclick="promote('+data.listaKorisnika[it].id+')">Promote</button>'+
						'<button id="btnBlock" onclick="block('+data.listaKorisnika[it].id+')">Block</button>'+
						'<button id="btnBlock" onclick="deleteUser('+data.listaKorisnika[it].id+')">Delete</button>'+
		                '<div class="container" id="alignCenter">'+
		                	'<h3>First name: '+data.listaKorisnika[it].ime+'</h3>'+
		                	'<h3>Last name: '+data.listaKorisnika[it].prezime+'</h3>'+
		                	'<h3>Email: '+data.listaKorisnika[it].email+'</h3>'+
		                	'<h3>Role: '+data.listaKorisnika[it].vrstaKorisnika+'</h3>'+
		                	'<h3 style="margin-bottom:1em;">Block: '+data.listaKorisnika[it].blokiran+'</h3><hr>'+
		                '</div>'+
	                '</div>'
			);
		}
	}
}

function promote(id) {
    var r = confirm("Are u sure u want to promote this user  to admin?");
    if (r == true) { 	

    	$.post('AdminStranicaServlet',{'korisnikId': id, 'action' : 'promote'}, function(data) {
    		if(data.alreadyAdmin == true) alert("he is already admin");
            else popuniDiv(data);
        }); 
    } 
	
}
function block(korisnikId) {
	this.korisnikId= korisnikId;
	
    var r = confirm("Are u sure u want to block this user?");
    if (r == true) { 	

    	$.post('AdminStranicaServlet',{'korisnikId': korisnikId, 'action' : 'block'}, function(data) {
            popuniDiv(data);
        }); 
    } 
}
function unBlock(korisnikId) {
	this.korisnikId= korisnikId;
	
    var r = confirm("Are u sure u want to unblock this user?");
    if (r == true) { 	

    	$.post('AdminStranicaServlet',{'korisnikId': korisnikId, 'action' : 'unblock'}, function(data) {
            popuniDiv(data);
        }); 
    } 
}
function deleteUser(korisnikId) {
	this.korisnikId= korisnikId;
	
    var r = confirm("Are u sure u want to delete this user?");
    if (r == true) { 	

    	$.post('DeleteUserServlet',{'id': korisnikId}, function(data) {
    		search();
        });
    } 
}
function search(){
	var searchBy = $('input[name=searchBy]:checked').val();
	var orderBy  = $('input[name=orderBy]:checked').val();
	var ascDesc   = $('input[name=ascDesc]:checked').val();
	var searchInput = $('#txtPretraga');
	var searchText = searchInput.val().trim();
	
	$.ajax({
        url: 'AdminStranicaServlet',
        dataType: 'json',
        contentType : "application/json",
        type: 'get',
        data: {
        	'prvaPretraga':false,
        	'searchText':searchText,
        	'searchBy':searchBy,
        	'orderBy':orderBy,
        	'ascDesc':ascDesc
        },
        success: function(response){
            popuniDiv(response);
        },
        error: function(response){
            console.log(response);
        }
    });	
}
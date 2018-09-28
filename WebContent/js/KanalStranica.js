var globalData;
$(document).ready(function(e){
	var url = location.href;
	var splitovanURL = url.split("?");
	var korisnickoIme = splitovanURL[1].split("=")[1];
	
	$.get('KanalStranicaServlet',{"korisnickoIme": korisnickoIme},function(data){
		if(data.vlasnikKanala == null){
			alert(data.poruka);
			window.location.href = "http://localhost:8080/Youtube/";
		}else{
			globalData = data;
			document.title = data.vlasnikKanala.korisnickoIme;
			if(data.ulogovaniKorisnik!=null){
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
				popuniInfoDiv();
				popuniButtonsDiv();
				if(data.ulogovaniKorisnik.id == data.vlasnikKanala.id && data.vlasnikKanala.blokiran == false){
					document.getElementById('userVideosDiv').innerHTML  = data.vlasnikKanala.korisnickoIme + " videos";	
					var addVideoDiv = $('.addVideoDiv');
					addVideoDiv.append(
							'<button id="dodajBtn" type="button" onclick=addNewVideo()>Add</button>'
					);
				}
			}
			else{
				var navbarLogin = $('#navbarLogin');
				navbarLogin.append(
					'<li><a href="Registracija.html" ><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>'+
	                '<li><a href="Login.html" ><span class="glyphicon glyphicon-log-in"></span> Login</a></li>'
					);
				popuniInfoDiv();
				popuniButtonsDiv();
				if(data.blokiran == true){
					alert(data.poruka);
					window.location.href = "http://localhost:8080/Youtube/";
				}
			}
			document.getElementById('userVideosDiv').innerHTML  = data.vlasnikKanala.korisnickoIme + " videos";
			document.getElementById('likedVideosH2').innerHTML  = data.vlasnikKanala.korisnickoIme + " like this videos";
			document.getElementById('followingChannelsH2').innerHTML  = data.vlasnikKanala.korisnickoIme + " following";
			document.getElementById('followersChannelsH2').innerHTML  = data.vlasnikKanala.korisnickoIme + " followers";
			document.getElementById('korisnickoIme').innerHTML  = data.vlasnikKanala.korisnickoIme;	
			popuniVideoDiv();
			popuniPratioceDiv();
		}
	});
	
});

function popuniButtonsDiv(){
	if(globalData.ulogovaniKorisnik != null){
		var userButtonsDiv = $('#userButtons');
		userButtonsDiv.empty();
		if(globalData.ulogovaniKorisnik.vrstaKorisnika =='ADMIN' || globalData.ulogovaniKorisnik.id == globalData.vlasnikKanala.id){
			userButtonsDiv.append(
				'<button id="izmeniBtn2" type="button" onclick="editChannel()">Edit</button>'+
				'<button id="obrisiBtn2" type="button" onclick="alertDelete('+globalData.vlasnikKanala.id+')">Delete</button>'
			);
		}
		if(globalData.ulogovaniKorisnik.id != globalData.vlasnikKanala.id && globalData.ulogovaniKorisnik.vrstaKorisnika=="ADMIN"){
			if(globalData.vlasnikKanala.vrstaKorisnika=="USER"){
				userButtonsDiv.append(
						'<button id="btnPromote" onclick="promote('+globalData.vlasnikKanala.id+')">Promote</button>'
				);
			}
			if(globalData.blokiran == true){
				userButtonsDiv.append(
						'<button id="btnBlock" onclick="unBlock('+globalData.vlasnikKanala.id+')">Unblock</button>'
				);
			}else{
				userButtonsDiv.append(
						'<button id="btnBlock" onclick="block('+globalData.vlasnikKanala.id+')">Block</button>'
				);
			}
				
		}	
		
		if(globalData.ulogovaniKorisnik.id != globalData.vlasnikKanala.id){
			if(globalData.ulogovaniKorisnik.blokiran == false){
				if(globalData.provera == true){
					userButtonsDiv.append(
						'<h3>'+globalData.brojPratioca+' followers</h3>'+
						'<button id="btnFollow" type="button" onclick="unfollow('+globalData.vlasnikKanala.id+')">Unfollow</button>'
					);
				}else if(globalData.provera == false){
					userButtonsDiv.append(
						'<h3>'+globalData.brojPratioca+' followers</h3>'+
						'<button id="btnFollow" type="button" onclick="follow('+globalData.vlasnikKanala.id+')">Follow</button>'
					);
				}
			}
		}
	}
}

function popuniVideoDiv(){
	for(it in globalData.listaVidea){
		var videoDiv = $('#videoDiv');
		videoDiv.append(
				'<div class="col-lg-3">'+
                    '<div class="Video">'+
						'<a href="VideoStranica.html?id='+globalData.listaVidea[it].id+'">'+
	                    '<img src="'+globalData.listaVidea[it].slika+'"class="imgVideo">'+
	                    '<div class="caption">'+
		                	'<h3>'+globalData.listaVidea[it].naziv+'</h3>'+
	                    '</div>'+
                        '</a>'+
                        '<a href="KanalStranica.html?channel='+globalData.listaVidea[it].vlasnik.korisnickoIme+'" ><h3>'+globalData.listaVidea[it].vlasnik.korisnickoIme+'</h3></a>'+
                        '<h3>'+globalData.listaVidea[it].opis+'<br>'+globalData.listaVidea[it].brojPregleda+' views<br>'+globalData.listaVidea[it].datumKreiranja+' <br>blocked: '+globalData.listaVidea[it].blokiran+'</h3>'+
                    '</div>'+
                '</div>'
		);
	}
	
	for(it in globalData.lajkovaniVidei){
		var videoDiv = $('#likedVideoDiv');
		videoDiv.append(
				'<div class="col-lg-3">'+
                    '<div class="Video">'+
						'<a href="VideoStranica.html?id='+globalData.lajkovaniVidei[it].id+'">'+
	                    '<img src="'+globalData.lajkovaniVidei[it].slika+'"class="imgVideo">'+
	                    '<div class="caption">'+
		                	'<h3>'+globalData.lajkovaniVidei[it].naziv+'</h3>'+
	                    '</div>'+
                        '</a>'+
                        '<a href="KanalStranica.html?channel='+globalData.lajkovaniVidei[it].vlasnik.korisnickoIme+'" ><h3>'+globalData.lajkovaniVidei[it].vlasnik.korisnickoIme+'</h3></a>'+
                        '<h3>'+globalData.lajkovaniVidei[it].opis+'<br>'+globalData.lajkovaniVidei[it].brojPregleda+' views<br>'+globalData.lajkovaniVidei[it].datumKreiranja+' <br>blocked: '+globalData.lajkovaniVidei[it].blokiran+'</h3>'+
                    '</div>'+
                '</div>'
		);
	}
}

function popuniPratioceDiv(){
	for(it in globalData.vlasnikKanala.kogaPratiKorisnik){
		var followingDiv = $('#followingChannelsDiv');
		followingDiv.append(
				'<div class="col-lg-3">'+
                    '<div class="FollowingContainer">'+
						'<a href="KanalStranica.html?channel='+globalData.vlasnikKanala.kogaPratiKorisnik[it].korisnickoIme+'">'+
		                    '<img src="images/avatar.png" class="avatar">'+
		                    '<div class="cufCaption">'+
			                	'<h3 style="margin-left:20px;">'+globalData.vlasnikKanala.kogaPratiKorisnik[it].korisnickoIme+'</h3>'+
		                    '</div>'+
                        '</a>'+
                    '</div>'+
                '</div>'
		);
	}
		for(it in globalData.vlasnikKanala.koNjegaPrati){
			var followingDiv = $('#followersChannelsDiv');
			followingDiv.append(
					'<div class="col-lg-3">'+
	                    '<div class="FollowersContainer">'+
							'<a href="KanalStranica.html?channel='+globalData.vlasnikKanala.koNjegaPrati[it].korisnickoIme+'">'+
			                    '<img src="images/avatar.png" class="avatar">'+
			                    '<div class="cufCaption">'+
				                	'<h3 style="margin-left:20px;">'+globalData.vlasnikKanala.koNjegaPrati[it].korisnickoIme+'</h3>'+
			                    '</div>'+
	                        '</a>'+
                        '</div>'+
                    '</div>'
			);
	}
}
function popuniInfoDiv(){
	if(globalData.ulogovaniKorisnik != null){
		if(globalData.ulogovaniKorisnik.vrstaKorisnika =='ADMIN' || globalData.ulogovaniKorisnik.id == globalData.vlasnikKanala.id){
			var descriptionDiv = $('#KanalContainerDescription');
			descriptionDiv.empty();
			descriptionDiv.append(
					'<div class="row-lg-2">'+
	               	'<h3>Information about '+globalData.vlasnikKanala.korisnickoIme+' channel</h3>'+
	                '</div>'+
	                '<div class="row-lg-2">'+
	                    '<h3>First name: '+globalData.vlasnikKanala.ime+'</h3>'+
	                '</div>'+
	                '<div class="row-lg-2">'+
	                    '<h3>Last name: '+globalData.vlasnikKanala.prezime+'</h3>'+
	                '</div>'+
	                '<div class="row-lg-2">'+
	                    '<h3>Email: '+globalData.vlasnikKanala.email+'</h3>'+
	                '</div>'+
	                '<div class="row-lg-2">'+
	                    '<h3>Type of user: '+globalData.vlasnikKanala.vrstaKorisnika+'</h3>'+
	                '</div>'+
	                '<div class="row-lg-2">'+
	                    '<h3>Registration date: '+globalData.vlasnikKanala.datum+'</h3>'+
	                '</div>'+
	                '<div class="row-lg-2">'+
	                    '<h3>Channel description: '+globalData.vlasnikKanala.opisKanala+'</h3>'+
	                '</div>'+
	                '<div class="row-lg-2">'+
	               	'<h3>Blocked: '+globalData.vlasnikKanala.blokiran+'</h3>'+
	                '</div>'

				);
		}
		if(globalData.ulogovaniKorisnik.id != globalData.vlasnikKanala.id && globalData.ulogovaniKorisnik.vrstaKorisnika=="USER"){
			if(globalData.blokiran == true){
				alert(data.poruka);
				window.location.href = "http://localhost:8080/Youtube/";
			}
			var descriptionDiv = $('#KanalContainerDescription');
			descriptionDiv.append(
					'<div class="row-lg-2">'+
	               	'<h3>Information about '+globalData.vlasnikKanala.korisnickoIme+' channel</h3>'+
	                '</div>'+
	                '<div class="row-lg-2">'+
	                    '<h3>Type of user: '+globalData.vlasnikKanala.vrstaKorisnika+'</h3>'+
	                '</div>'+
	                '<div class="row-lg-2">'+
	                    '<h3>Registration date: '+globalData.vlasnikKanala.datum+'</h3>'+
	                '</div>'+
	                '<div class="row-lg-2">'+
	                    '<h3>Channel description: '+globalData.vlasnikKanala.opisKanala+'</h3>'+
	                '</div>'
			);
			document.getElementById('userVideosDiv').innerHTML  = globalData.vlasnikKanala.korisnickoIme + " videos";
	}
	}else{
		if(globalData.blokiran == true){
			alert(data.poruka);
			window.location.href = "http://localhost:8080/Youtube/";
		}
		var descriptionDiv = $('#KanalContainerDescription');
		descriptionDiv.append(
				'<div class="row-lg-2">'+
               	'<h3>Information about '+globalData.vlasnikKanala.korisnickoIme+' channel</h3>'+
                '</div>'+
                '<div class="row-lg-2">'+
                    '<h3>Type of user: '+globalData.vlasnikKanala.vrstaKorisnika+'</h3>'+
                '</div>'+
                '<div class="row-lg-2">'+
                    '<h3>Registration date: '+globalData.vlasnikKanala.datum+'</h3>'+
                '</div>'+
                '<div class="row-lg-2">'+
                    '<h3>Channel description: '+globalData.vlasnikKanala.opisKanala+'</h3>'+
                '</div>'
		);
		document.getElementById('userVideosDiv').innerHTML  = globalData.vlasnikKanala.korisnickoIme + " videos";
	}
}

function alertDelete(id) {
    var r = confirm("Are u sure u want to delete this channel?");
    if (r == true) { 	
	
    	$.get('DeleteUserServlet',{'id': id}, function(data) {
    		window.location.href = "http://localhost:8080/Youtube/";	
        }); 	
    } 
}

function alertDeleteVideo(id) {
    var r = confirm("Are u sure u want to delete this video?");
    if (r == true) { 	
	
    	$.get('DeleteVideoServlet',{'id': id}, function(data) {
    		window.location.href = "http://localhost:8080/Youtube/";
        }); 	
    } 
}
function follow(id){
	$.get('FollowServlet',{'followUnfollow':'follow','id': id}, function(data) {
		if(data.status=="success"){
			location.reload();
		}else{
			alert(data.poruka);
		}
    }); 
}
function unfollow(id){
	$.get('FollowServlet',{'followUnfollow':'unfollow','id': id}, function(data) {
		if(data.status=="success"){
			location.reload();
		}else{
			alert(data.poruka);
		}
    }); 
}
function promote(id) {
    var r = confirm("Are u sure u want to promote this user  to admin? ");
    if (r == true) { 	

    	$.post('KanalStranicaServlet',{'korisnikId': id, 'action' : 'promote'}, function(data) {
    		if(data.alreadyAdmin == true) alert("he is already admin");
    		 if(data.status=="fail"){
             	alert(data.prouka);
             }else{
            	 location.reload();
             }
        }); 
    } 
	
}
function block(korisnikId) {
	this.korisnikId= korisnikId;
	
    var r = confirm("Are u sure u want to block this user?");
    if (r == true) { 	

    	$.post('KanalStranicaServlet',{'korisnikId': korisnikId, 'action' : 'block'}, function(data) {
    		 if(data.status=="fail"){
             	alert(data.prouka);
             }else{
             	popuniDiv(data);
             }
        }); 
    } 
}
function unBlock(korisnikId) {
	this.korisnikId= korisnikId;
	
    var r = confirm("Are u sure u want to unblock this user?");
    if (r == true) { 	

    	$.post('KanalStranicaServlet',{'korisnikId': korisnikId, 'action' : 'unblock'}, function(data) {
            if(data.status=="fail"){
            	alert(data.prouka);
            }else{
            	popuniDiv(data);
            }
        }); 
    } 
}
function editChannel(){
	var descriptionDiv = $('#KanalContainerDescription');
	descriptionDiv.empty();
	descriptionDiv.append(
			'<div class="row-lg-2">'+
           	'<h3>Edit Information about '+globalData.vlasnikKanala.korisnickoIme+'</h3>'+
            '</div>'+
            '<div class="row-lg-2">'+
                '<h3>First name: '+'</h3>'+
                '<input class="korisnikInformationInput" style="border:none" id="vlasnikKanalaIme" type="text" value="'+globalData.vlasnikKanala.ime+'">'+
            '</div>'+
            '<div class="row-lg-2">'+
                '<h3>Last name: '+'</h3>'+
                '<input class="korisnikInformationInput" style="border:none" id="vlasnikKanalaPrezime" type="text" value="'+globalData.vlasnikKanala.prezime+'">'+
            '</div>'+
            '<div class="row-lg-2">'+
                '<h3>Password: '+'</h3>'+
                '<input class="korisnikInformationInput" style="border:none" id="vlasnikKanalaLozinka" type="password" value="'+globalData.vlasnikKanala.lozinka+'">'+
            '</div>'+
            '<div class="row-lg-2">'+
                '<h3>Channel description: '+'</h3>'+
                '<input class="korisnikInformationInput" style="border:none" id="vlasnikKanalaOpisKanala" type="text" value="'+globalData.vlasnikKanala.opisKanala+'">'+
            '</div>'+
            '<div class="row-lg-2">'+
            	'<br/><button id="saveEdit" type="button" onclick="saveEdit()">Save</button>'+
            	'<button id="cancelEdit" type="button" onclick="cancelEdit()">Cancel</button>'+
            '</div>'
	);
	document.getElementById('vlasnikKanalaIme').style.backgroundColor = "#9b775d";
	document.getElementById('vlasnikKanalaPrezime').style.backgroundColor = "#9b775d";
	document.getElementById('vlasnikKanalaLozinka').style.backgroundColor = "#9b775d";
	document.getElementById('vlasnikKanalaOpisKanala').style.backgroundColor = "#9b775d";
}
function saveEdit(){
	var ime = $('#vlasnikKanalaIme').val().trim();
	var prezime = $('#vlasnikKanalaPrezime').val().trim();
	var lozinka = $('#vlasnikKanalaLozinka').val().trim();
	var opis = $('#vlasnikKanalaOpisKanala').val().trim();
	
	if(ime == "" || prezime == "" || lozinka == "" || opis == ""){
		alert("All fields must be filled")
		return;
	}
	
	$.post('KanalStranicaServlet',{
		'korisnikId': globalData.vlasnikKanala.id, 
		'action' : 'edit',
		'ime':ime,
		'prezime':prezime,
		'lozinka':lozinka,
		'opis':opis}, 
		function(data) {
		if(data.status == "fail"){
        	alert(data.poruka);
        	
        }else{
        	alert("successfuly edited user");
        	globalData.vlasnikKanala = data.korisnik;
        	popuniInfoDiv();
        	
        }
	});
}
function cancelEdit(){
	popuniInfoDiv();
}
function addNewVideo(){
	window.location.href = "http://localhost:8080/Youtube/AddVideo.html";
}

function search(){
	 var searchInput = $('#txtPretraga');
	 var searchText = searchInput.val().trim();
	 
	 var url = 'http://localhost:8080/Youtube/PretragaStranica.html?search=' + searchText;
	 window.location.href = url;
	 
}
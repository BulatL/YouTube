$(document).ready(function(e){
	
	var url = location.href;
	var splitovanURL = url.split("?");
	var pretraga = splitovanURL[1].split("=")[1];
	
	$.get('PretragaServlet',{"pretraga": pretraga,"prvaPretraga":true},function(data){
		if(data.status == "false"){
			alert("something went wrong");
			window.location.href = "http://localhost:8080/Youtube/";
		}else{
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
			}
			if(data.ulogovaniKorisnik == null){
				var navbarLogin = $('#navbarLogin');
				navbarLogin.append(
					'<li><a href="Registracija.html" ><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>'+
	                '<li><a href="Login.html" ><span class="glyphicon glyphicon-log-in"></span> Login</a></li>'
					);
			}
			
			popuniDiv(data);
		}
	});
});

function popuniDiv(data){
	
	var videoBody = $('#videoBody');
	videoBody.empty();
	
	var commentBody = $('#commentBody');
	commentBody.empty();
			
	var userBody = $('#userBody');
	userBody.empty();
	if(data.listaVidea.length == 0){

		videoBody.append(
				'<h2>No result for search</h2>'
		);
		
	}if(data.listaVidea.length > 0){
		for(it in data.listaVidea){
			videoBody.append(
					 '<div class="col-lg-3">'+
	                     '<div class="Video">'+
	                         '<a href="VideoStranica.html?id='+data.listaVidea[it].id+'" >'+
	                         	'<img src="'+data.listaVidea[it].slika+'" class="imgVideo">'+
	                         	'<div class="caption">'+
	                             	'<h3>'+data.listaVidea[it].naziv+'</h3>'+
	                             '</div>'+
	                         '</a>'+
	                         '<a href="KanalStranica.html?channel='+data.listaVidea[it].vlasnik.korisnickoIme+'" ><h3>'+data.listaVidea[it].vlasnik.korisnickoIme+'</h3></a>'+
	                         '<h4>'+data.listaVidea[it].opis+'<br>'+data.listaVidea[it].brojPregleda+' views<br>'+data.listaVidea[it].datumKreiranja+'</h4> '+
	                     '</div>'+
                     '</div>'
					);
		}
	}
	
	
	if(data.listaKomentara.length == 0){
		commentBody.append(
				'<h2>No result for search</h2>'
		);		
		
	}if(data.listaKomentara.length > 0){
		for(it in data.listaKomentara){
			commentBody.append(
					 '<div class="col-lg-3">'+
	                     '<div class="commentContainer">'+
	                     	'<div class="caption">'+
	                     		'<a href="KanalStranica.html?channel='+data.listaKomentara[it].vlasnik.korisnickoIme+'" >'+
	                     			'<h3>'+data.listaKomentara[it].vlasnik.korisnickoIme+'</h3>'+
	                     		'</a><br>'+
	                     		'<h4>'+data.listaKomentara[it].sadrzaj+'</h4>'+
	                     		'<h4>'+data.listaKomentara[it].brojLajkova+' raiting</h4>'+
	                     		'<h4>The name of the video on which the comment is: '+
	                     			'<a href="VideoStranica.html?id='+data.listaKomentara[it].video.id+'">'+
	                     			data.listaKomentara[it].video.naziv+
	                     			'</a>'+
	                     		'<h4>'+
	                     		'<hr>'+
	                        '</div>'+
	                    '</div>'+
                    '</div>'
					);
		}
	}
	
	if(data.listaKorisnika.length == 0){

		userBody.append(
				'<h2>No result for search</h2>'
		);
		
	}if(data.listaKorisnika.length > 0){
		for(it in data.listaKorisnika){
			userBody.append(
					'<div class="col-lg-3">'+
                    	'<div class="userContainer">'+
							'<a href="KanalStranica.html?channel='+data.listaKorisnika[it].korisnickoIme+'">'+
			                    '<img src="images/avatar.png" class="avatar">'+
			                    '<div class="cufCaption">'+
				                	'<h3 style="margin-left:20px;">'+data.listaKorisnika[it].korisnickoIme+'</h3>'+
			                    '</div>'+
		                    '</a>'+
	                    '</div>'+
                    '</div>'
					);
		}
	}
}

function search(){
	 var searchInput = $('#txtPretraga');
	 var searchText = searchInput.val().trim();
	 var checkedVideo = true;
	 var checkedUser = true;
	 var checkedComment = true;
	 var checkedSortVideo = "";
	 var checkedSortUser  = "";
	 var checkedSortComment   = "";
	 var ascDesc = "";

	 
	 var checkboxVideo = document.getElementById('searchVideo');
	 if(checkboxVideo.checked){
		 checkedVideo = true;
	 }else{
		 checkedVideo = false;
	 }
	 
	 var checkboxUser = document.getElementById('searchUser');
	 if(checkboxUser.checked){
		 checkedUser = true;
	 }else{
		 checkedUser = false;
	 }
	 
	 var checkboxComment = document.getElementById('searchComment');
	 if(checkboxComment.checked){
		 checkedComment = true;
	 }else{
		 checkedComment= false;
	 }
	 
	 checkedSortVideo = $('input[name=orderByVideo]:checked').val();
	 checkedSortUser  = $('input[name=orderByUser]:checked').val();
	 checkedSortComment   = $('input[name=orderByComment]:checked').val();
	 ascDesc = $('input[name=ascDesc]:checked').val();

	$.ajax({
        url: 'PretragaServlet',
        dataType: 'json',
        contentType : "application/json",
        type: 'get',
        data: {
        	'pretraga':searchText,
        	'prvaPretraga': false,
        	'videoPretraga':checkedVideo,
        	'korisnikPretraga':checkedUser,
        	'komentarPretraga':checkedComment,
        	'sortVideo':checkedSortVideo,
        	'sortKorisnik':checkedSortUser,
        	'sortKomentar':checkedSortComment,
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
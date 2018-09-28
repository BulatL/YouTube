$(document).ready(function(e){
	$.get('PocetnaStranicaServlet',{},function(data){
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
		for(it in data.listaPopularnihVidea){
			var videoBody = $('#top5VideoBody');
			videoBody.append(
					 '<div class="col-lg-3">'+
	                     '<div class="Video">'+
	                         '<a href="VideoStranica.html?id='+data.listaPopularnihVidea[it].id+'" >'+
	                         	'<img src="'+data.listaPopularnihVidea[it].slika+'" class="imgVideo">'+
	                         	'<div class="caption">'+
	                             	'<h3>'+data.listaPopularnihVidea[it].naziv+'</h3>'+
	                             '</div>'+
	                         '</a>'+
	                         '<a href="KanalStranica.html?channel='+data.listaPopularnihVidea[it].vlasnik.korisnickoIme+'" ><h3>'+data.listaPopularnihVidea[it].vlasnik.korisnickoIme+'</h3></a>'+
	                         '<h4>'+data.listaPopularnihVidea[it].opis+'<br>'+data.listaPopularnihVidea[it].brojPregleda+' views<br>'+data.listaPopularnihVidea[it].datumKreiranja+'</h4> '+
	                     '</div>'+
                     '</div>'
					);
		}
		for(it in data.listaSvihVidea){
			var allVideosBody = $('#allVideosBody');
			allVideosBody.append(
					 '<div class="col-lg-3">'+
	                     '<div class="Video">'+
	                         '<a href="VideoStranica.html?id='+data.listaSvihVidea[it].id+'" >'+
	                         	'<img src="'+data.listaSvihVidea[it].slika+'" class="imgVideo">'+
	                         	'<div class="caption">'+
	                             	'<h3>'+data.listaSvihVidea[it].naziv+'</h3>'+
	                             '</div>'+
	                         '</a>'+
	                         '<a href="KanalStranica.html?channel='+data.listaSvihVidea[it].vlasnik.korisnickoIme+'" ><h3>'+data.listaSvihVidea[it].vlasnik.korisnickoIme+'</h3></a>'+
	                         '<h4>'+data.listaSvihVidea[it].opis+'<br>'+data.listaSvihVidea[it].brojPregleda+' views<br>'+data.listaSvihVidea[it].datumKreiranja+'</h4> '+
	                     '</div>'+
                     '</div>'
					);
		}
	});
});

function search(){
	 var searchInput = $('#txtPretraga');
	 var searchText = searchInput.val().trim();
	 
	 var url = 'http://localhost:8080/Youtube/PretragaStranica.html?search=' + searchText;
	 window.location.href = url;
	 
}
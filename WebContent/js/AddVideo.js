$(document).ready(function(e){
	$.get('AddVideoServlet',{},function(data){
		if(data.ulogovaniKorisnik == null){
			alert("You need to login first");
			window.location.href = "http://localhost:8080/Youtube/Login.html";
			return;
		}else{
			if(data.ulogovaniKorisnik.blokiran == true){
				alert("You channel is blocked");
				window.location.href = "http://localhost:8080/Youtube/PocetnaStranica.html";
				return;
			}
		}
	});
});

function addVideo(){
	var url = "";
	var naziv = "";
	var opis = "";
	var vidljivost="";
	
	url = $ ('#youtubeUrlInput').val().trim();
	naziv = $ ('#nameInput').val().trim();
	opis = $ ('#descriptionInput').val().trim();
	
	if(url == "" || naziv == "" || opis == ""){
		alert("All fields must be filled.")
		return;
	}
	
	var dozvoliKomentare = false;
	var dozvoliRaiting = false;
	
	var checkboxDozvoliKomentare = document.getElementById('dozvoliKomentare');
	 if(checkboxDozvoliKomentare.checked){
		 dozvoliKomentare = true;
	 }else{
		 dozvoliKomentare = false;
	 }
	 
	 var checkboxDozvoliRaiting = document.getElementById('dozvoliRaiting');
	 if(checkboxDozvoliRaiting.checked){
		 dozvoliRaiting = true;
	 }else{
		 dozvoliRaiting = false;
	 }
	 
	 vidljivost = $('input[name=visibility]:checked').val();
	 
	$.post('AddVideoServlet',{'url':url, 
		'naziv':naziv, 
		'opis':opis, 
		'dozvoliKomentare': dozvoliKomentare,
		'dozvoliRaiting':dozvoliRaiting,
		'vidljivost':vidljivost},function(data){
		if(data.status="success"){
			alert("successfuly added new video");
			window.location.href = "http://localhost:8080/Youtube/KanalStranica.html?channel="+data.ulogovaniKorisnik.korisnickoIme;
		}else{
			alert(data.poruka);
		}
	});
}
function cancel(){
	window.history.back();
}
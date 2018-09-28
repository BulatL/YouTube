var currentVideo;
var globalData;
var komentarOpis;
$(document).ready(function(e){
	var url = location.href;
	var splitovanURL = url.split("?");
	var videoId = splitovanURL[1].split("=")[1];
	
	$.get('VideoStranicaServlet',{"videoId": videoId},function(data){
		if(data.video == null){
			alert("The requested video is not fount");
			window.location.href = "http://localhost:8080/Youtube/";
		}else{
			currentVideo = data.video;
			globalData = data;
			document.title = data.video.naziv;
			popuniVideoInfo(data);
			popuniKomentare(data);
			if(data.ulogovaniKorisnik!=null){
				if(data.video.vidljivost== "PRIVATE" && (data.ulogovaniKorisnik.vrstaKorisnika =="USER" && data.ulogovaniKorisnik.id != data.video.vlasnik.id)){
					alert("This video is private");
					window.location.href = "http://localhost:8080/Youtube/";
				}
				popuniVideoButtonsDiv(data);
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
				if(data.video.blokiran == true || data.video.vlasnik.blokiran == true){
					if(data.ulogovaniKorisnik.vrstaKorisnika =='USER' && data.ulogovaniKorisnik.id != data.video.vlasnik.id){
						alert("The video is blocked");
						window.location.href = "http://localhost:8080/Youtube/";
					}
				}
			}else{
				if(data.video.vidljivost== "PRIVATE"){
					alert("This video is private");
					window.location.href = "http://localhost:8080/Youtube/";
				}
				var navbarLogin = $('#navbarLogin');
				navbarLogin.append(
					'<li><a href="Registracija.html" ><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>'+
	                '<li><a href="Login.html" ><span class="glyphicon glyphicon-log-in"></span> Login</a></li>'
					);
				if(data.video.blokiran == true || data.video.vlasnik.blokiran == true){
					alert("The video is blocked");
					window.location.href = "http://localhost:8080/Youtube/";
				}
			}
		}
	});
});

function popuniVideoButtonsDiv(data){
	if(data.ulogovaniKorisnik.vrstaKorisnika =='ADMIN' || data.ulogovaniKorisnik.id == data.video.vlasnik.id){
		var videoButtonsDiv = $('#videoButtonsDiv');
		videoButtonsDiv.empty();
		videoButtonsDiv.append(
                '<button id="btnCancel2" onclick="alertDeleteVideo('+data.video.id+')">Delete</button>'
		);
		var editVideoButtonsDiv = $('#editVideoButtonsDiv');
		editVideoButtonsDiv.empty();
		editVideoButtonsDiv.append(
				'<button name="post"  id="editInfo" onclick="editInfoVideo()">Edit information</button>'
		);
		if(data.video.blokiran == true){
			videoButtonsDiv.append(
					'<button id="btnCancel2" onclick="unBlockVideo('+data.video.id+')">Unblock</button>'
			);
		}
		
		if(data.video.blokiran == false){
			videoButtonsDiv.append(
					'<button id="btnCancel2" onclick="blockVideo('+data.video.id+')">Block</button>'
			);
		}
	}
}

function popuniVideoInfo(data){
	var currentVideoDiv = $('.currentVideo');
	currentVideoDiv.empty();
	currentVideoDiv.append(
			'<iframe id="videoFrame" width="950" height="550" src="'+data.video.videoUrl+'" frameborder="0" gesture="media" allow="encrypted-media" allowfullscreen></iframe>'+
            '<div class="caption">'+
                '<input class="videoInformationInput" style="border:none"  id="videoNaziv" type="text" name="videoNaziv" value="'+data.video.naziv+'" readonly>'+
            '</div>'+
            '<div class="container">'+
            	'<input class="videoInformationInput" style="border:none"  id="videoBrPregleda" type="text" name="videoNaziv" value="'+data.video.brojPregleda+' views" readonly>'+
                '<div class="floatRight">'+
                     
                '</div>'+	
           '</div>'+
           '<div class="container" id="videoButtonsDiv">'+
           '</div>'+
           '<div class="container" id="videoInformationContainer">'+
           		'<div class="col-lg-8">'+
           			'<div class="container">'+
           				'<p class="viewCV">'+
           					'<a href="KanalStranica.html?channel='+data.video.vlasnik.korisnickoIme+'" style="font-size: 2em">'+data.video.vlasnik.korisnickoIme+'</a><br/>'+
           					'<textarea rows="4" cols="30" class="videoInformationInput" style="border:none"  id="videoOpis" readonly>'+data.video.opis+'</textarea><br/>'+
           					'<input class="videoInformationInput" style="border:none"  id="videoNaziv" type="text" name="videoDate" value="Posted '+data.video.datumKreiranja+'" readonly>'+
           				'</p>'+
           			'</div>'+
           		'</div>'+
                '<div class="col-lg-2" style="float: right">'+
                    '<div class="container" id="editVideoButtonsDiv">'+
                    '</div>'+
                '</div>'+
            '</div>'
	);
	if(data.video.rejting== true){
		var likeDiv = $('.floatRight');
		likeDiv.append(
				'<input class="videoInformationInput" style="border:none"  id="videoBrLajkova" type="text" name="videoNaziv" value="'+data.video.brojLajkova+' raiting" readonly>'
		);
	}
	if(data.ulogovaniKorisnik.blokiran == false){
		var likeDiv = $('.floatRight');
		likeDiv.append(
                '<a onclick="dislikeVideo('+data.video.id+')"><img id="btnUnlike" src="images/red-thumbs-down-md.png" width="50px" height="50px"></a>'+
                '<a onclick="likeVideo('+data.video.id+')"><img id="btnLike" src="images/green-thumbs-up-md.png" width="50px" height="50px"></a>'
		);
	}
}
function editInfoVideo(){
	var editVideoButtonsDiv = $('#editVideoButtonsDiv');
	editVideoButtonsDiv.empty();
	editVideoButtonsDiv.append(
			'<button name="post"  id="editInfo" onclick="saveInfoVideo()">Save</button>'+
			'<button type="reset" name="cancel"  id="btnCancel2" onclick="cancelInfoVideo()">Cancel</button>'
	);
	var viewCV = $('.viewCV');
	viewCV.empty();
	viewCV.append(
			'<a href="KanalStranica.html?channel='+currentVideo.vlasnik.korisnickoIme+'" style="font-size: 2em">'+currentVideo.vlasnik.korisnickoIme+'</a><br/>'+
			'<label style="font-size: 1em">Video name:</label><br/>'+ 
			'<input class="videoInformationInput" style="border:none"  id="videoNazivInfo" type="text" value="'+currentVideo.naziv+'"><br/>'+
			'<label style="font-size: 1em">Video description:</label><br/>'+
			'<textarea rows="4" cols="30" class="videoInformationInput" style="border:none"  id="videoOpisInfo">'+currentVideo.opis+'</textarea><br/>'+
			'<input type="checkbox" value="komentar" id="proveraKomentar"><a style="font-size: 2em">Allow comments</a><br/>'+
			'<input type="checkbox" value="komentar" id="proveraRaiting"><a style="font-size: 2em">Allow raiting</a><br/>'+
			'<input class="videoInformationInput" style="border:none"  id="videoDatum" type="text"  value="Posted '+currentVideo.datumKreiranja+'" readonly>'
	);
	var proveraKomentar = document.getElementById('proveraKomentar');
	var proveraRaiting = document.getElementById('proveraRaiting');
	if(currentVideo.komentar == true){
		proveraKomentar.checked = true;
	}
	if(currentVideo.komentar == false){
		proveraKomentar.checked = false;
	}
	if(currentVideo.rejting == true){
		proveraRaiting.checked = true;
	}
	if(currentVideo.rejting == false){
		proveraRaiting.checked = false;
	}
}

function saveInfoVideo(){
	var dozvoliKomentare = false;
	var dozvoliRejting = false;
	
	
	 var checkboxKomentari = document.getElementById('proveraKomentar');
	 if(checkboxKomentari.checked){
		 dozvoliKomentare = true;
	 }else{
		 dozvoliKomentare = false;
	 }
	 var checkboxRejting = document.getElementById('proveraRaiting');
	 if(checkboxRejting.checked){
		 dozvoliRejting = true;
	 }else{
		 dozvoliRejting = false;
	 }
	 var videoNaziv = document.getElementById('videoNazivInfo').value;
	 var videoOpis = document.getElementById('videoOpisInfo').value;
	 
	 $.post('VideoStranicaServlet',{'videoNaziv':videoNaziv,
     	'videoOpis': videoOpis,
    	'dozvoliKomentare':dozvoliKomentare,
    	'dozvoliRejting':dozvoliRejting,
    	'videoId': currentVideo.id}, function(data) {
			if(data.status == "success"){
				popuniVideoInfo(data);
				popuniKomentare(data);
				document.title = data.video.naziv;
			}else{
				alert(data.poruka);
			}
	    }); 
}

function cancelInfoVideo(){
	var videoInformationContainer = $('#videoInformationContainer');
	videoInformationContainer.empty();
	videoInformationContainer.append(
			'<div class="col-lg-8">'+
   			'<div class="container">'+
   				'<p class="viewCV">'+
   					'<a href="KanalStranica.html?channel='+currentVideo.vlasnik.korisnickoIme+'" style="font-size: 2em">'+currentVideo.vlasnik.korisnickoIme+'</a><br/>'+
   					'<textarea rows="4" cols="30" class="videoInformationInput" style="border:none"  id="videoOpis" readonly>'+currentVideo.opis+'</textarea><br/>'+
   					'<input class="videoInformationInput" style="border:none"  id="videoNaziv" type="text" name="videoDate" value="Posted '+currentVideo.datumKreiranja+'" readonly>'+
   				'</p>'+
   			'</div>'+
   		'</div>'+
        '<div class="col-lg-2" style="float: right">'+
            '<div class="container" id="editVideoButtonsDiv">'+
                '<button name="post"  id="editInfo" onclick="editInfoVideo()">Edit information</button>'+
            '</div>'+
        '</div>'
	);
}


function likeVideo(id){
	if(globalData.ulogovaniKorisnik == null){
		alert("You need to login first");
	}else{
		$.post('LikeVideoServlet',{'id': id, 'action' : 'like'}, function(data) {
	        if(data.status == "fail"){
	        	alert(data.poruka);
	        }else{
	        	alert("lajk success");
	        	currentVideo.brojLajkova++;
	        	document.getElementById("videoBrLajkova").value = currentVideo.brojLajkova + " raiting";
	
	        }
	    });
	}
}
function dislikeVideo(id){
	if(globalData.ulogovaniKorisnik == null){
		alert("You need to login first");
	}else{
		$.post('LikeVideoServlet',{'id': id, 'action' : 'dislike'}, function(data) {
			if(data.status == "fail"){
	        	alert(data.poruka);
	        }else{
	        	alert("dislike success");
	        	var videoBrLajkovaInput = $('#videoBrLajkova').val();
	        	currentVideo.brojLajkova--;
	        	document.getElementById("videoBrLajkova").value = currentVideo.brojLajkova + " raiting";
	        }
	    });
	}
}

function alertDeleteVideo(id) {

    var r = confirm("Are u sure u want to delete this video?");
    if (r == true) { 	

    	$.get('DeleteVideoServlet',{'id': id}, function(data) {
    		if(data.status== "success"){
    			alert("successfully deleted video");
    			window.location.href = "http://localhost:8080/Youtube/";
    		}else{
    			alert(data.poruka);
    		}
        }); 	
    } 
}

function unBlockVideo(id){
	$.post('BlockVideoServlet',{'id': id, 'action' : 'unblock'}, function(data) {
		if(data.status=="success"){
			alert("Successfunly unblocked video");
			currentVideo.blokiran = false;
        	popuniVideoButtonsDiv(data);
		}else alert("something went wrong, try again")
    });
}
function blockVideo(id){
	$.post('BlockVideoServlet',{'id': id, 'action' : 'block'}, function(data) {
		if(data.status=="success"){
			alert("Successfunly blocked video");
			currentVideo.blokiran = true;
        	popuniVideoButtonsDiv(data);
		}else alert("something went wrong, try again")
    });
}

function postComment(){
	if(globalData.ulogovaniKorisnik != null){
		if(globalData.ulogovaniKorisnik.blokiran == false){
			var komentarText = $('#komentarInput').val().trim();
			if(komentarText == ""){
				alert("Comment may not be empty");
				return;
			}
			$.post('EditKomentarServlet',{'komentarSadrzaj':komentarText,'akcija':'add', 'id':currentVideo.id}, function(data) {
				if(data.status == "fail"){
		        	alert(data.poruka);
		        	
		        }else{
		        	globalData.komentari = data.komentari;
		        	popuniKomentare(globalData);
		        	cancelPostComment();
		        	document.getElementById('komentarDiv'+data.lastId).focus();
		        }
			});
		}
		else{
			alert("Your channel is blocked");
		}
	}
	else{
		alert("You need to login first");
		window.location.href = "http://localhost:8080/Youtube/Login.html";
	}
}

function cancelPostComment(){
	document.getElementById('komentarInput').value = "";
}

function popuniKomentare(data){
	var glavniKomentarDiv = $('#glavniKomentarDiv');
	glavniKomentarDiv.empty();
	if(data.video.komentar == true || (data.ulogovaniKorisnik.vrstaKorisnika=="ADMIN" || data.ulogovaniKorisnik.id == data.video.vlasnik.id)){
		for(it in data.komentari){
			glavniKomentarDiv.append(
				'<div class="col-lg-8">'+
	                '<div class="komentarContainer">'+
	                    '<div class="col-log-3" id="komentarDiv'+data.komentari[it].id+'">'+
	                        '<a href="KanalStranica.html?channel='+data.komentari[it].vlasnik.korisnickoIme+'" style="font-size: 2em">'+
	                        data.komentari[it].vlasnik.korisnickoIme+
	                        '</a><br/>'+
	                        '<textarea class="videoInformationInput" rows="4" cols="30" style="border:none"  id="komentarOpis'+data.komentari[it].id+'" readonly>'+data.komentari[it].sadrzaj+'</textarea><br/>'+
	                        '<input class="videoInformationInput" style="border:none"  id="komentarDatum" type="text" value="Posted '+data.komentari[it].datumKreiranja+'" readonly><br/>'+
	                        '<input class="videoInformationInput" style="border:none"  id="komentarRaiting'+data.komentari[it].id+'" type="text" value="Raiting '+data.komentari[it].brojLajkova+'" readonly><br/>'+
	                        '<div class="containerButtonsKomentar">'+
	                           
	                        '</div>'+
	                    '</div>'+
	                    '<div class="col-log-2" id="komentarButtonsDiv'+data.komentari[it].id+'" style="float: right; margin-top: -4em; margin-left: -1em;">'+
	                    '</div>'+
	                '</div>'+
	            '</div>'
			);	
			popuniKomentarButtonsDiv(data.komentari[it]);
		}
		if(data.ulogovaniKorisnik.blokiran == false){
			var containerButtonsKomentar = $('.containerButtonsKomentar');
			containerButtonsKomentar.empty();
			containerButtonsKomentar.append(
					'<a href="javascript:dislikeKomentar('+data.komentari[it].id+')"><img id="btnUnlike" src="images/red-thumbs-down-md.png" width="30px" height="30px"></a>'+
                    '<a href="javascript:likeKomentar('+data.komentari[it].id+')"><img id="btnLike" src="images/green-thumbs-up-md.png" width="30px" height="30px"></a>'
			);
		}
	}else{
		glavniKomentarDiv.append(
				'<div class="col-lg-8">'+
                	'<div class="komentarContainer">'+
                		'<h3>'+globalData.video.vlasnik.korisnickoIme+' doesnt allow comments</h3>'+
                	'</div>'+
	            '</div>'
		);
	}
}

function popuniKomentarButtonsDiv (komentar){
	if(globalData.ulogovaniKorisnik != null && globalData.ulogovaniKorisnik.blokiran== false){
		if(globalData.ulogovaniKorisnik.id == komentar.vlasnik.id || globalData.ulogovaniKorisnik.vrstaKorisnika == "ADMIN"){
			var komenatButtonsDiv = $('#komentarButtonsDiv'+komentar.id);
			komenatButtonsDiv.empty();
			komenatButtonsDiv.append(
					'<button name="post"  id="editComment" onclick="editKomentar('+komentar.id+')">Edit</button>'+
	                '<button id="deleteVideo" onclick="alertDeleteKomentar('+komentar.id+')">Delete</button>'
			);
		}
	}
}

function alertDeleteKomentar(komentarId) {
	
	this.komentarId = komentarId;
    var r = confirm("Are u sure u want to delete this comment?");
    if (r == true) { 	
    	if(globalData.ulogovaniKorisnik == null){
    		alert("You need to login first");
    	}else{
	    	$.get('DeleteKomentarServlet',{'id': komentarId}, function(data) {
	    		if(data.status=="success"){
	    			for(var i = globalData.komentari.length - 1; i >= 0; i--) {
	    			    if(globalData.komentari[i].id == komentarId) {
	    			    	globalData.komentari.splice(i, 1);
	    			    }
	    			}
	    			popuniKomentare(globalData)
	    		}else{
	    			alert("something went wrong, try again");
	    		}
	        }); 	
    	}
    } 
}

function likeKomentar(id){
	if(globalData.ulogovaniKorisnik == null){
		alert("You need to login first");
	}else{
		$.post('LikeKomentarServlet',{'id': id, 'action' : 'like'}, function(data) {
	        if(data.status == "fail"){
	        	alert(data.poruka);
	        }else{
	        	alert("lajk success");
	        	for(it in globalData.komentari){
	        		if(globalData.komentari[it].id == id){
	        			globalData.komentari[it].brojLajkova++;
	    	        	document.getElementById("komentarRaiting"+id).value = globalData.komentari[it].brojLajkova + " raiting";
	    	        	break;
	        		}
	        	}
	        }
	    });
	}
}
function dislikeKomentar(id){
	if(globalData.ulogovaniKorisnik == null){
		alert("You need to login first");
	}else{
		$.post('LikeKomentarServlet',{'id': id, 'action' : 'dislike'}, function(data) {
			if(data.status == "fail"){
	        	alert(data.poruka);
	        }else{
	        	alert("dislike success");
	        	for(it in globalData.komentari){
	        		if(globalData.komentari[it].id == id){
	        			globalData.komentari[it].brojLajkova--;
	    	        	document.getElementById("komentarRaiting"+id).value = globalData.komentari[it].brojLajkova + " raiting";
	    	        	break;
	        		}
	        	}
	        }
	    });
	}
}
function editKomentar(komentarId){

	var komentarSadrzaj = document.getElementById("komentarOpis"+komentarId);
	komentarSadrzaj.readOnly = false;
	komentarSadrzaj.focus();
	komentarOpis = komentarSadrzaj.value;
	var komentarButtonsDiv = $('#komentarButtonsDiv'+komentarId);
	komentarButtonsDiv.empty();
	komentarButtonsDiv.append(
			'<button name="post"  id="editComment" onclick="saveEditKomentar('+komentarId+')">Save</button>'+
            '<button id="deleteVideo" onclick="cancelEditKomentar('+komentarId+')">Cancel</button>'
	);
	
}

function saveEditKomentar(id){
	if(globalData.ulogovaniKorisnik != null){
		var komentarOpis =  document.getElementById("komentarOpis"+id).value;
		$.post('EditKomentarServlet',{'id': id,'komentarSadrzaj':komentarOpis,'akcija':'edit'}, function(data) {
			if(data.status == "fail"){
	        	alert(data.poruka);
	        	
	        }else{
	        	document.getElementById("komentarOpis"+id).readOnly=true;
	        	var komenatButtonsDiv = $('#komentarButtonsDiv'+id);
				komenatButtonsDiv.empty();
				komenatButtonsDiv.append(
						'<button name="post"  id="editComment" onclick="editKomentar('+id+')">Edit</button>'+
		                '<button id="deleteVideo" onclick="alertDeleteKomentar('+id+')">Delete</button>'
				);
	        	
	        }
		});
	}else{
		alert("You need to login first");
		cancelEditKomentar(id);
	}
}

function cancelEditKomentar(id){
	document.getElementById("komentarOpis"+id).readOnly = true;
	var komenatButtonsDiv = $('#komentarButtonsDiv'+id);
	komenatButtonsDiv.empty();
	komenatButtonsDiv.append(
			'<button name="post"  id="editComment" onclick="editKomentar('+id+')">Edit</button>'+
            '<button id="deleteVideo" onclick="alertDeleteKomentar('+id+')">Delete</button>'
	);
	var komentarSadrzaj = document.getElementById("komentarOpis"+id);
	komentarSadrzaj.value = komentarOpis;
}
function search(){
	 var searchInput = $('#txtPretraga');
	 var searchText = searchInput.val().trim();
	 
	 var url = 'http://localhost:8080/Youtube/PretragaStranica.html?search=' + searchText;
	 window.location.href = url;
	 
}

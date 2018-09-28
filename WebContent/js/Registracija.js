$(document).ready(function(){

		e.preventDefault();
		return false;
});
function registracija(){
	var firstNameInput = $('#firstNameInput');
	var lastNameInput = $('#lastNameInput');
	var emailInput = $('#emailInput');
	var channelDescriptionInput = $('#channelDescriptionInput');
	var userNameInput = $('#userNameInput');
	var passwordInput = $('#passwordInput');

	
	var firstName = firstNameInput.val().trim();
	var lastName = lastNameInput.val().trim();
	var email = emailInput.val().trim();
	var channelDescription = channelDescriptionInput.val().trim();
	var userName = userNameInput.val().trim();
	var password = passwordInput.val().trim();
	
	if(firstName=="" || lastName=="" || email=="" || channelDescription=="" || userName=="" || password=="" ){
		alert("All fields must be filled.")
		return;
	}


	$.post('RegistracijaServlet',{'ime': firstName, 'prezime': lastName, 'email': email, 'opis': channelDescription, 'korisnickoIme': userName, 'lozinka': password},function(data){
		if(data.status=="failed"){
			firstNameInput.value = firstName;
			lastNameInput.value = lastName;

			emailInput.value = email;
			channelDescriptionInput.value = channelDescription;

			userNameInput.value = userName;
			passwordInput.value = password;
            alert("Username already exist!");
		}else{
			window.location.href = "http://localhost:8080/Youtube/";
		}
	});
}
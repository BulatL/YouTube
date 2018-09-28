$(document).ready(function(e){
	e.preventDefault();
	return false;
});

function login(){
	var userNameInput = $('#userNameInput');
	var passwordInput = $('#passwordInput');
	
	var userName = userNameInput.val().trim();
	var password = passwordInput.val().trim();

	if(userName=="" || password==""){
		alert("All fields must be filled.")
		return;
	}

	$.post('LoginStranicaServlet',{'userName': userName, 'password': password},function(data){
		if(data.status=="failed"){
			userNameInput.value = userName;
			passwordInput.value = password
            alert("Wrong username or password!");
		}else{
			window.location.href = "http://localhost:8080/Youtube/";
		}
	});
}
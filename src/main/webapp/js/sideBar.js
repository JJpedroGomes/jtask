// Logout button event listener to communicate with logout servlet
document.getElementById("logout_link").addEventListener("click", function(event) {
	event.preventDefault();
	document.getElementById("logout_form").submit();
});
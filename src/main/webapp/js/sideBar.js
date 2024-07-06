// Highlight selected menu button
const menuButtons = document.querySelectorAll(".menu-button");
menuButtons.forEach((btn) => {
	btn.classList.remove("selected");

	const currentPath = window.location.pathname.split('/').pop();
	
	const hrefLink = btn.querySelector("a").href;
	const thisPath = hrefLink.split("/").pop();
	
	if (currentPath === thisPath) {
		btn.classList.add("selected");
	}
});


// Logout button event listener to communicate with logout servlet
document.getElementById("logout_link").addEventListener("click", function(event) {
	event.preventDefault();
	document.getElementById("logout_form").submit();
});
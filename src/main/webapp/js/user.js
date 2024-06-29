const form = document.getElementById("login_form");

form.addEventListener("submit", function(event){
	event.preventDefault();
		
	const data = new URLSearchParams();
	for(const pair of new FormData(form )) {
		data.append(pair[0], pair[1]);
	};
		
	fetch("../user", {
		method: "post",
		body: data
	})
	.then(response => {
		if(response.status === 201) {
			window.location.href = "login.jsp?success=Account created successfully";
		}
	});
});
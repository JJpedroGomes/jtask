window.onload = function() {
            const params = new URLSearchParams(window.location.search);
            const responseDiv = document.getElementById('response_container');
            const responseMessageDiv = document.getElementById('response_message');
            const errorMessage = params.get('error');
            
            if (errorMessage) {
				responseDiv.style.display = 'block';
                responseMessageDiv.textContent = decodeURIComponent(errorMessage);
            } 
        };

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
		} else {
			return response.json().then(error => {
				if (error.message != null) {
					window.location.href = "newAccount.jsp?error=" + error.message;
				} else {
					window.location.href = "newAccount.jsp?error=Unexpected error occured"
				}
			});
		}
	})
	.catch((error) => {
	  console.log(error)
	});
});
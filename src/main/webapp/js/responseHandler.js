const responseDiv = document.getElementById('response_container');
const responseMessageDiv = document.getElementById('response_message');

window.onload = function() {
            const params = new URLSearchParams(window.location.search);           
            const errorMessage = params.get('error');
            const successMessage = params.get('success');
            
            if (errorMessage) {
            	responseDiv.style.display = 'block';
                responseMessageDiv.textContent = decodeURIComponent(errorMessage);
            } else if (successMessage) {
            	responseDiv.style.display = 'block';
            	responseMessageDiv.style.backgroundColor = "green";
            	responseMessageDiv.style.color = "white";
            	responseMessageDiv.textContent = decodeURIComponent(successMessage);
			}
        };
        
function resetResponseContainer() {
	responseDiv.style.display = "none";
	responseMessageDiv.textContent = "";
};
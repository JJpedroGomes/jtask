const responseDiv = document.getElementById('response_container');
const responseMessageDiv = document.getElementById('response_message');

window.onload = function() {
            const params = new URLSearchParams(window.location.search);           
            const errorMessage = params.get('error');
            const successMessage = params.get('success');
            
            if (errorMessage) {
				showMessage("#AA2F33", "#FFC1C3", "block")
                responseMessageDiv.textContent = decodeURIComponent(errorMessage);
            } else if (successMessage) {
				showMessage("green", null, "block");
            	responseMessageDiv.textContent = decodeURIComponent(successMessage);
			}
        };
        
function resetResponseContainer() {
	responseDiv.style.display = "none";
	responseMessageDiv.textContent = "";
};

function showMessage(messageColor, backgroundColor, display) {
	responseDiv.style.display =  display;
	responseMessageDiv.style.background = backgroundColor;
	responseMessageDiv.style.color = messageColor;
}
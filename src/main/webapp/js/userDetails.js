const changePasswordBtn = document.getElementById("change_password_btn");
changePasswordBtn.addEventListener("click", showPasswordFormFields);

let isShowingPasswordFields = false;

function showPasswordFormFields() {
	 const passwordForms = document.querySelectorAll(".form_password");
    
    // Check the current display state of the first form_password element
    isShowingPasswordFields = passwordForms[0].style.display === "flex";
    
    passwordForms.forEach((element) => {
        if (isShowingPasswordFields) {
            element.style.display = "none";
        } else {
            element.style.display = "flex";
        }
    });

    // Toggle the state for the next click
    isShowingPasswordFields = !isShowingPasswordFields;
};

document.getElementById("cancel_button").addEventListener("click", (event) => {
            event.preventDefault();
            document.location.href = "/jtask";
        });

function isFormValid() {
    const name = document.getElementById("name").value;
    const birthDate = document.getElementById("birth_date").value;
    const password1 = document.getElementById("password").value;
    const password2 = document.getElementById("confirm_password").value;
    
    // Validate Name
    if (name === "") {
        responseMessageDiv.textContent = "Fill the Name field";
        return false;
    } else if (name.match(/\d+/) !== null) {
        responseMessageDiv.textContent = "Name field cannot contain numbers";
        return false;
    }
    
    // Validate Password if password fields are being shown
    if (isShowingPasswordFields) {
        if (password1 === "" || password2 === "") {
            responseMessageDiv.textContent = "Fill all password fields";
            return false;
        } else if (password1.length < 8) {
            responseMessageDiv.textContent = "Password length must be at least 8 characters";
            return false;
        } else if (password1.match(/\d+/) === null) {
            responseMessageDiv.textContent = "Password must contain at least one number";
            return false;
        } else if (password1 !== password2) {
            responseMessageDiv.textContent = "Passwords are not the same";
            return false;
        }
    }
    
    // Validate Birth Date
    if (birthDate === "") {
        responseMessageDiv.textContent = "Fill the Birth Date field";
        return false;
    }
    
    return true;
};

const form = document.getElementById("account_details_form");

form.addEventListener("submit", (event) => {
	event.preventDefault();
	resetResponseContainer();
	
	if(isFormValid()) {
		const data = new URLSearchParams();
		for(const pair of new FormData(form)) {
			data.append(pair[0], pair[1]);
		};
		
		fetch("/jtask/user", {
			method: "post",
			body: data
		})
		.then(response => {
			if(response.status === 200) {
				window.location.href = "/user?success=Account updated successfully";
			} else {
				window.location.href = "/user?error=Unexpected error occured"
				responseMessageDiv.style.display = "block";
			}
		});
	} else {
		showMessage('red', null, 'block');
	}
});











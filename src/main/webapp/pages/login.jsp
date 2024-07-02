<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/stylesheet/forms.css">
<title>Login</title>
</head>
<body>
	<div class="major_container">
		<jsp:include page="/pages/banner.jsp" />
		<div class="login_container">
			<div class="login_logo_wrapper">
			</div>
			<div class="login_form_container">
				<form id="login_form" method="POST" action="${pageContext.request.contextPath}/login">
					<div id="response_container" class="response_container">
						<div id="response_message" class="response_message"></div>
					</div>
					<div class="login_form_element">
						<label for="email">Email</label>
						<input type="email" id="email" name="email" size="30" required />
					</div>
					<div class="login_form_element">
						<label for="password">Password</label>
						<input id="password" name="password" type="password" required></input>
					</div>
					<div class="login_form_element_link">
						<a href="recoverPassword.jsp">Forgot password?</a>
					</div>
					<div class="login_form_element">
						<button type="submit" id="login_submit_btn">Sign in</button>
					</div>
					<div class="login_form_element">
						<p>
							Are you new? <a href="newAccount.jsp">Create an Account</a>
						</p>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script>
        // JavaScript to read the URL parameters and display error message
        window.onload = function() {
            const params = new URLSearchParams(window.location.search);
            const responseDiv = document.getElementById('response_container');
            const responseMessageDiv = document.getElementById('response_message');
            
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
    </script>
</body>
</html>
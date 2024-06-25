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
					<div id="error_container" class="error_container">
						<div id="error_message" class="error_message"></div>
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
            const errorMessage = params.get('error');
            if (errorMessage) {
                const errorDiv = document.getElementById('error_container');
                errorDiv.style.display = 'block';
                
                const errorMessageDiv = document.getElementById('error_message');
                errorMessageDiv.textContent = decodeURIComponent(errorMessage);
                
            }
        };
    </script>
</body>
</html>
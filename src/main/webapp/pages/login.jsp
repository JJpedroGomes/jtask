<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/stylesheet/login.css">
<title>Login</title>
</head>
<body>
	<!-- Reference: https://dribbble.com/shots/23730001-Login-page -->
	<div class="major_container">
		<div class="side_banner_container">
			<!-- image on the left side -->
		</div>
		<div class="login_container">
			<div class="login_logo_wrapper">
				<!-- logo above login form -->
			</div>
			<div class="login_form_container">
				<form id="login_form" method="POST">
					<div class="login_form_element">
						<label for="email">Email</label>
						<textarea id="email" name="email"></textarea>
					</div>
					<div class="login_form_element">
						<label for="password">Password</label>
						<textarea id="password" name="password"></textarea>
					</div>
					<div class="login_form_element">
						<a href="#">Forgot password?</a>
					</div>
					<div class="login_form_element">
						<button type="submit" id="login_submit_btn">Sign in</button>
					</div>
					<div class="login_form_element">
						<p>
							Are you new? <a href="#">Create an Account</a>
						</p>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
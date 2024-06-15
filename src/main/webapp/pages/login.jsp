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
			<div class="side_banner_img">
				<img src="${pageContext.request.contextPath}/assets/img/Team-work-bro.svg" alt="People Working">
			</div>
			<div class="side_banner_textcontent">
				<h1>Lorem Ipsum</h1>
				<p>Lorem ipsum is placeholder text commonly used in the graphic, print, and publishing industries
				 for previewing layouts and visual mockups.</p>
			</div>
		</div>
		<div class="login_container">
			<div class="login_logo_wrapper">
				<!-- logo above login form -->
			</div>
			<div class="login_form_container">
				<form id="login_form" method="POST">
					<div class="login_form_element">
						<label for="email">Email</label>
						<textarea id="email" name="email" required></textarea>
					</div>
					<div class="login_form_element">
						<label for="password">Password</label>
						
						<input id="password" name="password" type="password" required></input>
					</div>
					<div class="login_form_element_link">
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
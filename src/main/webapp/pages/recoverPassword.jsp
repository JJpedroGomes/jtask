<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/stylesheet/forms.css">
<title>Recover your password</title>
</head>
<body>
	<!-- Reference: https://dribbble.com/shots/23730001-Login-page -->
	<div class="major_container">
		<jsp:include page="/pages/banner.jsp" />
		<div class="login_container">
			<div class="login_logo_wrapper">
				<img src="${pageContext.request.contextPath}/assets/img/logo.png" alt="" class="logo_img">
			</div>
			<div class="login_form_container">
				<form id="login_form" method="POST">
					<div class="login_form_element">
						<label for="email">Email</label>
						<input type="email" id="email" pattern=".+@example\.com" size="30" required />
					</div>
					<div class="login_form_element">
						<label for="birth_date">Birth Date</label>
						<input id="birth_date" name="birth_date" type="date" required></input>
					</div>
					<div class="login_form_element">
						<button type="submit" id="login_submit_btn">Send new password</button>
					</div>
					<div class="login_form_element">
						<p>
							Nevermind, <a href="login.jsp"> login</a>
						</p>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
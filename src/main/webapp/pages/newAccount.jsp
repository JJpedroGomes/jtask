<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/stylesheet/forms.css">
<script src="${pageContext.request.contextPath}/js/user.js" defer></script>
<title>Create an Account</title>
</head>
<body>
	<div class="major_container">
		<jsp:include page="/pages/banner.jsp" />
		<div class="login_container">
			<div class="login_logo_wrapper">
				<!-- logo above login form -->
			</div>
			<div class="login_form_container">
				<form id="login_form" action="${pageContext.request.contextPath}/user">
					<div id="response_container" class="response_container">
						<div id="response_message" class="response_message"></div>
					</div>
					<input type="hidden" name="action" value="CreateUser">
					<div class="login_form_element">
						<label for="name">Name</label>
						<textarea id="name" name="name" required></textarea>
					</div>
					<div class="login_form_element">
						<label for="email">Email</label>
						<input type="email" id="email" name="email" size="30" required />
					</div>
					<div class="login_form_element">
						<label for="password">Password</label>
						<input id="password" name="password" type="password" required></input>
					</div>
					<div class="login_form_element">
						<label for="birth_date">Birth Date</label>
						<input id="birth_date" name="birthDate" type="date" required></input>
					</div>
					<div class="login_form_element">
						<button type="submit" id="login_submit_btn">Create an Account</button>
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
	<script src="${pageContext.request.contextPath}/js/responseHandler.js"></script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/stylesheet/accountDetails.css">
	<title>Account Details</title>
</head>
<body>
	<%@ include file="commonSideBarMenu.jsp" %>
	<section class="account_details_section">
	<div class="account_details_title">
		<span>Account Details</span>
	</div>
		<div class="account_details_container">
			<div class="account_details">
				<form id="account_details_form">
					<div class="account_form_element">
						<label for="name">Name:</label>
						<input id="name" name="name" required></input>
					</div>
					<div class="account_form_element">
						<label for="birth_date">Birth Date:</label>
						<input id="birth_date" name="birthDate" type="date" required></input>
					</div>
					<div class="account_form_element">
						<label for="password">Password:</label>
						<input id="password" name="password" type="password" required></input>
					</div>
					<!-- Todo: Esse campo tem de ficar bloqueado se a senha nao for preenchida -->
					<div class="account_form_element">
						<label for="password">Confirm Password:</label>
						<input id="password" name="password" type="password" required></input>
					</div>
					<div class="account_form_buttons">
						<div class="account_form_element">
							<button type="submit" id="save_button">Save Changes</button>
						</div>
						<div class="account_form_element">
							<button type="submit" id="cancel_button">Cancel</button>
						</div>
					</div>
				</form>
			</div>
			<div class="account_details_final">
				<div class="final_header">
					<span>Immutable Data</span>
				</div>
				<div class="final_data">
						<span class="#">Email:</span>
        				<span class="#">teste@email.com</span>
				</div>
				<div class="final_data"">
						<span class="#">Creation Date:</span>
        				<span class="#">20/05/24</span>
				</div>
				<div class="final_data"">
						<span class="#">Status:</span>
        				<span class="#">Active</span>
				</div>
			</div>	
		</div>
	</section>
</body>
</html>

<%@ page import="databaseUtility.*,java.sql.*,frontendUtility.*"%>
<%
	String email = request.getParameter("email");
	String password = request.getParameter("password");
	String redirectTo = request.getParameter("redirectTo");
	String emailError = "";
	String passwordError = "";
	String loginError = "";

	if (email == null || password == null) {
		email = "";
		password = "";
	} else {
		emailError = Validate.validateEmail(email);
		passwordError = Validate.validatePassword(password);
		if (emailError.isEmpty() && passwordError.isEmpty()) {
			Connection c = MySQLConn.connect();
			if (c != null) {
				long id = MySQL.selectUser(c, email, password);
				if (id != -1) {
					session.setAttribute(WebPageStructure.USER_ID, id);
					response.sendRedirect("Homepage.jsp");
				} else {
					loginError = "Invalid email or password";
				}
				MySQLConn.close(c);
			}
		}
	
	}
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>FeedReader-Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/project.js"></script>

<link type="text/css" rel="stylesheet"
	href="css/ui-lightness/jquery-ui.css" />
<link type="text/css" rel="stylesheet" href="css/WebPage.css" />
<style type="text/css">
td:first-child {
	text-align: right;
}

td:last-child {
	color: red;
}
</style>

<script type="text/javascript">
	$(function() {
		login = $("#login").button({
			disabled : true
		});
		$("[name='email']").focusout(validateEmail).blur(validateEmail);
		$("[name='password']").focusout(validatePassword).blur(
				validatePassword);
	});
	
	function isAllValid() {
		var valid = true;
		$("input[name]").each(function(i, e) {
			return valid = valid && (!!e.value);
		});
		$("[id$='_error']").each(function(i, e) {

			return valid = valid && (!e.innerHTML);
		});
		login.button({
			disabled : !valid
		});
	}

</script>
</head>
<body>
	<div id="container" class="ui-widget ui-widget-content ui-corner-all">
		<div id="header" class="ui-widget-header ui-corner-all">
			<div id="account"><%=WebPageStructure.getAccountStatusLinks(session
					.getAttribute(WebPageStructure.USER_ID),"Login")%></div>
		</div>
		<div id="content">
			<form>
				<table id="tableLogin" width="100%">
					<tr>
						<td></td>
						<td>Log In</td>
						<td></td>
					</tr>
					<tr>
						<td class="align-right">Email :</td>
						<td><input type="text" name="email" value="<%=email%>" /></td>
						<td id="emailError"><%=emailError%></td>
					</tr>
					<tr>
						<td class="align-right">Password :</td>
						<td><input type="password" name="password" /></td>
						<td id="passwordError"><%=passwordError%></td>
					</tr>
					<tr>
						<td></td>
						<td class="small_text">Not a member? <a href="SignUp.jsp">Sign Up</a>
						</td>
						<td></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="submit" value="Login" id="login" /></td>
						<td><%=loginError%></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div id="footer"><%=WebPageStructure.getFooter()%></div>
</body>
</html>
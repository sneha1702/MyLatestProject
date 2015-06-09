<%@page
	import="frontendUtility.*,java.util.*,java.sql.*,databaseUtility.*"%>
	
<%
	String email = request.getParameter("email");
	String password = request.getParameter("password");
	String rPassword = request.getParameter("rPassword");
	String redirectTo = request.getParameter("redirectTo");
	String username = request.getParameter("username");
	String emailError = "";
	String passwordError = "";
	String passwordMatchError = "";
	String loginError = "";
	if (email == null) {
		email = "";
		password = "";
		rPassword = "";
		username = "";

	} else {
		emailError = Validate.validateEmail(email);
		passwordError = Validate.validatePassword(password);
		passwordMatchError = Validate.validateMatchPassword(password,rPassword);
		if (emailError.isEmpty() && passwordError.isEmpty()) {
			Connection c = MySQLConn.connect();
			if (c != null) {
				long id = MySQL.insertUser(c, email, username,password);
				if (id != -1) {
					session.setAttribute(WebPageStructure.USER_ID, id);
					response.sendRedirect("Homepage.jsp");
				} else {
					emailError = "You are already a registered user.Use Login.";					
				}
				MySQLConn.close(c);
			} else {
				loginError = "Unable to connect to the database. Try again later!";
			}
		}
	}
%>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>FeedReader-SignUp</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/jquery-ui.js"></script>
	<script type="text/javascript" src="js/project.js"></script>
	<link type="text/css" rel="stylesheet" href="css/ui-lightness/jquery-ui.css" />
	<link type="text/css" rel="stylesheet" href="css/WebPage.css" />
	
</head>
<body>
	<div id="container" class="ui-widget ui-widget-content ui-corner-all">
		<div id="header" class="ui-widget-header ui-corner-all">
			<div id="navigation_bar"><%=WebPageStructure.getNavigationBar() %></div>
			<div id="account"><%=WebPageStructure.getAccountStatusLinks(session.getAttribute(WebPageStructure.USER_ID),"SignUp") %></div>
		</div>
		<div id="content">
			<form method="post" action="SignUp.jsp">
				<table id="tableSignup">
					<tr>
						<td>Email:</td>
						<td><input type="text" name="email" value="<%=email%>" /></td>
						<td id="emailError"><%=emailError%></td>
					</tr>
					<tr>
						<td>Username:</td>
						<td><input type="text" name="username" value="<%=username%>" /></td>
						<td id=""></td>
					</tr>
					<tr>
						<td>Password:</td>
						<td><input type="password" name="password" value="<%=password%>"/></td>
						<td id="passwordError"><%=passwordError%></td>
					</tr>
					<tr>
						<td>Retype Password:</td>
						<td><input type="password" name="rPassword" value="<%=rPassword%>"/></td>
						<td id="passwordMatchError"><%=passwordMatchError%></td>
					</tr>
					
					<tr>
						<td></td>
						<td><input type="submit" value="Sign Up" id="signup" /></td>
						<td><%=loginError%></td>
					</tr>
					
				</table>
			</form>
		</div>
	</div>
	<div id="footer"><%=WebPageStructure.getFooter()%></div>
</body>
</html>
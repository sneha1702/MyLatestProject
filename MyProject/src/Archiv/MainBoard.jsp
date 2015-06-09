<%@page
	import="frontendUtility.*,java.util.*,java.sql.*,databaseUtility.*,indexUtility.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home-FeedReader</title>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/project.js"></script>

<link type="text/css" rel="stylesheet"
	href="css/ui-lightness/jquery-ui.css" />
<link type="text/css" rel="stylesheet" href="css/WebPage.css" />

</head>
<body>
	<div id="container" class="ui-widget ui-widget-content ui-corner-all">
		<div id="header" class="ui-widget-header ui-corner-all">
			<div id="navigation_bar"><%=WebPageStructure.getNavigationBar()%></div>
			<div id="account"><%=WebPageStructure.getAccountStatusLinks(
					session.getAttribute(WebPageStructure.USER_ID), "MainBoard")%></div>
		</div>
		<div id="content">
			<table width="100%" id="mainboardTable">
				<tr>
					<td><a href="Vault.jsp">View Vault</a></td>
					<td><a href="SubscriptionList.jsp">Manage Subscription</a></td>
				</tr>
				<tr>
					<form action="SearchServlet" method="get">
						<td>View Latest News</td>
						<td><input type="hidden" name="q" value="*" /> <input
							type="submit" value="Click here!" /></td>
					</form>
				</tr>
				<tr>
					<form action="SearchServlet" method="get">
						<td>Search</td>
						<td><input type="text" value="Type your query" name="q" /> <input
							type="submit" value="Submit" /></td>
					</form>
				</tr>
			</table>

		</div>
		<div id="footer"><%=WebPageStructure.getFooter()%></div>
	</div>
</body>
</html>
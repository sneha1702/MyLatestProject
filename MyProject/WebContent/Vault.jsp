<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page
	import="frontendUtility.*,java.sql.*,indexUtility.IndexDocument,java.util.List,java.util.ArrayList,databaseUtility.*"%>

<%
	// Check if subscription list exists in database for the user
	List<IndexDocument> list = new ArrayList<IndexDocument>();
	if (WebPageStructure.USER_ID != null) {
		Connection c = MySQLConn.connect();
		if (c != null) {
			//if yes, display it and show change subscription button
			list = MySQL.getUserVault(c,
				session.getAttribute(WebPageStructure.USER_ID));

			//list = MySQL.getUserVault(c, 1);

			MySQLConn.close(c);
		}
	}

	//if no, then display the empty subscription form
%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>My Subscription-FeedReader</title>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet"
	href="css/ui-lightness/jquery-ui.css" />
<link type="text/css" rel="stylesheet" href="css/WebPage.css" />

</head>
<body>
	<div id="container" class="ui-widget ui-widget-content ui-corner-all">
		<div id="header" class="ui-widget-header ui-corner-all">
			<div id="navigation_bar"><%=WebPageStructure.getNavigationBar()%></div>
			<div id="account"><%=WebPageStructure.getAccountStatusLinks(
					session.getAttribute(WebPageStructure.USER_ID),
					"Vault")%></div>
		</div>
		<div id="content">
			<form>
				<p align="center">
					<b>My Vault</b>
				</p>
				<div id="vaultList">
					<table width="100%" align="center" id="results">
						<%
							for (int i = 0; i < list.size(); i++) {
								IndexDocument d = (IndexDocument) list.get(i);
						%>
						<tr>
							<td><a href=<%=d.getItemLink()%>><%=d.getItemTitle()%></a><br>
						</tr>
						<%
							}
						%>
					</table>
				</div>
			</form>
		</div>
	</div>
	<div id="footer"><%=WebPageStructure.getFooter()%></div>
</body>
</html>
<%@page
	import="frontendUtility.*,java.util.*,java.sql.*,databaseUtility.*"%>
<%
	String cat = request.getParameter("category");
	String path = request.getParameter("feedPath");
	System.out.println("cat is"+cat);
	System.out.println("path is"+path);
	if (cat == null) {
		cat = "";
		path = "";

	} else {
		
		if (WebPageStructure.USER_ID != null) {
			Connection c = MySQLConn.connect();
			if (c != null){
				MySQL.saveFeedSubscription(c,session.getAttribute(WebPageStructure.USER_ID),cat,path);
				MySQLConn.close(c);
				response.sendRedirect("Homepage.jsp");
			}
		}
	} 
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
					"SubscribeNewFeed")%></div>
		</div>
		<div id="content">
		<form method="post" action="SubscribeNewFeed.jsp">
				<p align="center">
					<b>RSSFeed Subscription</b>
				</p>

				<table align="center" width="100%" id="feedTable">
					<tr>
						<th>Category</th>
						<td><input type="text" name="category" /></td>

					</tr>
					<tr>
						<th>RSSFeed Path</th>
						<td><input type="text" name="feedPath" /></td>
					<tr>
						<td></td>
						<td><input type="submit" value="Subscribe" id="subsFeedSave" /></td>

					</tr>
				</table>

			</form>
		</div>
	</div>
	<div id="footer"><%=WebPageStructure.getFooter()%></div>
</body>
</html>
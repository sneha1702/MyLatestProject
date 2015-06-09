<%@page
	import="indexUtility.*,frontendUtility.*,java.util.List,databaseUtility.*,java.sql.*"%>
<%
	String title = request.getParameter("itemTitle");
	String link = request.getParameter("itemLink");
	System.out.println("title is" + title);
	System.out.println("link is" + link);
	if (title == null) {
		title = "";
		link = "";

	} else {

		if (WebPageStructure.USER_ID != null) {
			Connection c = MySQLConn.connect();
			if (c != null) {
				MySQL.saveItemInUserVault(c,
						session.getAttribute(WebPageStructure.USER_ID),
						title, link);
				MySQLConn.close(c);
				//response.sendRedirect("Homepage.jsp");
			}
		}
	}
%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Homepage</title>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>

<link type="text/css" rel="stylesheet"
	href="css/ui-lightness/jquery-ui.css" />
<link type="text/css" rel="stylesheet" href="css/WebPage.css" />

</head>
<body>
	<div id="header" class="ui-widget-header ui-corner-all">

		<div id="navigation_bar"><%=WebPageStructure.getNavigationBar()%></div>
		<div id="account"><%=WebPageStructure.getAccountStatusLinks(
					session.getAttribute(WebPageStructure.USER_ID), "Homepage")%></div>

	</div>

	<form action="SearchServlet" method="get">
		<table width="100%" id="mainboard">
			<tr>
				<th align="left"><%=WebPageStructure.getLinks(
					session.getAttribute(WebPageStructure.USER_ID), "Homepage")%></th>
				<th align="right"><input type="text" value="Type your query"
					name="q" /> <input type="submit" value="Submit" /></th>
			</tr>
		</table>
	</form>

	<h1>Latest News</h1>

	<%
		XMLSearcher searcher = new XMLSearcher();
		searcher.search("*");
		List<IndexDocument> list = searcher.retrievePageResults(0, 10);
	%>
	<form method="post" action="Homepage.jsp">
		<table id="results">
			<%
				for (int i = 0; i < 10; i++) {
			%>
			<tr>
				<td>
					<%
						IndexDocument d = (IndexDocument) list.get(i);
							out.println("<p><b><a href=\"" + d.getItemLink() + "\">"
									+ d.getItemTitle() + "</a></b></p>" + "<p>"
									+ d.getItemLink() + "</p>" + "<p>" + d.getItemDesc()
									+ "</p>");
					%>
				</td>
				<%
					if (session.getAttribute(WebPageStructure.USER_ID) != null) {
				%>
				<td><input type="hidden" name="itemTitle"
					value="<%=d.getItemTitle()%>" /> <input type="hidden"
					name="itemLink" value="<%=d.getItemLink()%>" /> <input
					type="submit" value="Save in Vault" id="savevault" /></td>
				<%
					}
				%>
			</tr>
			<%
				}
			%>
		</table>
	</form>
	<div id="footer"><%=WebPageStructure.getFooter()%></div>
</body>
</html>

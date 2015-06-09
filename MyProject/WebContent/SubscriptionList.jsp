<%@page
	import="frontendUtility.*,java.util.*,java.sql.*,databaseUtility.*"%>

<%
	// Check if subscription list exists in database for the user
	String checkbox[] = request.getParameterValues("notify");
	int dbGeneral = 0;
	int dbPolitics = 0;
	int dbSports = 0;
	int dbTechnical = 0;
	int dbEntertainment = 0;

	if (WebPageStructure.USER_ID != null) {
		Connection c = MySQLConn.connect();
		if (c != null) {
			int id = MySQL.checkUserSubscription(c,
					session.getAttribute(WebPageStructure.USER_ID));
			if (id != 0) {
				//if yes, display it and show change subscription button
				List<Integer> list = MySQL.getUserSubscriptions(c,
						session.getAttribute(WebPageStructure.USER_ID));
				dbGeneral = list.get(0);
				dbPolitics = list.get(1);
				dbSports = list.get(2);
				dbTechnical = list.get(3);
				dbEntertainment = list.get(4);
			}
			MySQLConn.close(c);
		}
	}

	if (checkbox != null) {
		Set<String> st = new HashSet<String>();
		try {
			for (int i = 0; i < checkbox.length; i++) {
				st.add(checkbox[i]);
			}
			if (WebPageStructure.USER_ID != null) {
				Connection c = MySQLConn.connect();
				if (c != null) {
					MySQL.saveUserSubscription(c, session
							.getAttribute(WebPageStructure.USER_ID), st);
					MySQLConn.close(c);
					response.sendRedirect("Homepage.jsp");
				}
			}
		} catch (Exception e) {
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
					"SubscriptionList")%></div>
		</div>
		<div id="content">
			<form action="SubscriptionList.jsp" method="post">
				<p align="center">
					<b>My Subscription List</b>
				</p>
				<div id="subscriptionList">
					<table align="center" width="50%">
						<tr>
							<th>Category</th>
							<th>Receive Notifications</th>
							<th>Keywords</th>
						</tr>
						<tr>
							<td>
								<div id="catFormat">
									General<br> <br> Politics<br> <br> Sports<br>
									<br> Technical<br> <br> Entertainment
								</div>
							</td>
							<td>

								<div id="notifyFormat">
									<%
										if (dbGeneral == 1) {
									%>
									<input type="checkbox" name="notify" value="general"
										checked="checked" /><br>
									<%
										} else {
									%>
									<input type="checkbox" name="notify" value="general" /><br>
									<%
										}
									%>

									<%
										if (dbPolitics == 1) {
									%>
									<br> <input type="checkbox" name="notify" value="politics"
										checked="checked" /><br>
									<%
										} else {
									%>
									<br> <input type="checkbox" name="notify" value="politics" /><br>
									<%
										}
									%>

									<%
										if (dbSports == 1) {
									%>
									<br> <input type="checkbox" name="notify" value="sports"
										checked="checked" /><br>
									<%
										} else {
									%>
									<br> <input type="checkbox" name="notify" value="sports" /><br>
									<%
										}
									%>

									<%
										if (dbTechnical == 1) {
									%>
									<br> <input type="checkbox" name="notify"
										value="technical" checked="checked" /><br>
									<%
										} else {
									%>
									<br> <input type="checkbox" name="notify"
										value="technical" /><br>
									<%
										}
									%>

									<%
										if (dbEntertainment == 1) {
									%>
									<br> <input type="checkbox" name="notify"
										value="entertainment" checked="checked" /><br>
									<%
										} else {
									%>
									<br> <input type="checkbox" name="notify"
										value="entertainment" />
									<%
										}
									%>

								</div>
							</td>
							<td>
								<div id="inTextBox" class="ui-corner-all">
									<input type="text" id="tbGeneral" /> <br> <br> <input
										type="text" id="tbPolitics" /> <br> <br> <input
										type="text" id="tbSports" /> <br> <br> <input
										type="text" id="tbTechnical" /> <br> <br> <input
										type="text" id="tbEntertainment" />
								</div>
							</td>
						</tr>
						<tr>
							<th></th>
							<th><input type="submit" value="Save" id="subsSave" /></th>
							<th></th>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
	<div id="footer"><%=WebPageStructure.getFooter()%></div>
</body>
</html>
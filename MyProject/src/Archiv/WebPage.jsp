<%@page import="frontendUtility.*" %>
<!DOCTYPE html >
<html>
<head>
<title>Template</title>

<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet"
	href="css/ui-lightness/jquery-ui.css" />
<link type="text/css" rel="stylesheet" href="css/WebPage.css"/>
</head>
<body>
	<div id="container" class="ui-widget ui-widget-content ui-corner-all">
		<div id="header" class="ui-widget-header ui-corner-all">
			<div id="navigation_bar"><%=WebPageStructure.getNavigationBar()%></div>
			<div id="account"><%=WebPageStructure.getAccountStatusLinks(session.getAttribute(WebPageStructure.USER_ID),"")%></div>
		</div>
		<div id="content" class="">Content</div>
	</div>
	<div id="footer"><%=WebPageStructure.getFooter()%></div>
</body>
</html>
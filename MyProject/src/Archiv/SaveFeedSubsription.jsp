<%@page 	
import="frontendUtility.*,java.util.*,java.sql.*,databaseUtility.*"%>
<%
	String cat = request.getParameter("category");
	String path = request.getParameter("feedPath");
	try {
		
		if (WebPageStructure.USER_ID != null) {
			Connection c = MySQLConn.connect();
			if (c != null){
				MySQL.saveFeedSubscription(c,session.getAttribute(WebPageStructure.USER_ID),cat,path);
				MySQLConn.close(c);
				response.sendRedirect("Homepage.jsp");
			}
		}
	} catch (Exception e) {
	}
%>

<%@page 	
import="frontendUtility.*,java.util.*,java.sql.*,databaseUtility.*"%>
<%
	String[] checkbox = request.getParameterValues("notify");
	Set<String> st = new HashSet<String>();
	try {
		for (int i = 0; i < checkbox.length; i++) {
			st.add(checkbox[i]);
			System.out.println("adding "+ checkbox[i]);
		}
		if (WebPageStructure.USER_ID != null) {
			Connection c = MySQLConn.connect();
			if (c != null) {
				MySQL.saveUserSubscription(c,session.getAttribute(WebPageStructure.USER_ID),st);
				MySQLConn.close(c);
				response.sendRedirect("Homepage.jsp");
			}
		}
	} catch (Exception e) {
	}
%>

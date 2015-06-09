package frontendUtility;

import java.sql.Connection;

import databaseUtility.*;

public class WebPageStructure {

	public static final String[] SHOWN = new String[] { "FeedReader" };
	public static final String[] LINK = new String[] { "#" };

	public static String getNavigationBar() {
		String s = "FeedReader";
		/*
		 * for(int i =0; i< SHOWN.length;i++){ s += "<a href=\""+ LINK[i]+"\">"
		 * + SHOWN[i] + "</a> 	"; }
		 */

		return s;
	}

	public static String getAccountStatusLinks(Object userId, String formName) {
		Connection c = MySQLConn.connect();
		int id = 0;
		if (c != null && userId != null) {
			id = MySQL.isAdmin(c, userId);
			MySQLConn.close(c);
		}

		if (userId == null) {
			return "<a href=\"Login.jsp\">Login</a> / <a href =\"SignUp.jsp\">SignUp </a>";
		} else if (id != 0) {
			if (formName.equals("Homepage")) {
				return "<a href=\"SubscribeNewFeed.jsp\">Subscribe RSS Feed</a> / <a href=\"Logout.jsp\">Logout</a>";
			} else {
				return "<a href=\"Logout.jsp\">Logout</a>";
			}
		} else if (formName.equals("SubscriptionList")) {
			return "<a href=\"Homepage.jsp\">Home</a> / <a href=\"Vault.jsp\">View Vault</a> / <a href=\"Logout.jsp\">Logout</a>";
		} else if (formName.equals("SubscribeNewFeed")) {
			return "<a href=\"Homepage.jsp\">Home</a> / <a href=\"Vault.jsp\">View Vault</a> / <a href=\"SubscriptionList.jsp\">Manage  Subscription</a> / <a href=\"Logout.jsp\">Logout</a> ";
		}else if (formName.equals("Vault")) {
			return "<a href=\"Homepage.jsp\">Home</a> / <a href=\"SubscriptionList.jsp\">Manage  Subscription</a> / <a href=\"Logout.jsp\">Logout</a>";
		}else {
			return "<a href=\"Logout.jsp\">Logout</a>";
		}
	}

	public static String getLinks(Object userId, String formName) {
		String link = "";
		if (userId != null) {
			link = "<a href=\"Vault.jsp\">View Vault</a>   				<a href =\"SubscriptionList.jsp\">Manage Subscription</a>";
		}
		return link;
	}

	public static String getFooter() {
		return "@Copyrights: 2015 FeedReader.com";
	}

	public static final String USER_ID = "user_id";
}

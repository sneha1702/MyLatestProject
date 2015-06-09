package databaseUtility;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConn {
	public static Connection connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			return DriverManager.getConnection(
					"jdbc:mysql://localhost/feedreader", "root", "Rockstar@05");
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean close(Connection c) {
		try {
			c.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}

package databaseUtility;

import indexUtility.IndexDocument;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MySQL {

	public static long insertUser(Connection c, String email, String username,
			String password) {
		long id = -1;
		long subu_id = -1;
		try {
			
			java.util.Date today = new java.util.Date();
			java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());
			
			PreparedStatement ps = c
					.prepareStatement(
							"INSERT INTO main_user("
							+ "user_email,"
							+ "user_password,"
							+ "user_username,"
							+ "user_idate) "
							+ "VALUES (?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, email);
			ps.setString(2, password);
			ps.setString(3, username);
			ps.setTimestamp(4, ts);

			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(1);
				
						
			}
			rs.close();
			ps.close();
			
			//create an empty entry for subscription of the user_id			
			PreparedStatement ps_subu = c
					.prepareStatement(
							"INSERT INTO main_user_subscription("
							+ "sub_user_id,"
							+ "sub_idate) "
							+ "VALUES (?,?)",
							Statement.RETURN_GENERATED_KEYS);
			ps_subu.setLong(1, id);
			ps_subu.setTimestamp(2, ts);			
			ps_subu.execute();
			ResultSet rs_subu = ps_subu.getGeneratedKeys();
			if (rs_subu.next()) {
				System.out.println("subu entry created with id: " + subu_id);
				subu_id = rs_subu.getLong(1);
			}
			rs_subu.close();
			ps_subu.close();		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public static long selectUser(Connection c, String email, String password) {

		long id = -1;
		try {
			PreparedStatement ps = c
					.prepareStatement("SELECT user_id FROM main_user WHERE user_email=? AND user_password=?");
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getLong("user_id");
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	public static int isAdmin(Connection c, Object userId) {
		int status = 0;
		try {
			PreparedStatement ps = c
					.prepareStatement("SELECT asub_id "
							+ "FROM main_admin_subscription "
							+ "WHERE asub_user_id=?");
			ps.setLong(1, (long) userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				status = (int) rs.getInt("asub_id");
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	public static int checkUserSubscription(Connection c, Object userId) {
		int status = 0;
		try {
			PreparedStatement ps = c
					.prepareStatement("SELECT user_subscribed FROM main_user WHERE user_id=?");
			ps.setLong(1, (long) userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				status = (int) rs.getInt("user_subscribed");
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	public static List<Integer> getUserSubscriptions(Connection c, Object userId) {
		List<Integer> list = new ArrayList<Integer>();
		try {
			PreparedStatement ps = c
					.prepareStatement("SELECT sub_cat_general,"
											+ "sub_cat_politics,"
											+ "sub_cat_sports,"
											+ "sub_cat_technical,"
											+ "sub_cat_entertainment"
							+ " FROM main_user_subscription WHERE sub_user_id=?");
			ps.setLong(1, (long) userId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				list.add(0, rs.getInt("sub_cat_general"));
				list.add(1, rs.getInt("sub_cat_politics"));
				list.add(2, rs.getInt("sub_cat_sports"));
				list.add(3, rs.getInt("sub_cat_technical"));
				list.add(4, rs.getInt("sub_cat_entertainment"));
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
		}
		return list;
	}

	public static void saveUserSubscription(Connection c, Object userId, Set<String> notifySet) {
		
		System.out.println("in saveUserSub methodfor user "+userId);
		int politics = 0, general = 0, technical = 0, entertainment = 0, sports = 0;
		for (String s:notifySet){
			if(s.equals("politics"))
				politics = 1;
			else if(s.equals("general"))
				general = 1;
			else if(s.equals("technical"))
				technical = 1;
			else if(s.equals("entertainment"))
				entertainment = 1;
			else if(s.equals("sports"))
				sports = 1;
		}
		
		try {
			PreparedStatement ps = c
					.prepareStatement(
							"UPDATE main_user_subscription "
							+ "SET sub_cat_politics=?, "
								+ "sub_cat_general=?, "
								+ "sub_cat_technical=?, "
								+ "sub_cat_entertainment=?, "
								+ "sub_cat_sports=?, "
								+ "sub_idate=? "
							+ "WHERE sub_user_id=?");
							
							
			
			//update table here with the changed value
			java.util.Date today = new java.util.Date();
			java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());
			
			ps.setInt(1, politics);
			ps.setInt(2, general);
			ps.setInt(3, technical);
			ps.setInt(4, entertainment);
			ps.setInt(5, sports);
			ps.setTimestamp(6, ts);
			ps.setLong(7, (long) userId);
			
			ps.executeUpdate();
			ps.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static  List<IndexDocument> getUserVault(Connection c, Object userId) {
		List<IndexDocument> list = new ArrayList<IndexDocument>();
		IndexDocument iDoc = null;
		
		try {
			PreparedStatement ps = c
					.prepareStatement("SELECT item_title,"
											+ "item_link,"
											+ "vault_item_tag"
							+ " FROM main_item,main_user_vault"
							+ " WHERE vault_item_id = item_id"
							+ " AND vault_user_id = ?");
			ps.setLong(1, (long) userId);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
					iDoc = new IndexDocument();
					iDoc.setItemTitle(rs.getString("item_title"));
					iDoc.setItemLink(rs.getString("item_link"));
					iDoc.setItemTag(rs.getString("vault_item_tag"));
					list.add(iDoc);
				}
			
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
public static void saveItemInUserVault(Connection c, Object userId, String itemTitle, String itemLink) {
		try {
			int itemId = 0;
			System.out.println("itemTit"+itemTitle);
			System.out.println("itemLink"+itemLink);
			System.out.println("userId"+userId);
			
			
			//update table here with the changed value
			java.util.Date today = new java.util.Date();
			java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());
			
			PreparedStatement ps_vault = c
					.prepareStatement(
							"INSERT INTO main_item("
									+ "item_title, "
									+ "item_link, "
									+ "item_idate) "
									+ "VALUES (?,?,?)",
									Statement.RETURN_GENERATED_KEYS);
		
			ps_vault.setString(1, itemTitle);
			ps_vault.setString(2, itemLink);
			ps_vault.setTimestamp(3, ts);			
			ps_vault.execute();
			ResultSet rs_vault = ps_vault.getGeneratedKeys();
			if (rs_vault.next()) {
				itemId = rs_vault.getInt(1);
				System.out.println("item entry created with id: " + itemId);
			}
			rs_vault.close();	
			ps_vault.close();
			
			
			PreparedStatement ps = c
					.prepareStatement(
							"INSERT INTO main_user_vault("
									+ "vault_user_id, "
									+ "vault_item_id, "
									+ "vault_idate) "
									+ "VALUES (?,?,?)",
									Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, (long) userId);
			ps.setInt(2, itemId);
			ps.setTimestamp(3, ts);
			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				int vaultId = rs.getInt(1);
				System.out.println("vault entry created with id: " + vaultId);
			}
			rs.close();	
			
		
			ps.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveFeedSubscription(Connection c, Object userId, String category, String feedPath){
		try {
			//update table here with the changed value
			java.util.Date today = new java.util.Date();
			java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());
			
			PreparedStatement ps_usub = c
					.prepareStatement(
							"UPDATE main_user "
							+ "SET user_subscribed= ?, "
								+ "user_idate= ? "
							+ "WHERE user_id= ?");
		
			
			ps_usub.setInt(1, 1);
			ps_usub.setTimestamp(2, ts);
			ps_usub.setLong(3, (long) userId);
			
			ps_usub.executeUpdate();
			ps_usub.close();
			
			
			PreparedStatement ps_asub = c
					.prepareStatement(
							"UPDATE main_admin_subscription "
							+ "SET asub_category= ?, "
								+ "asub_feed_path= ?, "
								+ "asub_idate= ? "
							+ "WHERE asub_user_id= ?");
							
							
			
			
			
			ps_asub.setString(1, category);
			ps_asub.setString(2, feedPath);
			ps_asub.setTimestamp(3, ts);
			ps_asub.setLong(4, (long) userId);
			
			ps_asub.executeUpdate();
			ps_asub.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

package DBProject.Extractor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class DatabaseConnector {

	public static Connection conn = null;
	public static Statement stmt = null;
	public static boolean isTest;

//	public void createDocTable() {
//
//		try {
//			if(isTest) {
//				String sql = "CREATE TABLE IF NOT EXISTS test_doc_table "
//					+ "(doc_id INTEGER, " + "PRIMARY KEY (doc_id))";
//				stmt.executeUpdate(sql);
//			}		
//
//		} catch (SQLException ex) {
//			// handle any errors
//			System.err.println("Error in creating doc table.");
//			System.out.println("SQLException: " + ex.getMessage());
//			System.out.println("SQLState: " + ex.getSQLState());
//			System.out.println("VendorError: " + ex.getErrorCode());
//		}
//	}
	
//	public void truncateDocTable() {
//
//		try {
//			if(!isTest) {
//				String sql = "TRUNCATE TABLE Document";
//				stmt.executeUpdate(sql);
//			}
//			
//
//		} catch (SQLException ex) {
//			// handle any errors
//			System.err.println("Error in truncating document table.");
//			System.out.println("SQLException: " + ex.getMessage());
//			System.out.println("SQLState: " + ex.getSQLState());
//			System.out.println("VendorError: " + ex.getErrorCode());
//		}
//	}

//	public void addToDocTable(int docID, String username, String docName, String perm) {
//		try {
//			if(isTest) {
//				String sql = "INSERT INTO test_doc_table " + "VALUES (" + docID + ")";
//				PreparedStatement ps = conn.prepareStatement(sql);
//				ps.executeUpdate(sql);
//			}
//			else {
//				String sql = "INSERT INTO Document " + "VALUES (" + docID + ", '" + username + 
//						"', '" + docName + "', '" + perm + "')";
//				PreparedStatement ps = conn.prepareStatement(sql);
//				ps.executeUpdate(sql);
//			}
//
//		} catch (SQLException ex) {
//			// handle any errors
//			System.err.println("Error inserting row into doc table.");
//			System.out.println("SQLException: " + ex.getMessage());
//			System.out.println("SQLState: " + ex.getSQLState());
//			System.out.println("VendorError: " + ex.getErrorCode());
//		}
//	}

	public void createNodeTable() {

		try {
			if(isTest) {
				String sql = "CREATE TABLE IF NOT EXISTS test_node_table "
					+ "(node_id INTEGER, " + "k VARCHAR(255), "
					+ "v VARCHAR(255), " + "doc_id INTEGER, "
					+ "PRIMARY KEY (node_id))";

				stmt.executeUpdate(sql);
			}
			else {
				String sql = "CREATE TABLE IF NOT EXISTS node_table "
						+ "(node_id INTEGER, " + "k VARCHAR(255), "
						+ "v VARCHAR(255), " + "doc_id INTEGER, "
						+ "PRIMARY KEY (node_id))";

				stmt.executeUpdate(sql);
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating node table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void addToNodeTable(int nodeID, String key, String value, int docID) {
		try {
			if(key.length() > 255) {
				key = key.substring(0,254);
			}
			if(value.length() > 255) {
				value = value.substring(0,254);
			}
				if(isTest) {
				PreparedStatement ps = conn
					.prepareStatement("INSERT INTO test_node_table "
							+ "VALUES (?, ?, ?, ?)");
				ps.setInt(1, nodeID);
				ps.setString(2, key);
				ps.setString(3, value);
				ps.setInt(4, docID);
				ps.executeUpdate();
				ps.close();
			}
			else {
				PreparedStatement ps = conn
						.prepareStatement("INSERT INTO node_table "
								+ "VALUES (?, ?, ?, ?)");
					ps.setInt(1, nodeID);
					ps.setString(2, key);
					ps.setString(3, value);
					ps.setInt(4, docID);
					ps.executeUpdate();
					ps.close();
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into node table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void addToNodeTable(ArrayList<TreeNode> batch) {
		try {
			if(isTest) {
				conn.setAutoCommit(false);
				String sql = "INSERT INTO test_node_table (node_id, k, v, doc_id) VALUES (?, ?, ?, ?)";
				PreparedStatement ps = conn.prepareStatement(sql);

				for (TreeNode node : batch) {
					if(node.k.length() > 255) {
						node.k = node.k.substring(0,254);
					}
					if(node.v.length() > 255) {
						node.v = node.v.substring(0,254);
					}
					ps.setInt(1, node.nodeID);
					ps.setString(2, node.k);
					ps.setString(3, node.v);
					ps.setInt(4, node.docID);
					ps.addBatch();
				}

				ps.executeBatch();
				conn.commit();
				ps.close();
			}
			else {
				conn.setAutoCommit(false);
				String sql = "INSERT INTO node_table (node_id, k, v, doc_id) VALUES (?, ?, ?, ?)";
				PreparedStatement ps = conn.prepareStatement(sql);

				for (TreeNode node : batch) {
					ps.setInt(1, node.nodeID);
					ps.setString(2, node.k);
					ps.setString(3, node.v);
					ps.setInt(4, node.docID);
					ps.addBatch();
				}

				ps.executeBatch();
				conn.commit();
				ps.close();
			}
		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into node table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void addToNodeTable(TreeNode n) {
		try {
			if(isTest) {
				String sql = "INSERT INTO test_node_table " + "VALUES (" + n.nodeID
					+ ", '" + n.k + "', '" + n.v + "', " + n.docID + ")";
				PreparedStatement ps = conn.prepareStatement(sql);

				ps.executeUpdate(sql);
				ps.close();
			}
			else {
				String sql = "INSERT INTO node_table " + "VALUES (" + n.nodeID
						+ ", '" + n.k + "', '" + n.v + "', " + n.docID + ")";
					PreparedStatement ps = conn.prepareStatement(sql);

					ps.executeUpdate(sql);
					ps.close();
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into node table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void createIITable() {
		try {
			if(isTest) {
				String sql = "CREATE TABLE IF NOT EXISTS test_ii_table "
					+ "(word VARCHAR(255), " + " node_id INTEGER, "
					+ " PRIMARY KEY (word, node_id))";

				stmt.executeUpdate(sql);
			}
			else {
				String sql = "CREATE TABLE IF NOT EXISTS ii_table "
						+ "(word VARCHAR(255), " + " node_id INTEGER, "
						+ " PRIMARY KEY (word, node_id))";

					stmt.executeUpdate(sql);
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating inverted index table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public static void addToIITable(ArrayList<InvertedIndexEntry> batch) {
		try {
			if(isTest) {
				String sql = "INSERT INTO test_ii_table (word, node_id) VALUES (?, ?)";
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sql);

				for (InvertedIndexEntry entry : batch) {
					if(entry.word.length() > 255) {
						entry.word = entry.word.substring(0,254);
					}
					ps.setString(1, entry.word);
					ps.setInt(2, entry.nodeID);
					ps.addBatch();
				}
		

				ps.executeBatch();
				//System.out.println("execute batch");
				conn.commit();
				ps.close();
			}
			else {
				String sql = "INSERT INTO ii_table (word, node_id) VALUES (?, ?)";
				conn.setAutoCommit(false);
				PreparedStatement ps = conn.prepareStatement(sql);

				for (InvertedIndexEntry entry : batch) {
					ps.setString(1, entry.word);
					ps.setInt(2, entry.nodeID);

					ps.addBatch();
				}
		

				ps.executeBatch();
				conn.commit();
				ps.close();
			}
		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into inverted index table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void addToIITable(String word, int nodeID) {
		try {
			if(word.length() > 255) {
				word = word.substring(0,254);
			}
			if(isTest) {
				PreparedStatement ps = conn
					.prepareStatement("INSERT INTO test_ii_table VALUES (?, ?)");
				ps.setString(1, word);
				ps.setInt(2, nodeID);
				ps.executeUpdate();
				ps.close();
			}
			else {
				PreparedStatement ps = conn
						.prepareStatement("INSERT INTO ii_table VALUES (?, ?)");
				ps.setString(1, word);
				ps.setInt(2, nodeID);
				ps.executeUpdate();
				ps.close();
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in adding to inverted index table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			System.out.println("Word: " + word + " nodeID: " + nodeID);
		}
	}

	public void createEdgeTable() {

		try {
			if(isTest) {
				String sql = "CREATE TABLE IF NOT EXISTS test_edge_table "
					+ "(node_1 INTEGER, " + " node_2 INTEGER, "
					+ " link_type CHAR(1)," + " PRIMARY KEY (node_1, node_2))";

				stmt.executeUpdate(sql);
			}
			else {
				String sql = "CREATE TABLE IF NOT EXISTS edge_table "
						+ "(node_1 INTEGER, " + " node_2 INTEGER, "
						+ " link_type CHAR(1)," + " PRIMARY KEY (node_1, node_2))";

					stmt.executeUpdate(sql);
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating edge table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void addToEdgeTable(ArrayList<Edge> batch) {
		try {
			if(isTest) {
//				double time = System.nanoTime();
				String sql = "INSERT INTO test_edge_table (node_1, node_2, link_type) VALUES (?, ?, ?)";
				PreparedStatement ps = conn.prepareStatement(sql);
				conn.setAutoCommit(false);

				for (Edge entry : batch) {
					ps.setInt(1, entry.node_1);
					ps.setInt(2, entry.node_2);
					ps.setString(3, "T");
					ps.addBatch();
				}

				ps.executeBatch();
				conn.commit();
				ps.close();
			}
			else {
				double time = System.nanoTime();
				String sql = "INSERT INTO edge_table (node_1, node_2, link_type) VALUES (?, ?, ?)";
				PreparedStatement ps = conn.prepareStatement(sql);
				conn.setAutoCommit(false);

				for (Edge entry : batch) {
					ps.setInt(1, entry.node_1);
					ps.setInt(2, entry.node_2);
					ps.setString(3, "T");
					ps.addBatch();
				}

				ps.executeBatch();
				conn.commit();
				ps.close();
			}
		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into edge table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void addToEdgeTable(int parentNode, int childNode, char type) {
		try {
			if(isTest) {
				String sql = "SELECT * FROM test_edge_table WHERE node_1=" + parentNode
					+ " AND node_2=" + childNode;
				PreparedStatement ps = conn.prepareStatement(sql);

				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					System.err
						.println("This edge pair already exists in database.");
				} else {
					sql = "INSERT INTO test_edge_table " + "VALUES (" + parentNode
						+ ", " + childNode + ", '" + type + "')";
					ps = conn.prepareStatement(sql);
					ps.executeUpdate(sql);
					ps.close();
				}
				rs.close();
			}
			else {
				String sql = "SELECT * FROM edge_table WHERE node_1=" + parentNode
						+ " AND node_2=" + childNode;
				PreparedStatement ps = conn.prepareStatement(sql);

				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					System.err
						.println("This edge pair already exists in database.");
				} else {
					sql = "INSERT INTO edge_table " + "VALUES (" + parentNode
						+ ", " + childNode + ", '" + type + "')";
					ps = conn.prepareStatement(sql);
					ps.executeUpdate(sql);
					ps.close();
				}
				rs.close();
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into edge table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void addToEdgeTable(int parentNode, int childNode) {
		addToEdgeTable(parentNode, childNode);
	}

	public void addToEdgeTable(Edge e) {
		addToEdgeTable(e.node_1, e.node_2);
	}

	// public int getWordId(String word) {
	// int id = -1;
	// try {
	// String sql = "SELECT word_id FROM word_table WHERE word='" + word
	// + "'";
	// PreparedStatement ps = conn.prepareStatement(sql);
	// ResultSet w = ps.executeQuery();
	// if (w.next()) {
	// id = Integer.parseInt(w.getString("word_id"));
	// }
	// } catch (SQLException ex) {
	// // handle any errors
	// System.err.println("Error finding word id.");
	// System.out.println("SQLException: " + ex.getMessage());
	// System.out.println("SQLState: " + ex.getSQLState());
	// System.out.println("VendorError: " + ex.getErrorCode());
	// }
	// return id;
	// }

	public int getMaxNodeId() {
		int id = -1;
		try {
			if(isTest) {
				String sql = "SELECT MAX(node_id) AS maxId FROM test_node_table";
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					id = rs.getInt("maxId");
				} else {
					id = 0;
				}
				rs.close();
			// System.out.println("id is " + id);
			}
			else {
				String sql = "SELECT MAX(node_id) AS maxId FROM node_table";
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					id = rs.getInt("maxId");
				} else {
					id = 0;
				}
				rs.close();
			// System.out.println("id is " + id);
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error getting max nodeId.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return id;
	}
	
	public int getMaxDocId() {
		int id = -1;
		try {
			if(isTest) {
				String sql = "SELECT MAX(doc_id) AS maxId FROM test_doc_table";
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					id = rs.getInt("maxId");
				} else {
					id = 0;
				}
				rs.close();
			// System.out.println("id is " + id);
			}
			else {
				String sql = "SELECT MAX(doc_id) AS maxId FROM Document";
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					id = rs.getInt("maxId");
				} else {
					id = 0;
				}
				rs.close();
			// System.out.println("id is " + id);
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error getting max nodeId.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return id;
	}

	public int getMaxWordId() {
		int id = -1;
		try {
			if(isTest) {
				String sql = "SELECT MAX(word_id) AS maxId FROM test_word_table";
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					id = rs.getInt("maxId");
				} else {
					id = 0;
				}
				rs.close();
				// System.out.println("id is " + id);
			}
			else {
				String sql = "SELECT MAX(word_id) AS maxId FROM word_table";
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					id = rs.getInt("maxId");
				} else {
					id = 0;
				}
				rs.close();
				// System.out.println("id is " + id);
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error getting max wordId.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return id;
	}

	public void createConnection(boolean b) {
		isTest = b;
		try {

			conn = DriverManager
					.getConnection(
							"jdbc:mysql://datalake.c2lclaii6yaq.us-west-2.rds.amazonaws.com/Datalake?rewriteBatchedStatements=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&serverTimezone=UTC",
							"admin", "testing1234");
			stmt = conn.createStatement();

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error connecting to database.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void printTable(String table) {
		try {
			String sql = "SELECT * FROM " + table;
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			System.out.println("TABLE: " + table + " # of columns: "
					+ columnsNumber);
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					System.out.print(rs.getString(i) + " ");
				}
				System.out.println();
			}
			rs.close();
		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error printing table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public void getTableNames() {
		try {

			DatabaseMetaData dbmd = conn.getMetaData();
			String[] types = { "TABLE" };
			ResultSet rs = dbmd.getTables(null, null, "%", types);
			while (rs.next()) {
				System.out.println(rs.getString("TABLE_NAME"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void deleteTable(String table) {
		try {
			String sql = "DROP TABLE " + table;
			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error deleting " + table + " from database.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void closeConnection() {
		try {
			conn.close();
			stmt.close();

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error closing connection to database.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

}

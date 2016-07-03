package project.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class DocumentJdbcTemplate {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
   
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	
	public void create(String username, String name) {
		String SQL = "insert into Document (username, name) values (?, ?)";
		jdbcTemplateObject.update( SQL, username, name);
		System.out.println("Created Document Record: [Username: " + username + " Name: " + name + " Permission: 'A']");
	}
	
	public Document getDocument(Integer id) {
		String SQL = "select * from Document where id = ?";
		Document doc = jdbcTemplateObject.queryForObject(SQL, new Object[]{id}, new DocumentMapper());
		return doc;
	}
	
	public int createAndReturnKey(String username, String name) {
		final String insertIntoSql = "insert into Document (username, name) values ('" + username + "', '" + name + "')";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplateObject.update(
		  new PreparedStatementCreator() {
		    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		      return connection.prepareStatement(insertIntoSql, Statement.RETURN_GENERATED_KEYS);
		    }
		  }, keyHolder);

		return keyHolder.getKey().intValue();
	}
	
	public Document getMaxDocID() {
		String SQL = "select * from Document where id = (select max(id) from Document)";
		List<Document> doc = jdbcTemplateObject.query(SQL, new DocumentMapper());
		return doc.get(0);
	}
	
	public List<Document> getAllDocs() {
		String SQL = "select * from Document";
		List<Document> docs = jdbcTemplateObject.query(SQL, new DocumentMapper());
		return docs;
	}
	
	public List<Document> getDocsByUser(String username) {
		String SQL = "select * from Document where username = ?";
		List<Document> docs = jdbcTemplateObject.query(SQL, new Object[]{username}, new DocumentMapper());
		return docs;
	}
	
	public void delete(Integer id){
	    String SQL = "delete from Document where id = ?";
	    jdbcTemplateObject.update(SQL, id);
	    System.out.println("Deleted Document with ID: " + id);
	}
	
	public void update(Integer id, String username, String name, Character permission) {
		String SQL = "update Document set username = ? where id = ?";
		jdbcTemplateObject.update(SQL, username, id);
		SQL = "update Document set name = ? where id = ?";
		jdbcTemplateObject.update(SQL, name, id);
		SQL = "update Document set permission = ? where id = ?";
		jdbcTemplateObject.update(SQL, permission, id);
		System.out.println("Updated Document with ID: " + id);
	}
	
	public void updatePermission(Integer id, Character permission) {
		String charString = permission.toString().trim().substring(0, 1);
		String SQL = "update Document set permission = ? where id = ?";
		jdbcTemplateObject.update(SQL, charString, id);
		System.out.println("Set permission on Document with ID: " + id + " to: " + charString);
	}
	
	public List<Document> getDocsByTermAndPerm(String searchTerm, char permLevel) {
		String charString = Character.toString(permLevel).toUpperCase();
		String SQL = 
				"select * from Document " +
				"where permission = ? " +
				"and id in " +
				"( " +
				  "select distinct(doc_id) from node_table " +
				  "where node_id in " +
				  "( " +
				   " select node_id " +
				    "from ii_table " +
				   " where word = ? " +
				  ")  " +
				");";
		List<Document> docs = jdbcTemplateObject.query(SQL, new Object[]{charString, searchTerm}, new DocumentMapper());
		return docs;
	}
	
	public List<Document> getDocsByTermAndUser(String searchTerm, User user) {
		//get documents based on permission level
		String SQL = 
				"select * from Document " +
				"where permission ";
		if (user.getPermChar() == 'E')
			SQL += "in ('A', 'E') ";
		else
			SQL += "= 'A' ";
		SQL += "and id in " +
				"( " +
				  "select distinct(doc_id) from node_table " +
				  "where node_id in " +
				  "( " +
				   " select node_id " +
				    "from ii_table " +
				   " where word = ? " +
				  ")  " +
				");";
		List<Document> permissionDocs = jdbcTemplateObject.query(SQL, new Object[]{searchTerm}, new DocumentMapper());

		//get the user's personal Documents
		SQL = 
				"select * from Document " +
				"where username = ? " +
				"and id in " +
				"( " +
				  "select distinct(doc_id) from node_table " +
				  "where node_id in " +
				  "( " +
				   " select node_id " +
				    "from ii_table " +
				   " where word = ? " +
				  ")  " +
				");";
		List<Document> userDocs = jdbcTemplateObject.query(SQL, new Object[]{user.getUsername(), searchTerm}, new DocumentMapper());
		
		//remove duplicates using doc_id
		HashMap<Integer, Document> idToDoc = new HashMap<Integer, Document>();
		for (Document doc : permissionDocs)
			if (idToDoc.get(doc.getId()) == null)
				idToDoc.put(doc.getId(), doc);
		for (Document doc : userDocs)
			if (idToDoc.get(doc.getId()) == null)
				idToDoc.put(doc.getId(), doc);
		List<Document> cleanedList = new ArrayList<Document>();
		for (Integer key : idToDoc.keySet())
			cleanedList.add(idToDoc.get(key));
		return cleanedList;
	}
}

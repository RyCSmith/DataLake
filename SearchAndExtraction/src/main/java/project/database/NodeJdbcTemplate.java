package project.database;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class NodeJdbcTemplate {
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
   
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	
	public Node getNode(Integer nodeID) {
		try {
			String SQL = "select * from node_table where node_id = ?";
			Node node = jdbcTemplateObject.queryForObject(SQL, new Object[]{nodeID}, new NodeMapper());
			return node;
		}catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<Node> getNodesByDocList(List<Document> docs) {
		String docString = createDocIdString(docs);
		String SQL = 
				"select * from node_table " +
				"where doc_id in " +
				"( " + docString + " )";
		
		List<Node> nodes = jdbcTemplateObject.query(SQL, new NodeMapper());
		return nodes;
	}
	
	private String createDocIdString(List<Document> docs) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < docs.size(); i++) {
			builder.append(Integer.toString(docs.get(i).getId()));
			if (i != docs.size() - 1)
				builder.append(", ");
		}
		return builder.toString();
	}
}

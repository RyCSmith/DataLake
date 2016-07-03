package project.database;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class EdgeJdbcTemplate {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
   
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	
	public List<Edge> getEdgesByNodeList(List<Node> nodes) {
		String nodeString = createNodeIdString(nodes);
		String SQL = 
				"select * from edge_table " +
				"where link_type = 'T' " +
				"and node_1 in " +
				"( " + nodeString + " )";
		List<Edge> edges = jdbcTemplateObject.query(SQL, new EdgeMapper());
		return edges;
	}
	
	public List<Edge> getEdgesByNodeListIncludeLinks(List<Node> nodes) {
		String nodeString = createNodeIdString(nodes);
		String SQL = 
				"select * from edge_table " +
				"where node_1 in " +
				"( " + createNodeIdString(nodes) + " )";
		List<Edge> edges = jdbcTemplateObject.query(SQL, new EdgeMapper());
		return edges;
	}
	
	private String createNodeIdString(List<Node> nodes) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < nodes.size(); i++) {
			builder.append(Integer.toString(nodes.get(i).getId()));
			if (i != nodes.size() - 1)
				builder.append(", ");
		}
		return builder.toString();
	}
}

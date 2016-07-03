package project.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class NodeMapper implements RowMapper<Node> {
	public Node mapRow(ResultSet rs, int rowNum) throws SQLException {
		Node node = new Node();
		node.setId(rs.getInt("node_id"));
		node.setKey(rs.getString("k"));
		node.setValue(rs.getString("v"));
		node.setDocID(rs.getInt("doc_id"));
		return node;
	}	
}

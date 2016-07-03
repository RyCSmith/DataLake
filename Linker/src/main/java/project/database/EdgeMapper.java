package project.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class EdgeMapper implements RowMapper<Edge> {

	public Edge mapRow(ResultSet rs, int rowNum) throws SQLException {
		Edge edge = new Edge();
		edge.setNode1ID(rs.getInt("node_1"));
		edge.setNode2ID(rs.getInt("node_2"));
		edge.setType(rs.getString("link_type"));
		return edge;
	}
}
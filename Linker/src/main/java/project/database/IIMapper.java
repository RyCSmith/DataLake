package project.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class IIMapper implements RowMapper<II> {
	   public II mapRow(ResultSet rs, int rowNum) throws SQLException {
		   II ii = new II();
		   ii.setWord(rs.getString("word"));
		   ii.setNode_id(rs.getInt("node_id"));
		   return ii;
	   }
	}
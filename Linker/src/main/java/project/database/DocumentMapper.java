package project.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DocumentMapper implements RowMapper<Document> {
	   public Document mapRow(ResultSet rs, int rowNum) throws SQLException {
		   Document doc = new Document();
		   doc.setId(rs.getInt("id"));
		   doc.setUsername(rs.getString("username"));
		   doc.setName(rs.getString("name"));
		   doc.setPermissionViaString(rs.getString("permission"));
		   return doc;
	   }
	}

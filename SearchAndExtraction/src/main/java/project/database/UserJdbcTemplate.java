package project.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserJdbcTemplate {
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
   
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}
	
	public void create(String username, String first, String last, String password) {
		String SQL = "insert into User (username, first, last, password) values (?, ?, ?, ?)";
		jdbcTemplateObject.update( SQL, username, first, last, password);
		System.out.println("Created Record: [Username: " + 
				username + " First: " + first + " Last: " + last + " Password: " + password + "]");
	}
	
	public User getUser(String username) {
		try {
			String SQL = "select * from User where username = ?";
			User user = jdbcTemplateObject.queryForObject(SQL, new Object[]{username}, new UserMapper());
			return user;
		}catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<User> getAllUsers() {
		String SQL = "select * from User";
		List<User> users = jdbcTemplateObject.query(SQL, new UserMapper());
		return users;
	}
	
	public void delete(String username){
	    String SQL = "delete from User where username = ?";
	    jdbcTemplateObject.update(SQL, username);
	    System.out.println("Deleted User with Username: " + username);
	}
	
	public void update(String username, String first, String last, String password, String permlevel) {
		String SQL = "update User set first = ? where username = ?";
		jdbcTemplateObject.update(SQL, first, username);
		SQL = "update User set last = ? where id = ?";
		jdbcTemplateObject.update(SQL, last, username);
		SQL = "update User set password = ? where id = ?";
		jdbcTemplateObject.update(SQL, password, username);
		SQL = "update User set permlevel = ? where id = ?";
		jdbcTemplateObject.update(SQL, permlevel, username);
		System.out.println("Updated User with ID: " + username);
	}
	
}

package project.webComponents;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import project.database.User;
import project.database.UserJdbcTemplate;

public class SessionHelperFunctions {
	
	@Autowired
	@Qualifier("userJdbcBean")
	UserJdbcTemplate userJdbc;
	
	boolean isLoggedIn(HttpSession session) {
		Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
		if (isAuthenticated == null) {
			session.setAttribute("isAuthenticated", false);
			return false;
		}
		else {
			if (isAuthenticated)
				return true;
			else 
				return false;
		}
	}
	
	boolean login(HttpSession session, String username, String password) {
		User user = userJdbc.getUser(username);
		if (user.getPassword().equals(password)) {
			session.setAttribute("isAuthenticated", true);
			return true;
		}
		session.setAttribute("isAuthenticated", false);
		return false;
	}
	
	char getCurrentPermLevel(HttpSession session) {
		if (!isLoggedIn(session))
			return 'A';
		User user = userJdbc.getUser((String) session.getAttribute("username"));
		return user.getPermLevel().charAt(0);
	}
	
	User getCurrentUser(HttpSession session) {
		if (isLoggedIn(session))
			return userJdbc.getUser((String) session.getAttribute("username"));
		return null;
	}	
	
}

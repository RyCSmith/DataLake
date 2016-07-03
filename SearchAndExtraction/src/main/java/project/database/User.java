package project.database;

public class User {
	private String username;
	private String firstName;
	private String lastName;
	private String password;
	private String permLevel;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPermLevel() {
		return permLevel;
	}
	public void setPermLevel(String permLevel) {
		this.permLevel = permLevel;
	}
	
	public char getPermChar(){
		return permLevel.charAt(0);
	}
}

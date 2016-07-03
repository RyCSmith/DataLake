package project.database;

public class Document {
	private int id;
	private String username;
	private String name;
	private char permission;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public char getPermission() {
		return permission;
	}
	public void setPermission(char permission) {
		this.permission = permission;
	}
	public void setPermissionViaString(String permission) {
		this.permission = permission.charAt(0);
	}
}

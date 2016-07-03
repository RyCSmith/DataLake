package project.database;

public class Edge {
	private int node1ID;
	private int node2ID;
	private String type;
	
	public int getNode1ID() {
		return node1ID;
	}
	public void setNode1ID(int node1id) {
		node1ID = node1id;
	}
	public int getNode2ID() {
		return node2ID;
	}
	public void setNode2ID(int node2id) {
		node2ID = node2id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}

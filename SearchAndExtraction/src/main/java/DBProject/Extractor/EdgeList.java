package DBProject.Extractor;

import java.util.ArrayList;

public class EdgeList {
	private ArrayList<Edge> entries;
	DatabaseConnector dbc;

	public EdgeList(DatabaseConnector dbc) {
		entries = new ArrayList<Edge>();
		this.dbc = dbc;
	}

	public void addToList(Edge entry) {
		entries.add(entry);
		if (entries.size() > 499) {
			dbc.addToEdgeTable(entries);
			entries = new ArrayList<Edge>();
		}
	}

	public void addRemainingRecords() {
		dbc.addToEdgeTable(entries);
		entries = new ArrayList<Edge>();
	}

	public void addToList(int node1, int node2) {
		this.addToList(new Edge(node1, node2));
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (Edge n : entries){
			sb.append(n.toString() + "\n");
		}
		return sb.toString();
	}

}

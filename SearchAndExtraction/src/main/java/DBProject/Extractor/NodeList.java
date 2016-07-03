package DBProject.Extractor;

import java.util.ArrayList;

public class NodeList {
	private ArrayList<TreeNode> entries;
	DatabaseConnector dbc;

	public NodeList(DatabaseConnector dbc) {
		entries = new ArrayList<TreeNode>();
		this.dbc = dbc;
	}

	public void addToList(TreeNode entry) {
		entries.add(entry);
		if (entries.size() > 499) {
			dbc.addToNodeTable(entries);
			entries = new ArrayList<TreeNode>();
		}
	}

	public void addRemainingRecords() {
		dbc.addToNodeTable(entries);
		entries = new ArrayList<TreeNode>();
	}

	public void addToList(int nodeID, String key, String value, int docID) {
		this.addToList(new TreeNode(nodeID, key, value, docID));
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (TreeNode n : entries){
			sb.append(n.toString() + "\n");
		}
		return sb.toString();
	}

}

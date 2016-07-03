package project.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import project.database.Document;
import project.database.Node;

public class TwsSinglePathDisplay {
	private List<Document> docsInPath;
	private JSONArray nodesJson;
	private JSONArray edgesJson;
	private static int idCounter = 0;
	private int id;
	private String docsString;
	
	public TwsSinglePathDisplay(HashMap<Integer, Document> fullDocsMap, ArrayList<Node> path) {
		idCounter++;
		id = idCounter;
		docsInPath = new ArrayList<Document>();
		setDocs(fullDocsMap, path);
		setDocsString();
		setNodesJson(path);
		setEdgesJson(path);
	}
	
	public void setDocsString() {
		StringBuilder sb = new StringBuilder("Includes Docs: ");
		for (int i = 0; i < docsInPath.size(); i++) {
			sb.append(docsInPath.get(i).getName() + " (" + docsInPath.get(i).getId() + ")");
			if (i < docsInPath.size() - 1)
				sb.append(", ");
		}
		docsString = sb.toString();
	}
	
	public void setDocs(HashMap<Integer, Document> fullDocsMap, ArrayList<Node> path) {
		HashSet<Integer> addedDocIds = new HashSet<Integer>();
		for (Node node : path) {
			if (!addedDocIds.contains(node.getDocID())) {
				docsInPath.add(fullDocsMap.get(node.getDocID()));
				addedDocIds.add(node.getDocID());
			}
		}
	}
	
	public void setNodesJson(ArrayList<Node> path) {
		JSONArray jArray = new JSONArray();
		for (Node node : path) {
			JSONObject nodeJson = new JSONObject();
			nodeJson.put("id", node.getId());
			nodeJson.put("label", node.getKey() + ", " + node.getValue() + " (" + node.getDocID() + ")");
			jArray.add(nodeJson);
		}
		nodesJson = jArray;
	}
	
	public void setEdgesJson(ArrayList<Node> path) {
		JSONArray jArray = new JSONArray();
		for (int i = 0; i < path.size() - 1; i++) {
			JSONObject edgeJson = new JSONObject();
			edgeJson.put("from", path.get(i).getId());
			edgeJson.put("to", path.get(i + 1).getId());
			edgeJson.put("arrows", "to");
			jArray.add(edgeJson);
		}
		edgesJson = jArray;
	}

	public List<Document> getDocsInPath() {
		return docsInPath;
	}

	public void setDocsInPath(List<Document> docsInPath) {
		this.docsInPath = docsInPath;
	}

	public JSONArray getNodesJson() {
		return nodesJson;
	}

	public void setNodesJson(JSONArray nodesJson) {
		this.nodesJson = nodesJson;
	}

	public JSONArray getEdgesJson() {
		return edgesJson;
	}

	public void setEdgesJson(JSONArray edgesJson) {
		this.edgesJson = edgesJson;
	}
	
	public int getId() {
		return id;
	}
	public String getDocsString() {
		return docsString;
	}
}

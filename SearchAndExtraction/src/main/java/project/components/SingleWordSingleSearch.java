package project.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import project.database.*;

public class SingleWordSingleSearch {
	public Document document;
	public HashSet<Node> nodesSet;
	public HashSet<Edge> edgesSet;
	public String searchTerm;
	public ArrayList<Node> pathFromRoot;
	public HashMap<Node, ArrayList<Node>> adjacencyList;
	public JSONArray nodesJson;
	public JSONArray edgesJson;
	private final int MAX_DISPLAY_LENGTH = 25;
	
	public SingleWordSingleSearch() {
		nodesSet = new HashSet<Node>();
		edgesSet = new HashSet<Edge>();
	}
	
	public ArrayList<Node> getPathFromRoot() {
		if (pathFromRoot != null)
			return pathFromRoot;
		Node rootNode = identifyRootNode();
		createAdjacencyMatrix();
		pathFromRoot = bfs(rootNode);
		return pathFromRoot;
	}
	
	public JSONArray getNodesJson() {
		JSONArray jArray = new JSONArray();
		if (pathFromRoot == null)
			return jArray;
		System.out.println();
		List<Node> displayPath;
		if (pathFromRoot.size() > MAX_DISPLAY_LENGTH) {
			displayPath = pathFromRoot.subList(pathFromRoot.size() - MAX_DISPLAY_LENGTH, pathFromRoot.size());
		} else {
			displayPath = pathFromRoot;
		}
		for (Node node : displayPath) {
			JSONObject nodeJson = new JSONObject();
			nodeJson.put("id", node.getId());
			nodeJson.put("label", getNodeDisplayString(node));
			jArray.add(nodeJson);
		}
		nodesJson = jArray;
		return jArray;
	}
	
	private String getNodeDisplayString(Node node) {
		if (node == null)
			return "";
		String nodeString = "";
		boolean insertComma = false;
		if (node.getKey() != null) {
			nodeString += node.getKey();
			insertComma = true;
		}
		if (insertComma)
			nodeString += ", ";
		if (node.getValue() != null)
			nodeString += node.getValue();
		return nodeString;
	}
	
	public JSONArray getEdgesJson() {
		JSONArray jArray = new JSONArray();
		if (pathFromRoot == null)
			return jArray;
		List<Node> displayPath;
		if (pathFromRoot.size() > MAX_DISPLAY_LENGTH) {
			displayPath = pathFromRoot.subList(pathFromRoot.size() - MAX_DISPLAY_LENGTH, pathFromRoot.size());
		} else {
			displayPath = pathFromRoot;
		}
		for (int i = 0; i < displayPath.size() - 1; i++) {
			JSONObject edgeJson = new JSONObject();
			edgeJson.put("from", displayPath.get(i).getId());
			edgeJson.put("to", displayPath.get(i + 1).getId());
			edgeJson.put("arrows", "to");
			jArray.add(edgeJson);
		}
		edgesJson = jArray;
		return jArray;
	}
	
	public ArrayList<Node> bfs(Node root) {
		Queue<ArrayList<Node>> queue = new LinkedList<ArrayList<Node>>();
		ArrayList<Node> startingList = new ArrayList<Node>();
		startingList.add(root);
		queue.add(startingList);
		
		while (!queue.isEmpty()) {
			ArrayList<Node> path = queue.poll();
			Node nextNode = path.get(path.size() - 1);
			if ((nextNode.getKey() != null && nextNode.getKey().toLowerCase().indexOf(searchTerm.toLowerCase()) > -1) ||
					(nextNode.getValue() != null && nextNode.getValue().toLowerCase().indexOf(searchTerm.toLowerCase()) > -1)) {
				return path;
			}
			
			ArrayList<Node> adjacentNodes = adjacencyList.get(nextNode);
			for (Node node : adjacentNodes) {
				ArrayList<Node> newPath = new ArrayList<Node>();
				newPath.addAll(path);
				newPath.add(node);
				queue.add(newPath);
			}
		}
		return null;
	}
	
	public void createAdjacencyMatrix() {
		adjacencyList = new HashMap<Node, ArrayList<Node>>();
		HashMap<Integer, Node> idToNode = getIdToNodeMap();
		for (Node node : nodesSet)
			adjacencyList.put(node, new ArrayList<Node>());
		for (Edge edge : edgesSet) {
			Node parent = idToNode.get(edge.getNode1ID());
			Node child = idToNode.get(edge.getNode2ID());
			adjacencyList.get(parent).add(child);
		}
	}
	
	public HashMap<Integer, Node> getIdToNodeMap() {
		HashMap<Integer, Node> idToNode = new HashMap<Integer, Node>();
		for (Node node : nodesSet)
			idToNode.put(node.getId(), node);
		return idToNode;
	}
	
	public Node identifyRootNode() {
		HashSet<Integer> receivingNodes = new HashSet<Integer>();
		for (Edge edge : edgesSet)
			receivingNodes.add(edge.getNode2ID());
		for (Node node : nodesSet)
			if (!receivingNodes.contains(node.getId()))
				return node;
		throw new IllegalStateException("The document contains cycles.");
	}
	
	
	public void setDocument(Document document) {
		this.document = document;
	}
	public Document getDocument() {
		return document;
	}
	public void addNode(Node node) {
		nodesSet.add(node);
	}
	public void addEdge(Edge edge) {
		edgesSet.add(edge);
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	
	
}

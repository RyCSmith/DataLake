package project.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import project.database.Document;
import project.database.Edge;
import project.database.Node;

public class TwoWordSearch {
	public HashMap<Integer, Document> idToDocs;
	public List<Document> docsList;
	public HashSet<Node> nodesSet;
	public HashSet<Edge> edgesSet;
	public String searchTerm1;
	public String searchTerm2;
	private ArrayList<ArrayList<Node>> shortestPaths;
	public HashMap<Node, ArrayList<Node>> adjacencyList;
	public HashSet<Node> startNodes;
	public HashSet<Node> targetNodes;
	private int MAX_DEPTH =  25;
	
	
	public TwoWordSearch() {
		idToDocs = new HashMap<Integer, Document>();
		docsList = new ArrayList<Document>();
		nodesSet = new HashSet<Node>();
		edgesSet = new HashSet<Edge>();
		shortestPaths = new ArrayList<ArrayList<Node>>();
	}
	
	public List<TwsSinglePathDisplay> compute() {
		createAdjacencyMatrix();
		identifyStartAndTargetNodes();
		for (Node node : startNodes) {
			bfs(node);
		}
		return convertToResultsDisplay();
	}
	
	public List<TwsSinglePathDisplay> convertToResultsDisplay() {
		List<TwsSinglePathDisplay> displayPaths = new ArrayList<TwsSinglePathDisplay>();
		for (ArrayList<Node> path : shortestPaths) {
			TwsSinglePathDisplay twsSpd = new TwsSinglePathDisplay(idToDocs, path);
			displayPaths.add(twsSpd);
		}
		return displayPaths;
	}
	
	public void bfs(Node start) {
		long startTime = System.currentTimeMillis();
		HashSet<Node> existingPairings = new HashSet<Node>();
		boolean depthExceeded = false;
		int counter = 0;
		
		//continue until we have found a path from start to every target node or until we have exceeded the MAX_DEPTH
		while (existingPairings.size() < targetNodes.size() && !depthExceeded && counter < 25000) {
			HashSet<Node> visitedNodes = new HashSet<Node>();
			//prep queue with start node
			Queue<ArrayList<Node>> queue = new LinkedList<ArrayList<Node>>();
			ArrayList<Node> startingList = new ArrayList<Node>();
			startingList.add(start);
			queue.add(startingList);
			
			while (!queue.isEmpty() && counter < 25000) {
				counter++;
				
				//get next path from queue (initially just the start node)
				//if it ends at a target we haven't seen yet, end this bfs and continue to next loop
				ArrayList<Node> path = queue.poll();
				Node nextNode = path.get(path.size() - 1);
				visitedNodes.add(nextNode);
				if (targetNodes.contains(nextNode) && !existingPairings.contains(nextNode)) {
					existingPairings.add(nextNode);
					shortestPaths.add(path);
					break;
				}
				
				//if we are not yet at max depth: 
				//get all adjacent nodes for the next node in the path create new paths for each 
				if (path.size() < MAX_DEPTH) {
					ArrayList<Node> adjacentNodes = adjacencyList.get(nextNode);
					for (Node node : adjacentNodes) {
						//don't make paths back to nodes we have already visited (cycles)
						if (!visitedNodes.contains(node)) {
							ArrayList<Node> newPath = new ArrayList<Node>();
							newPath.addAll(path);
							newPath.add(node);
							queue.add(newPath);
						}
					}
				} else {depthExceeded = true;}
			}
		}
		
		System.out.println("TOTAL RUN TIME: " + (System.currentTimeMillis() - startTime));
	}
	
	public void identifyStartAndTargetNodes() {
		startNodes = new HashSet<Node>();
		targetNodes = new HashSet<Node>();
		for (Node node : adjacencyList.keySet()) {
			if ((node.getKey() != null && node.getKey().toLowerCase().indexOf(searchTerm1.toLowerCase()) > -1) ||
					(node.getValue() != null && node.getValue().toLowerCase().indexOf(searchTerm1.toLowerCase()) > -1))
				startNodes.add(node);
			else if ((node.getKey() != null && node.getKey().toLowerCase().indexOf(searchTerm2.toLowerCase()) > -1) ||
					(node.getValue() != null && node.getValue().toLowerCase().indexOf(searchTerm2.toLowerCase()) > -1))
				targetNodes.add(node);
		}
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
			adjacencyList.get(child).add(parent);
		}
	}
	
	public HashMap<Integer, Node> getIdToNodeMap() {
		HashMap<Integer, Node> idToNode = new HashMap<Integer, Node>();
		for (Node node : nodesSet)
			idToNode.put(node.getId(), node);
		return idToNode;
	}
	
	public void addDoc(Document doc) {
		if (idToDocs.get(doc.getId()) == null) {
			idToDocs.put(doc.getId(), doc);
			docsList.add(doc);
		}
	}
	
	public void addNodes(List<Node> nodes) {
		nodesSet.addAll(nodes);
	}
	
	public void addEdges(List<Edge> edges) {
		edgesSet.addAll(edges);
	}


	public String getSearchTerm1() {
		return searchTerm1;
	}

	public void setSearchTerm1(String searchTerm1) {
		this.searchTerm1 = searchTerm1;
	}

	public String getSearchTerm2() {
		return searchTerm2;
	}

	public void setSearchTerm2(String searchTerm2) {
		this.searchTerm2 = searchTerm2;
	}

	public List<Document> getDocList() {
		return docsList;
	}

	public ArrayList<ArrayList<Node>> getShortestPaths() {
		return shortestPaths;
	}
	//used only for testing
	public void setMaxPathDepth(int depth) {
		MAX_DEPTH = depth;
	}
	
	
	
}

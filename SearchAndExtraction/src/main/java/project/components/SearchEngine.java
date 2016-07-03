package project.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import project.database.Document;
import project.database.DocumentJdbcTemplate;
import project.database.Edge;
import project.database.EdgeJdbcTemplate;
import project.database.Node;
import project.database.NodeJdbcTemplate;
import project.database.User;
import project.database.UserJdbcTemplate;

@Service
public class SearchEngine {
	
	@Autowired
	@Qualifier("docJdbcBean")
	DocumentJdbcTemplate docJdbc;
	
	@Autowired
	@Qualifier("nodeJdbcBean")
	NodeJdbcTemplate nodeJdbc;
	
	@Autowired
	@Qualifier("edgeJdbcBean")
	EdgeJdbcTemplate edgeJdbc;
	
//	@Cacheable("testCache")
	public List<SingleWordSingleSearch> singleWordSearch(String searchTerm, User currentUser) throws InterruptedException {
		List<SingleWordSingleSearch> results = getNodesAndPrepareClasses(searchTerm, currentUser);
		for (SingleWordSingleSearch singleResult : results) {
			singleResult.getPathFromRoot();
			singleResult.getNodesJson();
			singleResult.getEdgesJson();
		}
		return results;
	}
	
	public List<TwsSinglePathDisplay> twoWordSearch(String word1, String word2, User currentUser) {
		TwoWordSearch tws = getNodesAndPrepareClassesTwoWord(word1, word2, currentUser);
		return tws.compute();
	}
	
	public TwoWordSearch getNodesAndPrepareClassesTwoWord(String word1, String word2, User currentUser) {
		TwoWordSearch tws = new TwoWordSearch();
		tws.setSearchTerm1(word1);
		tws.setSearchTerm2(word2);
		List<Document> docsWord1;
		List<Document> docsWord2;
		if (currentUser == null) {
			docsWord1 = docJdbc.getDocsByTermAndPerm(word1, 'A');
			docsWord2 = docJdbc.getDocsByTermAndPerm(word2, 'A');
		}
		else {
			docsWord1 = docJdbc.getDocsByTermAndUser(word1, currentUser);
			docsWord2 = docJdbc.getDocsByTermAndUser(word2, currentUser);
		}
		if (docsWord1.isEmpty() && docsWord2.isEmpty())
			return tws;
		for (Document doc : docsWord1)
			tws.addDoc(doc);
		for (Document doc : docsWord2)
			tws.addDoc(doc);
		List<Node> nodes = nodeJdbc.getNodesByDocList(tws.getDocList());
		List<Edge> edges = edgeJdbc.getEdgesByNodeListIncludeLinks(nodes);
		tws.addNodes(nodes);
		tws.addEdges(edges);
		return tws;
	}
	
	public List<SingleWordSingleSearch> getNodesAndPrepareClasses(String word, User currentUser) {
		List<SingleWordSingleSearch> swssList = new ArrayList<SingleWordSingleSearch>();
		List<Document> docs;
		if (currentUser == null)
			docs = docJdbc.getDocsByTermAndPerm(word, 'A');
		else
			docs = docJdbc.getDocsByTermAndUser(word, currentUser);
		if (docs.isEmpty())
			return swssList;
		List<Node> nodes = nodeJdbc.getNodesByDocList(docs);
		List<Edge> edges = edgeJdbc.getEdgesByNodeList(nodes);
		
		for (Document doc : docs) {
			SingleWordSingleSearch swss = new SingleWordSingleSearch();
			swss.setDocument(doc);
			swss.setSearchTerm(word);
			HashSet<Integer> nodeIDSet = new HashSet<Integer>();
			//add nodes
			Iterator<Node> nodesIter = nodes.iterator();
			while(nodesIter.hasNext()) {
				Node node = nodesIter.next();
				if (node.getDocID() == doc.getId()) {
					swss.addNode(node);
					nodeIDSet.add(node.getId());
					nodesIter.remove();
				}
			}
			//add edges
			Iterator<Edge> edgeIter = edges.iterator();
			while (edgeIter.hasNext()) {
				Edge edge = edgeIter.next();
				if (nodeIDSet.contains(edge.getNode1ID())) {
					swss.addEdge(edge);
					edgeIter.remove();
				}
			}
			swssList.add(swss);
		}
		return swssList;
	}
	
	//setters used during testing
	public void setDocJdbc(DocumentJdbcTemplate docJdbc) {
		this.docJdbc = docJdbc;
	}

	public void setNodeJdbc(NodeJdbcTemplate nodeJdbc) {
		this.nodeJdbc = nodeJdbc;
	}

	public void setEdgeJdbc(EdgeJdbcTemplate edgeJdbc) {
		this.edgeJdbc = edgeJdbc;
	}


}

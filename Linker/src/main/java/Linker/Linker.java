package Linker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import project.database.DocumentJdbcTemplate;
import project.database.EdgeJdbcTemplate;
import project.database.Node;
import project.database.NodeJdbcTemplate;
import project.database.UserJdbcTemplate;


public class Linker {
	
    public static void main( String[] args ) {
    	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
    	DocumentJdbcTemplate docJdbc = (DocumentJdbcTemplate) context.getBean("docJdbcBean");
    	UserJdbcTemplate userJdbc = (UserJdbcTemplate) context.getBean("userJdbcBean");
    	NodeJdbcTemplate nodeJdbc = (NodeJdbcTemplate) context.getBean("nodeJdbcBean");
    	EdgeJdbcTemplate edgeJdbc = (EdgeJdbcTemplate) context.getBean("edgeJdbcBean");
    	System.out.println("working");
    	
    	List<String> wordList = userJdbc.getAllII();
    	System.out.println(wordList.size());
    	HashMap<String, ArrayList<Node>> wordLists = new HashMap<String, ArrayList<Node>>();
    	
    	for (String word : wordList) {
    		List<Node> nodesWithWord = nodeJdbc.getNodesByWord(word);
    		System.out.println(nodesWithWord.size() + " " + word);
    		
    		HashMap<Integer, HashSet<Integer>> seen = new HashMap<Integer, HashSet<Integer>>();
    		
    		for (Node node : nodesWithWord) {
    			for (Node node2 : nodesWithWord) {
	    			if (node.getDocID() != node2.getDocID()) {
	    				if (seen.get(node.getId()) == null)
	    					seen.put(node.getId(), new HashSet<Integer>());
	    				if (seen.get(node2.getId()) == null)
	    					seen.put(node2.getId(), new HashSet<Integer>());
	    				
	    				if (seen.get(node.getId()).contains(node2.getId()))
	    					continue;
	    				if (seen.get(node2.getId()).contains(node.getId()))
	    					continue;
	    				
	    				edgeJdbc.insertEdge(node.getId(), node2.getId(), "L");
	    				seen.get(node.getId()).add(node2.getId());
	    				seen.get(node2.getId()).add(node.getId());
	    			}
    			}
    		}
    	}
    	
    	
    }
}

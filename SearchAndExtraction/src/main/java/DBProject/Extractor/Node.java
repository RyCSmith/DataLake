package DBProject.Extractor;

import java.util.Set;

public class Node {
	
	public int node_id;
	public String k;
	public String v;
	public int doc_id;
	
	public Node(int node_id, String k, String v, int doc_id) {
		this.node_id = node_id;
		this.k = k.toLowerCase();
		this.v = v.toLowerCase();
		this.doc_id = doc_id;
	}

}

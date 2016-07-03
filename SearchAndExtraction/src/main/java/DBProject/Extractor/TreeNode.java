package DBProject.Extractor;

public class TreeNode {
	
	int nodeID;
	String k;
	String v;
	int docID;
	
	public TreeNode(int nodeID, String key, String value, int docID){
		this.nodeID = nodeID;
		this.k = key.toLowerCase();
		this.v = value.toLowerCase();
		this.docID = docID;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("id: " + nodeID + " , key: " + k + ", value: " + v);
		return sb.toString();
		
	}

}

package DBProject.Extractor;

public class Edge {
	
	public int node_1;
	public int node_2;

	public Edge(int node_1, int node_2) {
		this.node_1 = node_1;
		this.node_2 = node_2;
	}
	
	@Override
	public String toString(){
		return node_1 + "->" + node_2;
	}

}
package DBProject.Extractor;

public class InvertedIndexEntry {
	 String word;
	 int nodeID;

	public InvertedIndexEntry(String w, int n) {
		word = w;
		nodeID = n;
	}
	
	@Override
	public String toString(){

		return "word: " + word + ", id: " + nodeID;
	}

}

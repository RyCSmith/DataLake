package DBProject.Extractor;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;

public class DataParser {
	
	static DatabaseConnector dbc = new DatabaseConnector();
	static InvIndexList indexList = new InvIndexList(dbc);
	static NodeList nodeList = new NodeList(dbc);
	static EdgeList edgeList = new EdgeList(dbc);
	static final Set<String> STOP_WORDS_LIST = new HashSet<String>(Arrays.asList(new String[]{"about",
			"are", "com", "for", "from", "how", "that", "the", "this", "was", "what",
			"when", "where", "who", "will", "with", "the"}));
	
	static void addToInvertedIndex(TreeNode n) {
		String key = n.k;
		String value = n.v;
		int id = n.nodeID;
		addToInvertedIndex(key, value, id);
	}

	static void addToInvertedIndex(String key, String value, int nodeID) {
		String[] keyParts = key.split("\\s+");
		String[] valueParts = value.split("\\s+");
		Set<String> uniqueWords = new HashSet<String>(Arrays.asList(keyParts));
		for (String s : valueParts) {
			uniqueWords.add(s);
		}

		for (String s : uniqueWords) {
			if (!NumberUtils.isNumber(s) && s.length() > 2 && !STOP_WORDS_LIST.contains(s)) {
				indexList.addToList(s, nodeID);
			}
		}
	}
	
	static int getDocID(File file) {
		// TODO
		return file.hashCode();
	}

	static int getTupleID(int tupleNumber, int docID) {
		// TODO
		return (Integer.toString(tupleNumber) + docID).hashCode();
	}

	static int getNodeID(String key, String value, int docID, int parentNodeID) {
		// TODO
		return (key + value + docID + parentNodeID).hashCode();
	}
	
	
	static void finishAllRemainingBatches(){
		indexList.addRemainingRecords();
		nodeList.addRemainingRecords();
		edgeList.addRemainingRecords();
	}

}

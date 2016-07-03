package DBProject.Extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class CommonsCSVParser extends DataParser {
	static int nextNodeID;
	static int docID;

	public static boolean parseWithCommonsCSV(File file, int doc, String docName) {
		try {
			CSVParser parser = new CSVParser(new FileReader(file),
					CSVFormat.DEFAULT.withHeader());
			Map<String, Integer> headers = parser.getHeaderMap();
			docID = doc;
			nextNodeID = dbc.getMaxNodeId() + 1;
			TreeNode t = new TreeNode(nextNodeID, docName, "", docID);
			nodeList.addToList(t);
			addToInvertedIndex(t);
			int rootID = nextNodeID;
			int rowNum = 0;
			for (CSVRecord record : parser) {
				rowNum++;
				nextNodeID++;
				edgeList.addToList(rootID, nextNodeID);
				nodeList.addToList(nextNodeID, "", "", docID);
				int tupleID = nextNodeID;

				for (String key : headers.keySet()) {
					nextNodeID++;
					String value = record.get(key).toLowerCase();
					key = key.toLowerCase();
					nodeList.addToList(nextNodeID, key, value, docID);
					edgeList.addToList(tupleID, nextNodeID);

					addToInvertedIndex(key, value, nextNodeID);
				}
				if (rowNum % 500 == 0) {
					System.out.println("read through " + rowNum + " rows");
				}

			}
			finishAllRemainingBatches();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

}

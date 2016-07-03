package DBProject.Extractor;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JaxpXMLParser extends DataParser {
	static int nextNodeID;
	static int docID;

	static boolean parseWithJaxp(File file, int doc, String docName) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);

		try {
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document document = builder.parse(file);
			docID = doc;
			nextNodeID = dbc.getMaxNodeId() + 1;
			Node root = document.getDocumentElement();
			TreeNode n = new TreeNode(nextNodeID, docName, "", docID);
			nodeList.addToList(n);
			addToInvertedIndex(n);
			parseWithJaxp(root, n.nodeID);
			finishAllRemainingBatches();
//			System.out.println(nodeList.toString());
//			System.out.println(edgeList.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	static void parseWithJaxp(Node node, int parentNodeID) {
		String key = "";
		String value = "";		
		int nodeID = nextNodeID;
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			nextNodeID++;
			nodeID = nextNodeID;
			Element elem = (Element) node;
			key = elem.getTagName();
			value = "";
			TreeNode n = null;
			if(node.getChildNodes().getLength() != 1) {
				n = new TreeNode(nodeID, key, value, docID);
			}
			else {
				n = new TreeNode(nodeID, key, node.getTextContent(), docID);
			}
			nodeList.addToList(n);
			addToInvertedIndex(n);
			Edge e = new Edge(parentNodeID, nodeID);
			edgeList.addToList(e);
			
			NamedNodeMap attrs = elem.getAttributes();
			if (attrs != null) {
				for (int i = 0; i < attrs.getLength(); i++) {
					Attr a = (Attr) attrs.item(i);
					nextNodeID++;
					edgeList.addToList(nodeID, nextNodeID);
					TreeNode t = new TreeNode(nextNodeID, a.getName(), a.getValue(),
							docID);
					nodeList.addToList(t);
					addToInvertedIndex(t);
				}
			}
		} else {
			value = node.getNodeValue() == null ? "" : node.getNodeValue()
					.trim();
		}

		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				parseWithJaxp(children.item(i), nodeID);
			}
		}
	}

}

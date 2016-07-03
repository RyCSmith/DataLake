package tests;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;

import junit.framework.TestCase;
import project.config.RootConfig;
import project.database.*;

public class DatabaseTest extends TestCase {

	DocumentJdbcTemplate docJdbc = getDocJdbcTemplate();
	NodeJdbcTemplate nodeJdbc = getNodeJdbcTemplate();
	EdgeJdbcTemplate edgeJdbc = getEdgeJdbcTemplate();
	UserJdbcTemplate userTemp = getUserJdbcTemplate();
	
	public UserJdbcTemplate getUserJdbcTemplate() {
		UserJdbcTemplate userTemp = new UserJdbcTemplate();
		userTemp.setDataSource(getDataSource());
		return userTemp;
	}
	
	
	public DocumentJdbcTemplate getDocJdbcTemplate() {
		DocumentJdbcTemplate docTemp = new DocumentJdbcTemplate();
		docTemp.setDataSource(getDataSource());
		return docTemp;
	}
	
	public NodeJdbcTemplate getNodeJdbcTemplate() {
		NodeJdbcTemplate nodeTemp = new NodeJdbcTemplate();
		nodeTemp.setDataSource(getDataSource());
		return nodeTemp;
	}
	
	public EdgeJdbcTemplate getEdgeJdbcTemplate() {
		EdgeJdbcTemplate edgeTemp = new EdgeJdbcTemplate();
		edgeTemp.setDataSource(getDataSource());
		return edgeTemp;
	}

	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    dataSource.setUrl("jdbc:mysql://ec2-50-19-202-216.compute-1.amazonaws.com/EMP" + 
	    		"?useUnicode=true&useJDBCCompliantTimezoneShift=true&serverTimezone=UTC");
	    dataSource.setUsername("ryan");
	    dataSource.setPassword("mysqladmin");
	    return dataSource;
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Test
	public void testInsertAndRetrieve() {
		int val = docJdbc.createAndReturnKey("rysmit", "new_test");
		System.out.println(val);
	}
	
	@Test
	public void testQueryForDocs() {
		List<Document> docs = docJdbc.getDocsByTermAndPerm("a", 'A');
		assertEquals(docs.size(), 1);
		assertEquals(docs.get(0).getName(), "test_document_1");
//		for (Document doc : docs) {
//			System.out.println(doc.getName() + " " + doc.getId());
//		}
	}
	
	@Test
	public void testGetNodeByID() {
		Node node = nodeJdbc.getNode(3);
		assertEquals(node.getKey(), "e");
		assertEquals(node.getValue(), "f");
	}
	
	@Test
	public void testGetNodesByDocsList() {
		List<Document> docs = docJdbc.getDocsByTermAndPerm("a", 'A');
		List<Node> nodes = nodeJdbc.getNodesByDocList(docs);
		assertEquals(nodes.size(), 13);
		
//		System.out.println("Num nodes: " + nodes.size());
//		for (Node node : nodes) {
//			System.out.println(node.getId() + " " + node.getKey() + " " + node.getValue());
//		}
	}
	
	@Test
	public void testGetEdgesByNodesList() {
		List<Document> docs = docJdbc.getDocsByTermAndPerm("a", 'A');
		List<Node> nodes = nodeJdbc.getNodesByDocList(docs);
		List<Edge> edges = edgeJdbc.getEdgesByNodeList(nodes);
		assertEquals(edges.size(), 12);
//		System.out.println("Num edges: " + edges.size());
//		for (Edge edge : edges) {
//			System.out.println(edge.getNode1ID() + " " + edge.getNode2ID() + " " + edge.getType());
//		}
	}
	
	@Test
	public void testGetDocsByTermAndUser() {
		User user = userTemp.getUser("rysmit");
		assertNotNull(user);
		List<Document> docs = docJdbc.getDocsByTermAndUser("friend", user);
		assertNotNull(docs);
		assertEquals(docs.size(), 1);
		
		user = userTemp.getUser("rysmit2");
		assertNotNull(user);
		docs = docJdbc.getDocsByTermAndUser("friend", user);
		
		for (Document doc : docs) {
			System.out.println(doc.getId() + " " + doc.getName());
		}
		assertNotNull(docs);
		assertEquals(docs.size(), 3);
		
	}

}

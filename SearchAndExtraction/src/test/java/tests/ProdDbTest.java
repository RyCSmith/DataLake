package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import project.components.SearchEngine;
import project.components.SingleWordSingleSearch;
import project.database.DocumentJdbcTemplate;
import project.database.EdgeJdbcTemplate;
import project.database.Node;
import project.database.NodeJdbcTemplate;
import project.database.UserJdbcTemplate;

public class ProdDbTest {

	SearchEngine engine;
	
	@Before
	public void setUp() throws Exception {
		engine = new SearchEngine();
		engine.setDocJdbc(getDocJdbcTemplate());
		engine.setEdgeJdbc(getEdgeJdbcTemplate());
		engine.setNodeJdbc(getNodeJdbcTemplate());
		userTemp = getUserJdbcTemplate();
	}
	
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

	@Bean
	public DataSource getDataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    dataSource.setUrl("jdbc:mysql://datalake.c2lclaii6yaq.us-west-2.rds.amazonaws.com/Datalake" + 
	    		"?useUnicode=true&useJDBCCompliantTimezoneShift=true&serverTimezone=UTC");
	    dataSource.setUsername("admin");
	    dataSource.setPassword("testing1234");
	    return dataSource;
	}
	
	@Test
	public void testGetRenaissance() {
		List<SingleWordSingleSearch> swssList = engine.getNodesAndPrepareClasses("renaissance", null);
		assertNotNull(swssList);
		assertEquals(swssList.size(), 1);
		assertEquals(swssList.get(0).document.getId(), -712980431);
		assertEquals(swssList.get(0).nodesSet.size(), 52353);
		assertEquals(swssList.get(0).edgesSet.size(), 52352);
		Node rootNode = swssList.get(0).identifyRootNode();
		assertNotNull(rootNode);
		assertEquals(rootNode.getId(), 1);
		assertNotNull(swssList.get(0).searchTerm);
		assertEquals(swssList.get(0).searchTerm, "renaissance");
		ArrayList<Node> path = swssList.get(0).getPathFromRoot();
		assertNotNull(path);
		for (Node node: path) {
			System.out.print(node + " ");
		} System.out.println();
	}

}

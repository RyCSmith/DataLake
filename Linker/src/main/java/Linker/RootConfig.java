package Linker;

import javax.sql.DataSource;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

import project.database.DocumentJdbcTemplate;
import project.database.EdgeJdbcTemplate;
import project.database.NodeJdbcTemplate;
import project.database.UserJdbcTemplate;

/**
 * 
 */
@Configuration
@ComponentScan({})
public class RootConfig {
	
	public final static String TEMP_DIR = "temp_uploads";
	public final static String AWS_USER_KEY = "AKIAIBYRLQ2Q4NFCVLHA";
	public final static String AWS_SECRET_KEY = "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";
	public final static String S3_BUCKET_NAME = "data-lake-five-fifty";	
	
	

	
//MySQL Connection Beans
	@Bean(name="userJdbcBean")
	public UserJdbcTemplate getUserJdbcTemplate() {
		UserJdbcTemplate userJdbc = new UserJdbcTemplate();
		userJdbc.setDataSource(getDataSource());
		return userJdbc;
	}
	
	@Bean(name="docJdbcBean")
	public DocumentJdbcTemplate getDocJdbcTemplate() {
		DocumentJdbcTemplate docTemp = new DocumentJdbcTemplate();
		docTemp.setDataSource(getDataSource());
		return docTemp;
	}
	
	@Bean(name="nodeJdbcBean")
	public NodeJdbcTemplate getNodeJdbcTemplate() {
		NodeJdbcTemplate nodeTemp = new NodeJdbcTemplate();
		nodeTemp.setDataSource(getDataSource());
		return nodeTemp;
	}
	
	@Bean(name="edgeJdbcBean")
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
	
 

}
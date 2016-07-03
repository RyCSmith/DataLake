package project.config;

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

import DBProject.Extractor.ExtractorJmsListener;
import net.sf.ehcache.config.CacheConfiguration;
import project.components.S3Interface;
import project.components.SearchEngine;
import project.database.DocumentJdbcTemplate;
import project.database.EdgeJdbcTemplate;
import project.database.NodeJdbcTemplate;
import project.database.UserJdbcTemplate;
import project.webComponents.SessionHelperFunctions;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

/**
 * 
 */
@Configuration
@EnableCaching
@EnableJms
@ComponentScan({})
public class RootConfig {
	
	public final static String TEMP_DIR = "temp_uploads";
	public final static String AWS_USER_KEY = "AKIAIBYRLQ2Q4NFCVLHA";
	public final static String AWS_SECRET_KEY = "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";
	public final static String S3_BUCKET_NAME = "data-lake-five-fifty";
	
//ActiveMQ stuff
	@Bean
    public ExtractorJmsListener myService() {
        return new ExtractorJmsListener();
    }
	@Bean
    public DefaultJmsListenerContainerFactory myJmsListenerContainerFactory() {
      DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
      factory.setConnectionFactory(getConnectionFactory());
      factory.setDestinationResolver(getDestResolver());
      factory.setConcurrency("5");
      return factory;
    }
	@Bean
	public ActiveMQConnectionFactory getConnectionFactory() {
		ActiveMQConnectionFactory acf = new ActiveMQConnectionFactory();
		acf.setBrokerURL("tcp://0.0.0.0:61616");
		return acf;
	}
	@Bean 
	public DynamicDestinationResolver getDestResolver() {
		return new DynamicDestinationResolver();
	}
	
	@Bean(name="jmsTemplateBean")
	public JmsTemplate getJmsTemplate() {
		JmsTemplate temp = new JmsTemplate(getConnectionFactory());
		temp.setDefaultDestinationName("testingQueue");
		return temp;
	}

	
//general beans
	@Bean(name="tempDir")
	public String getTempDir() {
		return TEMP_DIR;
	}
	
	@Bean(name="searchEngineBean")
	public SearchEngine getSearchEngine() {
		return new SearchEngine();
	}
	
//context refreshed bean - creates temp dir for uploads
	@Bean 
	public BootEventHandler getBootEventHandler() { 
		BootEventHandler beh = new BootEventHandler();
		beh.setTempDir(getTempDir());
		return beh;
	}
	
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
	
//Handler for multipart files
	 @Bean
	 public CommonsMultipartResolver multipartResolver() {
		 CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		 multipartResolver.setMaxUploadSize(100000000); //100 MB
		 multipartResolver.setDefaultEncoding("utf-8");
		 return multipartResolver;
	}
	 
	 
//configuring S3 Client
	@Bean(name="s3Interface")
	public S3Interface getS3Interface() {
		S3Interface s3 = new S3Interface();
		s3.setAmazonS3Client(amazonS3Client(basicAWSCredentials()));
		s3.setTempFilesDir(TEMP_DIR);
		s3.setBucket(S3_BUCKET_NAME);
		return s3;
	}
	
	@Bean
	public BasicAWSCredentials basicAWSCredentials() {
		return new BasicAWSCredentials(AWS_USER_KEY, AWS_SECRET_KEY);
	}
	
	@Bean
	public AmazonS3Client amazonS3Client(AWSCredentials awsCredentials) {
		AmazonS3Client amazonS3Client = new AmazonS3Client(awsCredentials);
		amazonS3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
		return amazonS3Client;
	}
	
//configuring caching
	/**
     * Creates an EHCache for the web app.
     * @return - an ehcache.CacheManager
     */
    @Bean(destroyMethod="shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("testCache");
        cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        cacheConfiguration.setMaxEntriesLocalHeap(1000);
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(cacheConfiguration);
        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    /**
     * Creates a CacheManager to be used for the web app.
     * @return springframework.cache.CacheManager.
     */
    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }
}
package project.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * This class replaces web.xml.
 * Defines the config classes
 *
 */
public class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	 
    @Override
    protected Class<?>[] getRootConfigClasses() {
    	return new Class[] { RootConfig.class };
    }
  
    @Override
    protected Class<?>[] getServletConfigClasses() {
    	return new Class[] { WebConfig.class };
    }
  
    /**
     * Includes all mappings 
     */
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
    
 
}
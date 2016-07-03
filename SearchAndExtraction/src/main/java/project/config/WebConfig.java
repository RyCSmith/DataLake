package project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import project.webComponents.AccountsController;
import project.webComponents.SessionHelperFunctions;
 
/**
 * 
 */
@Configuration
@EnableWebMvc
@ComponentScan({"project.webComponents"})
public class WebConfig extends WebMvcConfigurerAdapter {
	
	@Bean(name="sessionHelperFunctionsBean")
	SessionHelperFunctions getSessionHelperFunctions() {
		SessionHelperFunctions shf = new SessionHelperFunctions();
		return shf;
	}
	
	/**
	 * Adding a resource handler for static files. 
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
	
	/**
	 *
	 */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
 
}
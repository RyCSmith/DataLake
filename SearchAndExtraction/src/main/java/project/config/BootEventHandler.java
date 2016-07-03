package project.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

public class BootEventHandler {
	
	String TEMP_FILES_DIR;

	@EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        File tempDir = new File(TEMP_FILES_DIR);
        if (!tempDir.exists()) {
        	tempDir.mkdir();
        }
        System.out.println("Created Temp Files Directory at: " + tempDir.getAbsolutePath());
    }
	
	public void setTempDir(String tempDir) {
		TEMP_FILES_DIR = tempDir;
	}

}

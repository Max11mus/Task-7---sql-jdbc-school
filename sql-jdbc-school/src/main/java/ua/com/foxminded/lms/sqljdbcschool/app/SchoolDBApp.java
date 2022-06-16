package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.lms.sqljdbcschool.spring.SpringContextConfig;

public class SchoolDBApp {

	public static void main(String[] args) throws IOException, SQLException {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(SpringContextConfig.class);
		applicationContext.refresh();
		
		Menu appMenu = applicationContext.getBean(Menu.class);
		appMenu.runCycle();
		
		applicationContext.close();
	}

}
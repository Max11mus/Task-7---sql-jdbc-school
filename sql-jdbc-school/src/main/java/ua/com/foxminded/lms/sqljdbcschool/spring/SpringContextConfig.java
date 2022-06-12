package ua.com.foxminded.lms.sqljdbcschool.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;

@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.lms.sqljdbcschool.app")
public class SpringContextConfig {

	@Bean
	@Scope("singleton")
	@Lazy
	public SchoolDAO dao() {
		return new SchoolDAO(null);
	}

}

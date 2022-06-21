package ua.com.foxminded.lms.sqljdbcschool.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ua.com.foxminded.lms.sqljdbcschool.utils.DBConnectionPool;


@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.lms.sqljdbcschool.app, "
		+ "ua.com.foxminded.lms.sqljdbcschool.dao, ua.com.foxminded.lms.sqljdbcschool.controllers")
public class SpringContextConfig {

	@Bean
	@Lazy
	public DBConnectionPool dBConnectionPool(DBConnectionPool init) {
		return init;
	}

}

package ua.com.foxminded.lms.sqljdbcschool.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ua.com.foxminded.lms.sqljdbcschool.utils.DBConnectionPool;


@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.lms.sqljdbcschool.dao")
public class SpringDaoConfig {

	@Bean
	@Lazy
	public DBConnectionPool dBConnectionPool(DBConnectionPool init) {
		return init;
	}

}

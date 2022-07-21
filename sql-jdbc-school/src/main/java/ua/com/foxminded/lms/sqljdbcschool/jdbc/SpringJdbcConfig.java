package ua.com.foxminded.lms.sqljdbcschool.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import ua.com.foxminded.lms.sqljdbcschool.utils.DBConnectionPool;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.function.Function;


@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.lms.sqljdbcschool.jdbc")
public class SpringJdbcConfig {

	@Bean
	@Lazy
	public DBConnectionPool dBConnectionPool() {
		int initPoolSize = 5;
		FileLoader fileLoader = new FileLoader();
		URL propertiesURL = ClassPathResource.class.getResource("/db.posgresql.properties");
		Properties conectionProperties = new Properties();
		DBConnectionPool connectionPool;
		Function<Properties, DBConnectionPool> initPool = (properties) -> {
			try {
				properties.load(fileLoader.loadProperties(propertiesURL));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return new DBConnectionPool(properties, initPoolSize);
		};

		return initPool.apply(conectionProperties);
	}

}

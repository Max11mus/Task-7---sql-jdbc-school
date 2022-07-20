package ua.com.foxminded.lms.sqljdbcschool.hibernate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import ua.com.foxminded.lms.sqljdbcschool.jdbc.SchoolJdbcDAO;
import ua.com.foxminded.lms.sqljdbcschool.utils.DBConnectionPool;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

import java.io.IOException;
import java.util.Properties;

@Configuration
@Import({SchoolHibernateDAO.class, SchoolJdbcDAO.class})
public class TestConfig {
    @Bean
    public DBConnectionPool dBConnectionPool() {
        FileLoader fileLoader = new FileLoader();
        Properties dbProperties = new Properties();
        try {
            dbProperties.load(fileLoader.loadProperties(ClassLoader.getSystemResource("db.h2.properties")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new DBConnectionPool(dbProperties, 10);
    }

}

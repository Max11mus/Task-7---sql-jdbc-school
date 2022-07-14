package ua.com.foxminded.lms.sqljdbcschool.hibernate;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

import java.net.URL;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.lms.sqljdbcschool.hibernate")
public class HibernateSpringConfig {

}

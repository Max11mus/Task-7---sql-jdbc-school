package ua.com.foxminded.lms.sqljdbcschool.hibernate;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

@Component
public class HibernateProperties {
    FileLoader fileLoader = new FileLoader();
    private URL propertiesURL = ClassPathResource.class.getResource("/hibernate.properties");
    Properties hibernateProperties = new Properties();

    public HibernateProperties() {
        try {
            hibernateProperties.load(fileLoader.loadProperties(propertiesURL));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Properties getHibernateProperties() {
        return hibernateProperties;
    }
}
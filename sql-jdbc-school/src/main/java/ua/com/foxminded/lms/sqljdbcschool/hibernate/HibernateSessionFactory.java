package ua.com.foxminded.lms.sqljdbcschool.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class HibernateSessionFactory {
    @Autowired
    private HibernateProperties properties;

    private Configuration configuration = new Configuration();
    private SessionFactory sessionFactory;

    String EntitiesPackage = "ua.com.foxminded.lms.sqljdbcschool.entitybeans";

    @PostConstruct
    public void init() {
        configuration.addProperties(properties.getHibernateProperties());
        scanEntitiesPackage(EntitiesPackage)
                .stream()
                .forEach(claz -> configuration.addAnnotatedClass(claz));
        sessionFactory = configuration.buildSessionFactory();
    }

    private Set<Class> scanEntitiesPackage(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        return reader.lines().filter(line -> line.endsWith(".class"))
                .map(line -> {
                    try {
                        return Class.forName(packageName + "." + line.substring(0, line.lastIndexOf('.')));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
        }).collect(Collectors.toSet());
    }
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}


package ua.com.foxminded.lms.sqljdbcschool.hibernate;

import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateSessionFactory {
    @Autowired
    HibernateProperties properties;

    Configuration configuration;
    String EntitiesPackage = "ua.com.foxminded.lms.sqljdbcschool.entitybeans";

    public HibernateSessionFactory() {
        configuration
                .addProperties(properties.getHibernateProperties())
                .addPackage(EntitiesPackage)
                .buildSessionFactory();
    }
}

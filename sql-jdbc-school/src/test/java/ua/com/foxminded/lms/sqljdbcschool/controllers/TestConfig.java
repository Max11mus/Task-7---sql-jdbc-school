package ua.com.foxminded.lms.sqljdbcschool.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ua.com.foxminded.lms.sqljdbcschool.controllers.SpringWebControllersConfig;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.hibernate.SchoolHibernateDAO;
import ua.com.foxminded.lms.sqljdbcschool.jdbc.SchoolJdbcDAO;
import ua.com.foxminded.lms.sqljdbcschool.utils.DBConnectionPool;

@Configuration
@Import({ SpringWebControllersConfig.class })
@ComponentScan(basePackages = "ua.com.foxminded.lms.sqljdbcschool.hibernate")
public class TestConfig {
	@MockBean
	DBConnectionPool dBConnectionPool;

	@MockBean
	@Autowired
	SchoolDAO dao;

}

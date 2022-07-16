package ua.com.foxminded.lms.sqljdbcschool.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.hibernate.SchoolHibernateDAO;
import ua.com.foxminded.lms.sqljdbcschool.jdbc.SchoolJdbcDAO;

import javax.annotation.PostConstruct;

@Component
public class ChooseDaoEngine {
    @Autowired
    @Lazy
    SchoolJdbcDAO jdbcDAO;
    @Autowired
    SchoolHibernateDAO hibernateDAO;

    SchoolDAO currentDaoEngine = null;

    public SchoolDAO getCurrentDaoEngine() {
        return currentDaoEngine;
    }

    public void setJdbcEngine() {
        currentDaoEngine = jdbcDAO;
    }

    public void setHibernateJdbcEngine() {
        currentDaoEngine = hibernateDAO;
    }

    @PostConstruct
    private void init() {
        currentDaoEngine = hibernateDAO;
    }

}

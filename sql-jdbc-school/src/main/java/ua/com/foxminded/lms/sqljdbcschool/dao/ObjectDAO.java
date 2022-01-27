package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import ua.com.foxminded.lms.sqljdbcschool.entities.Course;

abstract class ObjectDAO<Entity>  implements CRUD<Entity> {
	private String query;
	private DataSource datasource;
	
	

	public ObjectDAO() {
		query="";
		datasource = new DataSource();
		
	}

	public void ExecuteSQL() {
		
	}

	

}
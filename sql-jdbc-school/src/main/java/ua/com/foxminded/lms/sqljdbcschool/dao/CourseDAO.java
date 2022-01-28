package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.SQLException;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;

public class CourseDAO extends TableDAO<Course> {

	public CourseDAO(String DBName, Connection connection) throws SQLException {
		super(DBName, connection);
		// TODO Auto-generated constructor stub
	}

}

package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.SQLException;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.StudentsOnCourse;

public class StudentsOnCourseDAO extends TableDAO<StudentsOnCourse> {

	public StudentsOnCourseDAO(String DBName, Connection connection) throws SQLException {
		super(DBName, connection);
		// TODO Auto-generated constructor stub
	}

}

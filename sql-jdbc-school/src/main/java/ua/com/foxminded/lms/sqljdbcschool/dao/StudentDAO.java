package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.SQLException;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class StudentDAO extends TableDAO<Student> {

	public StudentDAO(String DBName, Connection connection) throws SQLException {
		super(DBName, connection);
		// TODO Auto-generated constructor stub
	}

}

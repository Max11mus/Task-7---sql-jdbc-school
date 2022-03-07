package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ua.com.foxminded.lms.sqljdbcschool.utils.SQLQueryUtils;

public class SchoolDBIntializer {
	static protected String EOF = System.lineSeparator();
	private DBConnectionPool connectionPool;
	protected SQLQueryUtils SQLName = new SQLQueryUtils();

	public SchoolDBIntializer(DBConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
	}

	public void dropTables() throws SQLException, IOException {
		String query = "DROP TABLE IF EXISTS " + SQLName("StudentsOnCourse") + " CASCADE; " + EOF
				+ "DROP TABLE IF EXISTS " + SQLName("Course") + " CASCADE; " + EOF
				+ "DROP TABLE IF EXISTS " + SQLName("Student") + " CASCADE; " + EOF
				+ "DROP TABLE IF EXISTS " + SQLName("Group") + " CASCADE; " + EOF;

		Connection connection = null;
		Statement statement = null;

		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			statement.execute(query);
			connection.commit();
			
		} catch (SQLException e) {
			System.out.println("Drop Tables failure.");
			connection.rollback();
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connectionPool.checkIn(connection);
			}
		}

	}

	public void createTables() throws SQLException, IOException {
		String query = 
				"CREATE TABLE IF NOT EXISTS " + EOF
				+ SQLName("Course") + EOF
					+ "( " + SQLName("id") + " uuid NOT NULL UNIQUE PRIMARY KEY, " + EOF
					+ SQLName("courseName") + " varchar(255) NOT NULL, " + EOF
					+ SQLName("courseDescription") + " varchar(1024) NOT NULL ); " + EOF
				
				+ "CREATE TABLE IF NOT EXISTS " + EOF
				+ SQLName("Group") + EOF
					+ "( " + SQLName("id") + " uuid UNIQUE PRIMARY KEY, " + EOF
					+ SQLName("groupName") + " varchar(100) NOT NULL ); " + EOF
				
				+ "CREATE TABLE IF NOT EXISTS " + EOF
				+ SQLName("Student") + EOF
					+ "( " + SQLName("id") +" uuid NOT NULL UNIQUE PRIMARY KEY, " + EOF
					+ SQLName("studentFirstName") + " varchar(20) NOT NULL, " + EOF
					+ SQLName("studentLastName")  + " varchar(20) NOT NULL, " + EOF
					+ SQLName("groupId") + " uuid NULL " + EOF
						+ "REFERENCES " + SQLName("Group") + " ( " + SQLName("id") + " ) " + EOF
								+ "ON DELETE SET NULL ); " + EOF
				
				+ "CREATE TABLE IF NOT EXISTS " + EOF
				+ SQLName("StudentsOnCourse") + EOF
					+ " ( " + SQLName("studentID") + " uuid NOT NULL " + EOF
						+ "REFERENCES " + SQLName("Student") + " ( "+ SQLName("id") + " ), " + EOF
					+ SQLName("courseID") + " uuid NOT NULL " + EOF
						+ "REFERENCES " + SQLName("Course") + " ( " + SQLName("id") + " ), " + EOF
							+ " PRIMARY KEY( " + SQLName("studentID") + " , " + SQLName("courseID") + " )); " + EOF;

		Connection connection = null;
		Statement statement = null;

		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			statement.execute(query);
			connection.commit();
			
		} catch (SQLException e) {
			System.out.println("Create Tables failure.");
			connection.rollback();
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connectionPool.checkIn(connection);
			}
		}
		
		connection.commit();
	}

	private String SQLName(String camelCaseName) {
		return SQLQueryUtils.convert(camelCaseName);
	}
}

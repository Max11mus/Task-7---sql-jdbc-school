package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

public class SchoolDBInitializer {
	static protected String EOF = System.lineSeparator();
	private DBConnectionPool connectionPool;

	public SchoolDBInitializer(DBConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
	}

	public void dropTables() throws SQLException, IOException {
		String query = String.join(" ",
				new FileLoader().loadTextLines(ClassLoader.getSystemResource("drop.sql")));

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
		String query = String.join(" ",
				new FileLoader().loadTextLines(ClassLoader.getSystemResource("create.sql")));

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

}

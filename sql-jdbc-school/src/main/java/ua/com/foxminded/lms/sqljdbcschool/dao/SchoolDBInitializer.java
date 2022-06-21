package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import ua.com.foxminded.lms.sqljdbcschool.utils.DBConnectionPool;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

@Component
@Lazy
public class SchoolDBInitializer {
	static protected String EOF = System.lineSeparator();
	@Autowired
	private DBConnectionPool connectionPool;

	public SchoolDBInitializer(DBConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
	}

	public void dropTables() throws SQLException, IOException {
		String query = String.join(" ", new FileLoader().loadTextLines(ClassPathResource.class.getResource("/drop.sql")));

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
		String query = String.join(" ", new FileLoader().loadTextLines(ClassPathResource.class.getResource("/create.sql")));

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

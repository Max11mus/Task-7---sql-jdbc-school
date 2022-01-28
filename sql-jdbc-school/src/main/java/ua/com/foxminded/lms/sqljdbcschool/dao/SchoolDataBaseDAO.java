package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Squad;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.StudentsOnCourse;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;
import ua.com.foxminded.lms.sqljdbcschool.utils.SQLQueryUtils;

public class SchoolDataBaseDAO {
	private DBConnectionPool connectionPool;
	private CourseDAO courseDAO;
	private SquadDAO squadDAO;
	private StudentDAO studentDAO;
	private StudentsOnCourseDAO studentsOnCourseDAO;
	private String DBName;
	protected SQLQueryUtils SQLName = new SQLQueryUtils();

	public SchoolDataBaseDAO(DBConnectionPool connectionPool) {
		this.connectionPool = connectionPool;
		this.DBName = connectionPool.getDBname();
	}

	public void initTables() throws SQLException, IOException {
		Connection connection = null;
		try {
			connection = connectionPool.checkOut();
			this.courseDAO = new CourseDAO(DBName, connection);
			this.squadDAO = new SquadDAO(DBName, connection);
			this.studentDAO = new StudentDAO(DBName, connection);
			this.studentsOnCourseDAO = new StudentsOnCourseDAO(DBName, connection);
			connectionPool.checkIn(connection);
		} catch (SQLException e) {
			if (connection != null) {
				connectionPool.checkIn(connection);
			}
		}

	}

	public void dropTables() throws SQLException, IOException {
		URL dropSql = ClassLoader.getSystemResource("drop.sql");
		FileLoader fileLoader = new FileLoader();
		String query = fileLoader.loadLines(dropSql).stream().collect(Collectors.joining());

		Connection connection = null;
		Statement statement = null;

		try {
			connection = connectionPool.checkOut();
			statement = connection.createStatement();
			statement.execute(query);
			statement.close();
			connectionPool.checkIn(connection);

		} catch (SQLException e) {
			System.out.println("Drop Tables failure.");
			e.printStackTrace();
			throw e;
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
		URL createSql = ClassLoader.getSystemResource("create.sql");
		FileLoader fileLoader = new FileLoader();
		String query = fileLoader.loadLines(createSql).stream().collect(Collectors.joining());

		Connection connection = null;
		Statement statement = null;

		try {
			connection = connectionPool.checkOut();
			statement = connection.createStatement();
			statement.execute(query);
			statement.close();
		} catch (SQLException e) {
			System.out.println("Create Tables failure.");
			e.printStackTrace();
			throw e;
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connectionPool.checkIn(connection);
			}
		}
	}

}

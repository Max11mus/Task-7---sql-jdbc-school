package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

public class SchoolDataBase {
	private DBConnectionPool connectionPool;
	private CourseDAO courseDAO;
	private GroupDAO groupDAO;
	private StudentDAO studentDAO;
	private StudentsOnCourseDAO studentsOnCourseDAO;
	String DBName;

	public SchoolDataBase(DBConnectionPool connectionPool, String DBName) throws SQLException {
		this.connectionPool = connectionPool;
		this.DBName = DBName;
		this.courseDAO = new CourseDAO(DBName, connectionPool.checkOut());
		this.groupDAO = new GroupDAO(DBName, connectionPool.checkOut());
		this.studentDAO = new StudentDAO(DBName, connectionPool.checkOut());
		this.studentsOnCourseDAO = new StudentsOnCourseDAO(DBName, connectionPool.checkOut());
	}

	public void dropTables() throws SQLException {
		
		try {
			Connection connection = connectionPool.checkOut();
			
			URL DBPropertiesUrl = ClassLoader.getSystemResource("db.properties");

			FileLoader fileLoader = new FileLoader();
			Properties conectionProperties = new Properties();
			conectionProperties.load(fileLoader.load(DBPropertiesUrl));
			
			Statement statement = connection.createStatement();
			InputStream initSQLFile = ClassLoader.getSystemResourceAsStream("drop.sql");
			InputStreamReader initSQLStreamReader = new InputStreamReader(initSQLFile);
			BufferedReader initSQLReader = new BufferedReader(initSQLStreamReader);
			Stream<String> initSQLLines = initSQLReader.lines();
			query = initSQLLines.collect(Collectors.joining());

			statement.execute(query);
			statement.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
finally {
	
}
		
			}

	public void createTables() throws SQLException {
		Statement statement = connection.createStatement();
		InputStream initSQLFile = ClassLoader.getSystemResourceAsStream("create.sql");
		InputStreamReader initSQLStreamReader = new InputStreamReader(initSQLFile);
		BufferedReader initSQLReader = new BufferedReader(initSQLStreamReader);
		Stream<String> initSQLLines = initSQLReader.lines();
		query = initSQLLines.collect(Collectors.joining());

		statement.execute(query);
		statement.close();
	}

}

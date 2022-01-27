package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DataBaseInitializer {
	private String query;
	private Connection connection;

	public DataBaseInitializer(Connection connection) {
		this.connection = connection;
	}

	public void init() throws SQLException  {
		Statement statement = connection.createStatement();
		InputStream initSQLFile = ClassLoader.getSystemResourceAsStream("init.sql");
		InputStreamReader initSQLStreamReader = new InputStreamReader(initSQLFile);
		BufferedReader initSQLReader = new BufferedReader(initSQLStreamReader);
		Stream<String> initSQLLines = initSQLReader.lines();
		query = initSQLLines.collect(Collectors.joining());
		
		statement.execute(query);
		
	}

}

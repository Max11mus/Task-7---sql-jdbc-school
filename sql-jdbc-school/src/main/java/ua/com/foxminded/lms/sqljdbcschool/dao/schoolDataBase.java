package ua.com.foxminded.lms.sqljdbcschool.dao;

import javax.sql.DataSource;

public class schoolDataBase {
	private String query;
	private DataSource datasource;

	public void init() {
		String SQLDropCoursesTable = "DROP TABLE IF EXISTS cources;";
		String SQLCreateCourseTable = "CREATE TABLE IF NOT EXISTS"
				+ "cources(id serial primary key, name varchar(255), description varchar(1024));";
		
		String SQLDropGroupsTable = "DROP TABLE IF EXISTS groups;";
		String SQLCreateGroupsTable = "CREATE TABLE IF NOT EXISTS;"
				+ "groups(id serial primary key, name varchar(100));";
		
		String SQLDropStudentsTable = "DROP TABLE IF EXISTS students;";
		String SQLCreateStudentsTable = "CREATE TABLE IF NOT EXISTS;"
				+ "students(id serial primary key, first_name varchar(20), second_name varchar(20),"
				+ "group_id integer REFERENCES groups(id));";
		
	}

}

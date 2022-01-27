package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.util.ArrayList;

import ua.com.foxminded.lms.sqljdbcschool.entities.Course;

public class CourseDAO extends ObjectDAO<Course> {

	public CourseDAO(Connection connection) {
		super(connection);
	}

	@Override
	public ArrayList<Course> getAllEntities() {
		
		return null;
	}

	@Override
	public void createEntity(Course object) {
		
		
	}

	@Override
	public void updateEntity(Course object) {
		
		
	}

	@Override
	public void deleteEntity(Course object) {
		
		
	}

}

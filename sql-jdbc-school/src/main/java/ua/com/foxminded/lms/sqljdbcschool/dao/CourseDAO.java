package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class CourseDAO extends  BasicDAO<Course> {

	public CourseDAO(DBConnectionPool connectionPool) {
		super(connectionPool);
	}

	@Override
	public int delete(List<Course> entities) {
		if (entities == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}

		int result = 0;
		queryResultLog = "";
		
		if (!entities.isEmpty()) {
		String query = " DELETE FROM " + EOL
				+ SQLName("Course") + EOL
				+ " WHERE " + EOL
				+ SQLName("id") + EOL 
				+ " IN ("
				+ String.join(", ", Collections.nCopies(entities.size(), "?"))
				+ ");" + EOL; 
		
		PreparedStatement statement = null;
		Connection connection = null;
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);

			deleteEnrolledStudents(entities, connection);

			statement = connection.prepareStatement(query);
			int iteratorIndex = 1;
			for (Course course : entities) {
			statement.setObject(iteratorIndex++, course.getId());
			} 
			
			result = statement.executeUpdate();
			
			setQueryResultLogAppend(statement, result);
			
			connection.commit();
			
			
		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
			closeStatemant(statement);
		}

	}

	return result;

}
	
	public int deleteAll() {
		int result = 0;
		String queryStudentsOnCourse = " DELETE FROM " + EOL
				+ SQLName("StudentsOnCourse") + " ;" + EOL;
		String queryCourses = " DELETE FROM " + EOL
				+ SQLName("Course") + " ;" + EOL;
		
		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);

			statement = connection.prepareStatement(queryStudentsOnCourse);
			result = statement.executeUpdate();
			setQueryResultLog(statement, result);
			statement.close();
			
			statement = connection.prepareStatement(queryCourses);
			result = statement.executeUpdate();
			setQueryResultLogAppend(statement, result);

			connection.commit();

		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
			closeStatemant(statement);
		}
		return result;
	}
	
	@Override
	public int insert(List<Course> entities){
		if (entities == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}

		int result = 0;
		queryResultLog = "";
		
		if (entities.size() != 0) {
		String query = "INSERT INTO " + EOL 
				+ SQLName("Course") + EOL
				+ " ( " + SQLName("id") + ", " + EOL
				+ SQLName("courseName") + ", " + EOL 
				+ SQLName("courseDescription") + ") " + EOL
				+ " VALUES " 
				+ String.join(", ", Collections.nCopies(
						entities.size(), " ( ?, ?, ? )")) + ";" + EOL;
		
		Connection connection = null;
		PreparedStatement statement = null;
			
		try {
			int iteratorIndex = 1;
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);

			for (Course course : entities) {
			statement.setObject(iteratorIndex++, course.getId());
			statement.setObject(iteratorIndex++, course.getCourseName());
			statement.setObject(iteratorIndex++, course.getCourseDescription());
			} 
			
			result = statement.executeUpdate();
			setQueryResultLog(statement, result);
			
			insertEnrolledStudents(entities, connection);
			connection.commit();
			
		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		}
		finally {
			connectionPool.checkIn(connection);
			closeStatemant(statement);
		}
	}
	return result;
}
	
	@Override
	public int update(List<Course> entities) {
		if (entities == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}

		int result = 0;
		queryResultLog = "";
		
		if (entities.size()!=0) {
			
		String queryUpdateRow = " UPDATE " + SQLName("Course") + EOL 
				+ " SET " + EOL
				+ SQLName("courseName") + " = ?, " + EOL
				+ SQLName("courseDescription") + " = ?" + EOL 
				+ " WHERE " + EOL 
				+ SQLName("id") + " = ? ;"  + EOL;
		
		String queryUpdateRows = String.join("", Collections.nCopies(entities.size(), queryUpdateRow ));
	
		Connection connection = null;
		PreparedStatement statement = null;
				
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(queryUpdateRows);
			
			int iteratorIndex = 1;

			for (Course course : entities) {
			statement.setObject(iteratorIndex++, course.getCourseName());
			statement.setObject(iteratorIndex++, course.getCourseDescription());
			statement.setObject(iteratorIndex++, course.getId());
			}
			
			result = statement.executeUpdate();
			setQueryResultLog(statement, result);
			
			updateEnrolledStudents(entities, connection);
			
			connection.commit();
			
		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		}
		finally {
			connectionPool.checkIn(connection);
			closeStatemant(statement);
		}
	}
	return result;
	}
	
	@Override
	public List<Course> getAll() {
		return get("", new ArrayList<String>());
	}
	
	@Override
	public List<Course> get(String conditions, List<String> params) {
		if (conditions == null || params == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}

		String query = "SELECT " + EOL
				+ SQLName("id") + ", "+ EOL 
				+ SQLName("courseName") + ", "+ EOL
				+ SQLName("courseDescription")+ EOL
				+ " FROM "+ EOL
				+ SQLName("Course") + EOL;

		if (!conditions.isEmpty()) {
			query += " " + conditions +";"+ EOL;
		}

		ResultSet result = null;
		queryResultLog = "";
		ArrayList<Course> resultCourses = new ArrayList<Course>();
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			for (int i = 0; i < params.size(); i++) {
				statement.setString(i + 1, params.get(i));
			}

			result = statement.executeQuery();
			setQueryResultLog(statement, result);
			
			Course course = null;

			while (result.next()) {
				course = new Course();
				course.setId((UUID) result.getObject(SQLName("id")));
				course.setCourseName((String) (result.getObject(SQLName("courseName"))));
				course.setCourseDescription((String) result.getObject(SQLName("courseDescription")));
				resultCourses.add(course);
			}

			HashMap<UUID, List<Student>> students = getStudents(resultCourses, connection);
			resultCourses.parallelStream().forEach(e -> e.getEnrolledStudents()
					.addAll((students.containsKey(e.getId())
							? students.get(e.getId())
							: new ArrayList<Student>())));

			connection.commit();
			
		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		}
		finally {
			connectionPool.checkIn(connection);
			closeResultSet(result);
			closeStatemant(statement);
		}
		return resultCourses;
	}
	
	@Override
	public List<Course> getRange(int startRow, int endRow) {
		if (startRow > endRow || startRow <0 || endRow < 0) {
			throw new IllegalArgumentException("ERROR: Illegal arguments.");
		}

		String query = "SELECT * " + EOL
				+ " FROM " + EOL
				+ SQLName("Course") + EOL
				+ " LIMIT " + (endRow - startRow) + EOL
				+ " OFFSET " + startRow  + ";" + EOL;

		ResultSet result = null;
		queryResultLog = "";
		ArrayList<Course> resultCourses = new ArrayList<Course>();
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			result = statement.executeQuery();
			setQueryResultLog(statement, result);
			
			Course course = null;

			while (result.next()) {
				course = new Course();
				course.setId((UUID) result.getObject(SQLName("id")));
				course.setCourseName((String) (result.getObject(SQLName("courseName"))));
				course.setCourseDescription((String) result.getObject(SQLName("courseDescription")));
				resultCourses.add(course);
			}

			HashMap<UUID, List<Student>> students = getStudents(resultCourses, connection);
			resultCourses.parallelStream().forEach(e -> e.getEnrolledStudents()
					.addAll((students.containsKey(e.getId())
							? students.get(e.getId())
							: new ArrayList<Student>())));

			connection.commit();
			
		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		}
		finally {
			connectionPool.checkIn(connection);
			closeResultSet(result);
			closeStatemant(statement);
		}
		return resultCourses;
	}

	@Override
	public int getRowsCount() {
		int result = 0;
		ResultSet resultSet = null;

		String query = "SELECT COUNT(*) " + EOL
				+ " FROM " + EOL
				+ SQLName("Course") + ";" + EOL;
		
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			
			resultSet = statement.executeQuery();
			connection.commit();
			resultSet.next();
			result = resultSet.getInt(1);
			setQueryResultLog(statement, resultSet);
			
		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		}
		finally {
			connectionPool.checkIn(connection);
			closeResultSet(resultSet);
			closeStatemant(statement);
			
		}
		return result;

	}
	
	private void insertEnrolledStudents(List<Course> сourses, Connection connection) {
		if ((сourses== null) || (connection == null)) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}
		
		if (!сourses.isEmpty()) {
			int numberOfEnrolledStudents = сourses.stream()
					.mapToInt(course -> course.getEnrolledStudents().size()).sum();
			if (numberOfEnrolledStudents > 0) {
				
			String query = "INSERT INTO " + EOL
				+ SQLName("StudentsOnCourse") + EOL
				+ " ( " + SQLName("studentId") + ", "+ EOL
				+ SQLName("courseId") + ") "+ EOL
				+ " VALUES "+ EOL
				+ String.join(", ", Collections.nCopies(
						numberOfEnrolledStudents, "( ?, ? )")) 
				+ ";" + EOL;
		
			PreparedStatement statement = null;
			try {
				int iteratorIndex = 1;
				statement = connection.prepareStatement(query);
				for (Course course : сourses) {
					HashSet<Student> enrolledStudents = course.getEnrolledStudents();
					for (Student student : enrolledStudents) {
						statement.setObject(iteratorIndex++, student.getId());
						statement.setObject(iteratorIndex++, course.getId());
					}
				}

				int result = statement.executeUpdate();
				queryResultLog = "";
				setQueryResultLogAppend(statement, result);

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatemant(statement);
			}

		}
	}
}

	private void updateEnrolledStudents(List<Course> сourses, Connection connection) {
		if ((сourses == null) || (connection == null)) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}
		queryResultLog="";
		deleteEnrolledStudents(сourses, connection);
		insertEnrolledStudents(сourses, connection);

	}

	private void deleteEnrolledStudents(List<Course> сourses, Connection connection)
	{
		if ((сourses== null) || (connection == null)) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}
		
		if (сourses.size() != 0) {
			String query = "DELETE FROM " + EOL
				+ SQLName("StudentsOnCourse") + EOL
				+ " WHERE " + SQLName("courseId") + EOL
				+ " IN " 
				+ " ("
				+ String.join(", ", 
							Collections.nCopies(сourses.size(), " ?"))
					+ ");" + EOL;
		
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				int iteratorIndex = 1;
				for (Course course : сourses) {
					statement.setObject(iteratorIndex++, course.getId());
				}

				int result = statement.executeUpdate();
				setQueryResultLogAppend(statement, result);

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	
	public HashMap<UUID, List<Student>> getStudents(ArrayList<Course> courses, Connection connection) {
		if ((courses == null) || (connection == null)) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}
		
		HashMap<UUID, List<Student>> resultStudents = new HashMap<UUID, List<Student>>();
			
		if (!courses.isEmpty()) {

		String query = " SELECT " + EOL
				+ SQLName("StudentsOnCourse") + "." + SQLName("courseId") + ", " + EOL
				+ SQLName("StudentsOnCourse") + "." + SQLName("studentId") + ", "+ EOL
				+ SQLName("Student") + "." + SQLName("groupId") + ", "+ EOL
				+ SQLName("Student") + "." + SQLName("studentFirstName") + ", "+ EOL
				+ SQLName("Student") + "." + SQLName("studentLastName")+ EOL
				+ " FROM " + SQLName("StudentsOnCourse")+ EOL
				+ " LEFT JOIN " + SQLName("Student") + EOL
				+ " ON " + EOL
				+ SQLName("StudentsOnCourse") + "." + SQLName("studentId") + "=" + EOL
				+ SQLName("Student") + "." + SQLName("Id") + EOL
				+ " WHERE " + EOL
				+ SQLName("courseId") + EOL
				+ " IN ( "
				+ String.join(", ", Collections.nCopies(courses.size(), "?"))
				+ " )" + EOL;

		ResultSet result = null;
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			int iteratorIndex = 1;
			for (Course course : courses) {
				statement.setObject(iteratorIndex++, course.getId());
			}

			result = statement.executeQuery();

			UUID courseId = null;
			Student student = null;
			HashMap<UUID, Student> students = new HashMap<UUID, Student>();

			while (result.next()) {
				Student temp = new Student();
				temp.setId((UUID) result.getObject(SQLName("studentId")));
				temp.setGroupId(nullToZeroAndVersa.apply((UUID) result.getObject(SQLName("groupId"))));
				temp.setStudentFirstName(result.getString(SQLName("studentFirstName")));
				temp.setStudentLastName(result.getString(SQLName("studentLastName")));
				student = students.computeIfAbsent(temp.getId(), (key) -> temp);

				courseId = (UUID) result.getObject(SQLName("courseId"));
				resultStudents.computeIfAbsent(courseId, (key) -> new ArrayList<Student>());
				resultStudents.get(courseId).add(student);
			}

			setQueryResultLogAppend(statement, result);
			
		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		} finally {
			closeResultSet(result);
			closeStatemant(statement);
		}
	}
	return resultStudents;
}

}
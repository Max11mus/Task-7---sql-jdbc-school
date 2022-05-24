package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class SchoolDAO{
	static private String EOL = System.lineSeparator();
	private DBConnectionPool connectionPool;
	private  String queryResultLog="";
	private boolean enableLogging = false;
	private  Function<UUID, UUID> nullToZeroAndVersa = (uuid) -> {
		UUID result = uuid;
		if (uuid == null) {
			result = UUID.fromString("00000000-0000-0000-0000-000000000000");
		} else 
		if (uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
			result = null;
		}
		return result;
	};
	
	public SchoolDAO(DBConnectionPool connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}

	public void insertGroups(ArrayList<Group> groups) {
		if (groups == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}
		boolean saveEnableLogging = enableLogging;
		enableLogging = false;
		groups.parallelStream().forEach(this::insert);
		enableLogging = saveEnableLogging;
	}

	public void insert(Group group) {
		if (group == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}
		
		String query = "INSERT INTO \"group\" " + EOL  
				+ " ( id, group_name) " + EOL
				+ " VALUES ( ?, ? );" + EOL;
			
			Connection connection = null;
			PreparedStatement statement = null; 
			try  {
				connection = connectionPool.checkOut();
				connection.setAutoCommit(false);
				statement = connection.prepareStatement(query);
				int iteratorIndex = 1;
				
					statement.setObject(iteratorIndex++, group.getId());
					statement.setObject(iteratorIndex++, group.getGroupName());
				
				statement.executeUpdate();
				connection.commit();
				
				if (enableLogging) {
					queryResultLog = "Query executed:" + EOL;
					queryResultLog += statement.toString() + EOL;
					queryResultLog += "Results:" + EOL;
					queryResultLog += "1 row inserted." + EOL;
				}
				
			} catch (SQLException e) {
				rollbackConnection(connection);
				e.printStackTrace();
			}
			finally {
				connectionPool.checkIn(connection);
				closeStatemant(statement);
			}
	}

	public void insertStudents(ArrayList<Student> students) {
		if (students == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}
		boolean saveEnableLogging = enableLogging;
		enableLogging = false;
		students.parallelStream().forEach(this::insert);
		enableLogging = saveEnableLogging;
	}
	
	public void insert(Student student) {
		if (student == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}
		
		String query = "INSERT INTO student" + EOL  
				+ " ( id, group_id, student_first_name, student_last_name ) " + EOL
				+ " VALUES ( ?, ?, ?, ? );" + EOL;
			
			Connection connection = null;
			PreparedStatement statement = null; 
			try  {
				connection = connectionPool.checkOut();
				connection.setAutoCommit(false);
				statement = connection.prepareStatement(query);
				int iteratorIndex = 1;
				
					statement.setObject(iteratorIndex++, student.getId());
					statement.setObject(iteratorIndex++, nullToZeroAndVersa.apply(student.getGroupId()));
					statement.setObject(iteratorIndex++, student.getStudentFirstName());
					statement.setObject(iteratorIndex++, student.getStudentLastName());
				
				statement.executeUpdate();
				connection.commit();
				
				if (enableLogging) {
					queryResultLog = "Query executed:" + EOL;
					queryResultLog += statement.toString() + EOL;
					queryResultLog += "Results:" + EOL;
					queryResultLog += "1 row inserted." + EOL;
				}
				
			} catch (SQLException e) {
				rollbackConnection(connection);
				e.printStackTrace();
			}
			finally {
				connectionPool.checkIn(connection);
				closeStatemant(statement);
			}
		}
	
	public void insertCourses(ArrayList<Course> courses) {
		if (courses == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}
		boolean saveEnableLogging = enableLogging;
		enableLogging = false;
		courses.parallelStream().forEach(this::insert);
		courses.parallelStream().forEach(course -> course.getEnrolledStudents().parallelStream()
				.forEach(student -> addStudentToCourse(student.getId(), course.getId())));
		enableLogging = saveEnableLogging;
	}
	
	private void insert(Course course) {
		if (course == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}
		
		String query = "INSERT INTO course " + EOL  
				+ " ( id, course_name, course_description) " + EOL
				+ " VALUES ( ?, ?, ?);" + EOL;
			
			Connection connection = null;
			PreparedStatement statement = null; 
			try  {
				connection = connectionPool.checkOut();
				connection.setAutoCommit(false);
				statement = connection.prepareStatement(query);
				int iteratorIndex = 1;
				
					statement.setObject(iteratorIndex++, course.getId());
					statement.setObject(iteratorIndex++, course.getCourseName());
					statement.setObject(iteratorIndex++, course.getCourseDescription());
				
				statement.executeUpdate();
				connection.commit();
				
				if (enableLogging) {
					queryResultLog = "Query executed:" + EOL;
					queryResultLog += statement.toString() + EOL;
					queryResultLog += "Results:" + EOL;
					queryResultLog += "1 row inserted." + EOL;
				}
				
				course.getEnrolledStudents().parallelStream()
						.forEach(student -> addStudentToCourse(student.getId(), course.getId()));
				
			} catch (SQLException e) {
				rollbackConnection(connection);
				e.printStackTrace();
			}
			finally {
				connectionPool.checkIn(connection);
				closeStatemant(statement);
			}
	}
	
	public List<Student> getAllStudents() {
		String query = "SELECT *" + EOL
				+ " FROM " + EOL
				+ "student" + EOL;
		
		ResultSet result = null;
		ArrayList<Student> resultStudents = new ArrayList<Student>();

		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			result = statement.executeQuery();
			connection.commit();
			if (enableLogging) {
				queryResultLog = statement.toString() + EOL;
				queryResultLog += "Results:" + EOL;

			}
			
			
			Student student = null;
			int rowNo = 1;
			while (result.next()) {
				student = new Student();
				student.setId((UUID) result.getObject("id"));
				student.setGroupId(nullToZeroAndVersa.apply((UUID) result.getObject("group_id")));
				student.setStudentFirstName((String) (result.getObject("student_first_name")));
				student.setStudentLastName((String) result.getObject("student_last_name"));
				resultStudents.add(student);
				
				if (enableLogging) {
					queryResultLog += "RowNO  " + String.valueOf(rowNo++) + "  " + student.toString() + EOL;
				}
			}
			
		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		}
		finally {
			connectionPool.checkIn(connection);
			closeResultSet(result);
			closeStatemant(statement);
		}
		
		return resultStudents;
	}

	public void deleteStudent(UUID studentUUID) {
		if (studentUUID == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}

		int result = 0;

		String queryStudentsOnCourse = " DELETE FROM " + EOL
				+ "students_on_course" + EOL
				+ " WHERE " + EOL
				+ "student_id" + EOL 
				+ " IN ( ? ); " + EOL;
			
		String queryStudents = " DELETE FROM " + EOL
				+ "student" + EOL 
				+ " WHERE" + EOL
				+ "id" + EOL 
				+ " IN ( ? ); " + EOL;

		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);

			statement = connection.prepareStatement(queryStudentsOnCourse);
			statement.setObject(1, studentUUID);
			result = statement.executeUpdate();
			if (enableLogging) {
				queryResultLog = "Query executed:" + EOL;
				queryResultLog += statement.toString() + EOL;
				queryResultLog += "Results:" + EOL;
				queryResultLog += result + " rows deleted." + EOL;
			}
			statement.close();

			statement = connection.prepareStatement(queryStudents);
			statement.setObject(1, studentUUID);

			result = statement.executeUpdate();
			result = statement.executeUpdate();

			if (enableLogging) {
				queryResultLog += "Query executed:" + EOL;
				queryResultLog += statement.toString() + EOL;
				queryResultLog += "Results:" + EOL;
				queryResultLog += "1 row deleted." + EOL;
			}

			connection.commit();

		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
			closeStatemant(statement);
		}
	}

	public void findGroupsStudentCountLessOrEquals(int studentCount) {
		String query =  "SELECT " + EOL
				+ "\"group\".id," + EOL
				+ "\"group\".group_name,"
				+ "COUNT(*) AS \"Student Count\"" + EOL
				+ "FROM \"group\"" + EOL
				+ "INNER JOIN student" + EOL
				+ "ON  student.group_id = \"group\".id" + EOL
				+ "GROUP BY \"group\".id" + EOL
				+ "HAVING COUNT(*) <= CAST( ? AS INT) ;"; 
		
		ResultSet result = null;

		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			statement.setObject(1, studentCount);
			result = statement.executeQuery();
			connection.commit();
			if (enableLogging) {
				queryResultLog = statement.toString() + EOL;
				queryResultLog += "Results:" + EOL;

			}
			
			Group group = null;
			int rowNo = 1;
			String curentStudentCount = "";
			while (result.next()) {
				group = new Group();
				group.setId((UUID) result.getObject("id"));
				group.setGroupName(((String) (result.getObject("group_name"))));
				curentStudentCount = (((String) (result.getString("Student Count"))));
				if (enableLogging) {
					queryResultLog += "RowNO  " + String.valueOf(rowNo++) + "  " + group.toString()
							+ " Student Count = " + curentStudentCount + EOL;
				}
			}

		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
			closeResultSet(result);
			closeStatemant(statement);
		}
	}

	public List<Course> getAllCourses() {
		String query = "SELECT * " + EOL
				+ "FROM" + EOL
				+ "course ;";

		ResultSet result = null;
		List<Course> resultCourses = new ArrayList<Course>();
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			result = statement.executeQuery();
			
			if (enableLogging) {
				queryResultLog = statement.toString() + EOL;
				queryResultLog += "Results:" + EOL;
			}
			
			Course course = null;
			int rowNo = 1;
			while (result.next()) {
				course = new Course();
				course.setId((UUID) result.getObject("id"));
				course.setCourseName((String) (result.getObject("course_name")));
				course.setCourseDescription((String) result.getObject("course_description"));
				resultCourses.add(course);
			
				if (enableLogging) {
					queryResultLog += "RowNO  " + String.valueOf(rowNo++) + "  " + course.toString() + EOL;
				}
			}
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

	public void FindStudentsByCourseID(UUID courseId) {
		if (courseId == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}
		
		String query =  "SELECT " + EOL
				+ "students_on_course.course_id, "+ EOL
				+ "student.id,"+ EOL
				+ "student.group_id," + EOL
				+ "student.student_first_name," + EOL
				+ "student.student_last_name" + EOL
				+ "FROM students_on_course" + EOL
				+ "INNER JOIN student" + EOL
				+ "ON student.id = students_on_course.student_id" + EOL
				+ "WHERE course_id IN ( ? );" + EOL;
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			statement.setObject(1, courseId);
			result = statement.executeQuery();
			
			if (enableLogging) {
				queryResultLog = statement.toString() + EOL;
				queryResultLog += "Results:" + EOL;

			}
			
			Student student = null;
			int rowNo = 1;
			while (result.next()) {
				student = new Student();
				student.setId((UUID) result.getObject("id"));
				student.setGroupId(nullToZeroAndVersa.apply((UUID) result.getObject("group_id")));
				student.setStudentFirstName((String) (result.getObject("student_first_name")));
				student.setStudentLastName((String) result.getObject("student_last_name"));
								
				if (enableLogging) {
					queryResultLog += "RowNO  " + String.valueOf(rowNo++) + " "
							+ " " + student.toString() + EOL;
				}
			}
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
		
	}

	public void addStudentToCourse(UUID studentId, UUID courseId) {
		if (courseId == null || studentId == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}

		int result = 0;
		String query = "INSERT INTO" + EOL
				+ "students_on_course" + EOL
				+ " ( student_id," + EOL
				+ "course_id)" + EOL
				 + "VALUES ( ?, ?) " + EOL
				 + " ON CONFLICT (student_id, course_id) DO NOTHING ;" + EOL;
	
		Connection connection = null;
		PreparedStatement statement = null;
				
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);

			statement.setObject(1, studentId);
			statement.setObject(2, courseId);
			
			result = statement.executeUpdate();
			
			if (enableLogging) {
				queryResultLog = "Query executed:" + EOL;
				queryResultLog += statement.toString() + EOL;
				queryResultLog += "Results:" + EOL;
				queryResultLog += result + " row inserted." + EOL;
			}

			connection.commit();

		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
			closeStatemant(statement);
		}
	}
		
	public List<Course> findStudentCourses(UUID studentUUID) {
		if (studentUUID == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}
		ResultSet result = null;
		List<Course> studentCourses = new ArrayList<Course>();
		
		String query = "SELECT " + EOL
				+ "course.id," + EOL
				+ "course.course_name," + EOL 
				+ "course.course_description," + EOL
				+ "students_on_course.student_id" + EOL
				+ "FROM" + EOL
				+ "course" + EOL
				+ "RIGHT JOIN" + EOL 
				+ "students_on_course" + EOL
				+ "ON" + EOL
				+ " course.id = students_on_course.course_id " + EOL
				+ "WHERE" + EOL
				+ " students_on_course.student_id = ? " + EOL; 


		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);

			statement = connection.prepareStatement(query);
			statement.setObject(1, studentUUID);
			result = statement.executeQuery();
			
			if (enableLogging) {
				queryResultLog = "Query executed:" + EOL;
				queryResultLog += statement.toString() + EOL;
				queryResultLog += "Results:" + EOL;
				queryResultLog += result + " rows deleted." + EOL;
			}

			Course course = null;
			int rowNo = 1;
			while (result.next()) {
				course = new Course();
				course.setId((UUID) result.getObject("id"));
				course.setCourseName((String) (result.getObject("course_name")));
				course.setCourseDescription((String) result.getObject("course_description"));
				studentCourses.add(course);

				if (enableLogging) {
					queryResultLog += "RowNO  " + String.valueOf(rowNo++) + "  " + course.toString() + EOL;
				}
				connection.commit();
			}
		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
			closeStatemant(statement);
		}
		
		return studentCourses;
	}

	public void dropoutStudentFromCourse(UUID studentId, UUID courseId) {
		if (courseId == null || studentId == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}

		int result = 0;
		String query = "DELETE FROM" + EOL
				+ "students_on_course" + EOL
				 + "WHERE"
				 + " student_id = ? "
				 + "AND "
				 + "course_id = ? ;" + EOL;
	
		Connection connection = null;
		PreparedStatement statement = null;
				
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);

			statement.setObject(1, studentId);
			statement.setObject(2, courseId);
			
			result = statement.executeUpdate();
			
			if (enableLogging) {
				queryResultLog = "Query executed:" + EOL;
				queryResultLog += statement.toString() + EOL;
				queryResultLog += "Results:" + EOL;
				queryResultLog += result + " row deleted." + EOL;
			}

			connection.commit();

		} catch (SQLException e) {
			rollbackConnection(connection);
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
			closeStatemant(statement);
		}
		
	}

	public String getQueryResultLog() {
		return queryResultLog;
	}

	public boolean isEnableLogging() {
		return enableLogging;
	}

	public void setEnableLogging(boolean enableLogging) {
		this.enableLogging = enableLogging;
	}
	
	private void rollbackConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void closeStatemant(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	


}

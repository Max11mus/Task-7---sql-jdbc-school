package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.utils.CheckForNull;

public class SchoolDAO{
	static private String EOL = System.lineSeparator();
	private DBConnectionPool connectionPool;
	public SchoolDAO(DBConnectionPool connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}

	public void insertGroups(List<Group> groups) {
		CheckForNull.check(groups);

		groups.parallelStream().forEach(this::insertGroup);
	}

	public void insertGroup(Group group) {
		CheckForNull.check(group);
		
		String query = "INSERT INTO group_1" + EOL  
				+ " ( uuid, group_name) " + EOL
				+ " VALUES ( ?, ? );" + EOL;
			
			Connection connection = null;
			PreparedStatement statement = null; 
			try  {
				connection = connectionPool.checkOut();
				connection.setAutoCommit(false);
				statement = connection.prepareStatement(query);
				int iteratorIndex = 1;

				statement.setObject(iteratorIndex++, group.getUuid());
				statement.setObject(iteratorIndex++, group.getGroupName());

				statement.executeUpdate();
				connection.commit();
				
				synchronized (System.out) {
					System.out.println("Query executed:");
					System.out.println(statement.toString());
					System.out.println("Results:");
					System.out.println("1 row inserted.");
				}

			} catch (SQLException e) {
				rollbackTransaction(connection);
				e.printStackTrace();
			}
			finally {
				connectionPool.checkIn(connection);
				closeStatemant(statement);
			}
	}

	public void insertStudents(List<Student> students) {
		CheckForNull.check(students);

		students.parallelStream().forEach(this::insertStudent);
	}
	
	public void insertStudent(Student student) {
		CheckForNull.check(student);
		
		String query = "INSERT INTO student" + EOL  
				+ " ( uuid, group_uuid, first_name, last_name ) " + EOL
				+ " VALUES ( ?, ?, ?, ? );" + EOL;
			
			Connection connection = null;
			PreparedStatement statement = null; 
			try  {
				connection = connectionPool.checkOut();
				connection.setAutoCommit(false);
				statement = connection.prepareStatement(query);
				int iteratorIndex = 1;
				
				statement.setObject(iteratorIndex++, student.getUuid());
				statement.setObject(iteratorIndex++, student.getGroupUuid());
				statement.setObject(iteratorIndex++, student.getFirstName());
				statement.setObject(iteratorIndex++, student.getLastName());

				statement.executeUpdate();
				connection.commit();
				
				synchronized (System.out) {
					System.out.println("Query executed:");
					System.out.println(statement.toString());
					System.out.println("Results:");
					System.out.println("1 row inserted.");
				}

			} catch (SQLException e) {
				rollbackTransaction(connection);
				e.printStackTrace();
			}
			finally {
				connectionPool.checkIn(connection);
				closeStatemant(statement);
			}
		}
	
	public void insertCourses(List<Course> courses) {
		CheckForNull.check(courses);
		
		courses.parallelStream().forEach(this::insertCourse);
	}
	
	private void insertCourse(Course course) {
		CheckForNull.check(course);
		
		String query = "INSERT INTO course " + EOL  
				+ " ( uuid, course_name, course_description) " + EOL
				+ " VALUES ( ?, ?, ?);" + EOL;
			
			Connection connection = null;
			PreparedStatement statement = null; 
			try  {
				connection = connectionPool.checkOut();
				connection.setAutoCommit(false);
				statement = connection.prepareStatement(query);
				int iteratorIndex = 1;

				statement.setObject(iteratorIndex++, course.getUuid());
				statement.setObject(iteratorIndex++, course.getCourseName());
				statement.setObject(iteratorIndex++, course.getCourseDescription());

				statement.executeUpdate();
				connection.commit();
				
				synchronized (System.out) {
					System.out.println("Query executed:");
					System.out.println(statement.toString());
					System.out.println("Results:");
					System.out.println("1 row inserted.");
				}

			} catch (SQLException e) {
				rollbackTransaction(connection);
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

			synchronized (System.out) {
				System.out.println(statement.toString());
				System.out.println("Results:");

				Student student = null;
				int rowNo = 1;
				while (result.next()) {
					student = new Student();
					student.setUuid((String) result.getObject("uuid"));
					student.setGroupUuid((String) result.getObject("group_uuid"));
					student.setFirstName((String) (result.getObject("first_name")));
					student.setLastName((String) result.getObject("last_name"));
					resultStudents.add(student);

					System.out.println("RowNO  " + String.valueOf(rowNo++) + "  " + student.toString());
				}			
			}
		} catch (SQLException e) {
			rollbackTransaction(connection);
			e.printStackTrace();
		}
		finally {
			connectionPool.checkIn(connection);
			closeResultSet(result);
			closeStatemant(statement);
		}
		return resultStudents;
	}

	public void deleteStudent(String studentUuid) {
		CheckForNull.check(studentUuid);

		int result = 0;

		String queryStudentsOnCourse = " DELETE FROM " + EOL
				+ "students_on_course" + EOL
				+ " WHERE " + EOL
				+ "student_uuid" + EOL 
				+ " IN ( ? ); " + EOL;
			
		String queryStudents = " DELETE FROM " + EOL
				+ "student" + EOL 
				+ " WHERE" + EOL
				+ "uuid" + EOL 
				+ " IN ( ? ); " + EOL;

		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);

			statement = connection.prepareStatement(queryStudentsOnCourse);
			statement.setObject(1, studentUuid);
			result = statement.executeUpdate();

			synchronized (System.out) {
				System.out.println("Query executed:");
				System.out.println(statement.toString());
				System.out.println("Results:" + EOL);
				System.out.println(result + " rows deleted.");

				statement.close();

				statement = connection.prepareStatement(queryStudents);
				statement.setObject(1, studentUuid);
				result = statement.executeUpdate();

				System.out.println("Query executed:");
				System.out.println(statement.toString());
				System.out.println("Results:");
				System.out.println("1 row deleted.");
			}

			connection.commit();

		} catch (SQLException e) {
			rollbackTransaction(connection);
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
			closeStatemant(statement);
		}
	}

	public void findGroupsStudentCountLessOrEquals(int studentCount) {
		String query =  "SELECT " + EOL
				+ "group_1.uuid," + EOL
				+ "group_1.group_name,"
				+ "COUNT(*) AS \"Student Count\"" + EOL
				+ "FROM group_1" + EOL
				+ "INNER JOIN student" + EOL
				+ "ON  student.group_uuid = group_1.uuid" + EOL
				+ "GROUP BY group_1.uuid" + EOL
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
			
			synchronized (System.out) {
				System.out.println(statement.toString());
				System.out.println("Results:");

				Group group = null;
				int rowNo = 1;
				String curentStudentCount = "";
				while (result.next()) {
					group = new Group();
					group.setUuid(result.getString("uuid"));
					group.setGroupName(((String) (result.getObject("group_name"))));
					curentStudentCount = (((String) (result.getString("Student Count"))));

					System.out.println("RowNO  " + String.valueOf(rowNo++) + "  " + group.toString()
							+ " Student Count = " + curentStudentCount);
				}
			}
		} catch (SQLException e) {
			rollbackTransaction(connection);
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
			
			 synchronized (System.out) {
					System.out.println(statement.toString());
					System.out.println("Results:");

					Course course = null;
					int rowNo = 1;
					while (result.next()) {
						course = new Course();
						course.setUuid(result.getString("uuid"));
						course.setCourseName((String) (result.getObject("course_name")));
						course.setCourseDescription((String) result.getObject("course_description"));
						resultCourses.add(course);

						System.out.println("RowNO  " + String.valueOf(rowNo++) + "  " + course.toString());
					}
				}
			connection.commit();
			
		} catch (SQLException e) {
			rollbackTransaction(connection);
			e.printStackTrace();
		}
		finally {
			connectionPool.checkIn(connection);
			closeResultSet(result);
			closeStatemant(statement);
		}
		return resultCourses;
	}

	public void FindStudentsByCourseID(String courseUuid) {
		CheckForNull.check(courseUuid);
		
		String query =  "SELECT " + EOL
				+ "students_on_course.course_uuid, "+ EOL
				+ "student.uuid,"+ EOL
				+ "student.group_uuid," + EOL
				+ "student.first_name," + EOL
				+ "student.last_name" + EOL
				+ "FROM students_on_course" + EOL
				+ "INNER JOIN student" + EOL
				+ "ON student.uuid = students_on_course.student_uuid" + EOL
				+ "WHERE course_uuid IN ( ? );" + EOL;
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			statement.setObject(1, courseUuid);
			result = statement.executeQuery();
			
			synchronized (System.out) {
				System.out.println(statement.toString());
				System.out.println("Results:");

				Student student = null;
				int rowNo = 1;
				while (result.next()) {
					student = new Student();
					student.setUuid(result.getString("uuid"));
					student.setGroupUuid(result.getString("group_uuid"));
					student.setFirstName((String) (result.getObject("first_name")));
					student.setLastName((String) result.getObject("last_name"));

					System.out.println("RowNO  " + String.valueOf(rowNo++) + " " + " " + student.toString());
				}
			}
			connection.commit();
		} catch (SQLException e) {
			rollbackTransaction(connection);
			e.printStackTrace();
		}
		finally {
			connectionPool.checkIn(connection);
			closeResultSet(result);
			closeStatemant(statement);
		}
	}

	public void addStudentToCourse(String studentId, String courseId) {
		CheckForNull.check(studentId, courseId);

		int result = 0;
		String querySelect = "SELECT * FROM " + EOL
				+ "students_on_course " + EOL
				+ "WHERE " + EOL
				+ "student_uuid = ? " + EOL
				+ "AND" + EOL
				+ " course_uuid = ?;" + EOL;
		
		String queryInsert = "INSERT INTO " + EOL
				+ "students_on_course " + EOL
				+ "(student_uuid, course_uuid)" + EOL
				+ "VALUES " + EOL
				+ "( ?, ? ) ;" + EOL;
	
		Connection connection = null;
		PreparedStatement statement = null;
				
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(querySelect);
			statement.setObject(1, studentId);
			statement.setObject(2, courseId);
			ResultSet resultSet = statement.executeQuery();
			 
			synchronized (System.out) {
				System.out.println("Query executed:");
				System.out.println(statement.toString());
				System.out.println("Results:");

				if (resultSet.next()) {
					System.out.println("student_uuid = " + resultSet.getString("student_uuid"));
					System.out.println("course_uuid = " + resultSet.getString("course_uuid"));

					resultSet.close();
					connection.commit();
				} else {
					System.out.println("0 rows affected");

					statement.close();
					statement = connection.prepareStatement(queryInsert);
					statement.setObject(1, studentId);
					statement.setObject(2, courseId);

					result = statement.executeUpdate();

					System.out.println("Query executed:");
					System.out.println(statement.toString());
					System.out.println("Results:");
					System.out.println(result + " row inserted.");
				}

				connection.commit();
			}
		} catch (SQLException e) {
			rollbackTransaction(connection);
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
			closeStatemant(statement);
		}
	}
		
	public List<Course> findStudentCourses(String studentUuid) {
		CheckForNull.check(studentUuid);
		
		ResultSet result = null;
		List<Course> studentCourses = new ArrayList<Course>();
		
		String query = "SELECT " + EOL
				+ "course.uuid," + EOL
				+ "course.course_name," + EOL 
				+ "course.course_description," + EOL
				+ "students_on_course.student_uuid" + EOL
				+ "FROM" + EOL
				+ "course" + EOL
				+ "RIGHT JOIN" + EOL 
				+ "students_on_course" + EOL
				+ "ON" + EOL
				+ " course.uuid = students_on_course.course_uuid " + EOL
				+ "WHERE" + EOL
				+ " students_on_course.student_uuid = ? " + EOL; 

		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);

			statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			statement.setObject(1, studentUuid.toString());
			result = statement.executeQuery();

			synchronized (System.out) {
				System.out.println("Query executed:");
				System.out.println(statement.toString());
				System.out.println("Results:");

				if (result.first()) {
					Course course = null;
					int rowNo = 1;
					while (result.next()) {
						course = new Course();
						course.setUuid(result.getString("uuid"));
						course.setCourseName((String) (result.getObject("course_name")));
						course.setCourseDescription((String) result.getObject("course_description"));
						studentCourses.add(course);

						System.out.println("RowNO  " + String.valueOf(rowNo++) + "  " + course.toString());
					}
				}
			}
				connection.commit();
		} catch (SQLException e) {
			rollbackTransaction(connection);
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
			closeStatemant(statement);
		}
		return studentCourses;
	}

	public void dropoutStudentFromCourse(String studentUuid, String courseUuid) {
		CheckForNull.check(studentUuid, courseUuid);

		int result = 0;
		String query = "DELETE FROM" + EOL
				+ "students_on_course" + EOL
				 + "WHERE"
				 + " student_uuid = ? "
				 + "AND "
				 + "course_uuid = ? ;" + EOL;
	
		Connection connection = null;
		PreparedStatement statement = null;
				
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			statement.setObject(1, studentUuid.toString());
			statement.setObject(2, courseUuid.toString());
			
			result = statement.executeUpdate();

			synchronized (System.out) {
				System.out.println("Query executed:");
				System.out.println(statement.toString());
				System.out.println("Results:");
				System.out.println(result + " row deleted.");
			}

			connection.commit();
		} catch (SQLException e) {
			rollbackTransaction(connection);
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
			closeStatemant(statement);
		}
	}

	private void rollbackTransaction(Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void closeStatemant(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}

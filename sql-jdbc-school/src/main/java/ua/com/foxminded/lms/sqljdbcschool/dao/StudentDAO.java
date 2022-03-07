package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class StudentDAO extends BasicDAO<Student> {

	public StudentDAO(DBConnectionPool connectionPool) {
		super(connectionPool);
	}

	@Override
	public List<Student> getAll() {
		return get("", new ArrayList<String>());
	}

	@Override
	public List<Student> get(String conditions, List<String> params) {
		if ((conditions == null) || (params == null) ) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}

		String query = "SELECT " + EOL
				+ SQLName("id") + ", " + EOL 
				+ SQLName("groupId") + ", " + EOL
				+ SQLName("studentFirstName") + ", " + EOL
				+ SQLName("studentLastName") + EOL
				+ " FROM " + EOL
				+ SQLName("Student") + EOL;

		
		if (!conditions.isEmpty()) {
			query += " "  + conditions +";" + EOL;
		}

		ResultSet result = null;
		ArrayList<Student> resultStudents = new ArrayList<Student>();

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
			connection.commit();
			
			Student student = null;
			while (result.next()) {
				student = new Student();
				student.setId((UUID) result.getObject(SQLName("id")));
				student.setGroupId(nullToZeroAndVersa.apply((UUID) result.getObject(SQLName("groupId"))));
				student.setStudentFirstName((String) (result.getObject(SQLName("studentFirstName"))));
				student.setStudentLastName((String) result.getObject(SQLName("studentLastName")));
				resultStudents.add(student);
			}
			
			setQueryResultLog(statement, result);
			
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

	@Override
	public List<Student> getRange(int startRow, int endRow) {
		if (startRow > endRow || startRow <0 || endRow < 0) {
			throw new IllegalArgumentException("ERROR: Illegal arguments.");
		}
		
		String query = "SELECT * " + EOL
				+ " FROM " + EOL
				+ SQLName("Student") + EOL
				+ " LIMIT " + (endRow - startRow) + EOL
				+ " OFFSET " + startRow  + ";" + EOL;

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
			
			Student student = null;
			while (result.next()) {
				student = new Student();
				student.setId((UUID) result.getObject(SQLName("id")));
				student.setGroupId(nullToZeroAndVersa.apply((UUID) result.getObject(SQLName("groupId"))));
				student.setStudentFirstName((String) (result.getObject(SQLName("studentFirstName"))));
				student.setStudentLastName((String) result.getObject(SQLName("studentLastName")));
				resultStudents.add(student);
			}
			
			setQueryResultLog(statement, result);
			
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

	@Override
	public int getRowsCount() {
		int result = 0;
		ResultSet resultSet = null;

		String query = "SELECT COUNT(*) " + EOL
				+ " FROM " + EOL
				+ SQLName("Student") + ";" + EOL;
		
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
	
	@Override
	public int delete(List<Student> entities) {
		if ((entities == null) ) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}

		int result = 0;

		if (!entities.isEmpty()) {
		
		String queryStudentsOnCourse = " DELETE FROM " + EOL
					+ SQLName("StudentsOnCourse ")  + EOL
					+ " WHERE " + EOL
					+ SQLName("studentId") + EOL 
					+ " IN ("
					+ String.join(", ", Collections.nCopies(entities.size(), "?"))
					+ "); " + EOL;
			
		String queryStudents = " DELETE FROM " + EOL
				+ SQLName("Student") + EOL 
				+ " WHERE " + EOL
				+ SQLName("id") + EOL 
				+ " IN ("
				+ String.join(", ", Collections.nCopies(entities.size(), "?"))
				+ "); " + EOL;

		Connection connection = null;
		PreparedStatement statement =null;
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			
			statement = connection.prepareStatement(queryStudentsOnCourse);
			int iteratorIndex = 1;
			for (Student student : entities) {
				statement.setObject(iteratorIndex++ , student.getId());
			} 
			
			result = statement.executeUpdate();
			setQueryResultLog(statement, result);

			statement = connection.prepareStatement(queryStudents);
			iteratorIndex = 1;
			for (Student student : entities) {
				statement.setObject(iteratorIndex++ , student.getId());
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

	@Override
	public int deleteAll() {
		int result = 0;
		String queryStudentsOnCourse = " DELETE FROM " + EOL
				+ SQLName("StudentsOnCourse") + " ;" + EOL;
		String queryStudents = " DELETE FROM " + EOL
				+ SQLName("Student") + " ;" + EOL;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			
			statement = connection.prepareStatement(queryStudentsOnCourse);
			result = statement.executeUpdate();
			setQueryResultLog(statement, result);
			statement.close();
			
			statement = connection.prepareStatement(queryStudents);
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
	public int insert(List<Student> entities) {
		if (entities== null)  {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}
		
		int result = 0;
		if (!entities.isEmpty()) {
			String query = "INSERT INTO " + EOL 
				+ SQLName("Student") 
				+ " ( " + SQLName("id") + ", " + EOL
				+ SQLName("groupId") + ", " + EOL
				+ SQLName("studentFirstName") + ", " + EOL
				+ SQLName("studentLastName") + ") " + EOL
				+ " VALUES " + EOL
				+ String.join(", ", Collections.nCopies(
						entities.size(),
						"( ?, ?, ?, ? )")) 
				+ ";" + EOL;
			
			Connection connection = null;
			PreparedStatement statement = null; 
			try  {
				connection = connectionPool.checkOut();
				connection.setAutoCommit(false);
				statement = connection.prepareStatement(query);
				int iteratorIndex = 1;
				for (Student student : entities) {
					statement.setObject(iteratorIndex++, student.getId());
					statement.setObject(iteratorIndex++, nullToZeroAndVersa.apply(student.getGroupId()));
					statement.setObject(iteratorIndex++, student.getStudentFirstName());
					statement.setObject(iteratorIndex++, student.getStudentLastName());
				}

				result = statement.executeUpdate();
				connection.commit();
				
				setQueryResultLog(statement, result);
				
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
	public int update(List<Student> entities) {
			if (entities == null) {
				throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
			}

			int result = 0;
			if (entities.size()!=0) {
				
			String queryUpdateRow = " UPDATE " + SQLName("Student") + EOL 
					+ " SET " + EOL 
					+ SQLName("groupId") + " = ?, " + EOL
					+ SQLName("studentFirstName") + " = ?," + EOL
					+ SQLName("studentLastName") + " = ?" + EOL
					+ " WHERE " + EOL
					+ SQLName("id") + " = ? ;" + EOL;
			
			String queryUpdateRows = String.join("", Collections.nCopies(entities.size(), queryUpdateRow ));
		
			Connection connection = null;
			PreparedStatement statement = null;
			try {
				connection = connectionPool.checkOut();
				connection.setAutoCommit(false);
				statement = connection.prepareStatement(queryUpdateRows);
				int iteratorIndex = 1;

				for (Student student : entities) {
				statement.setObject(iteratorIndex++, nullToZeroAndVersa.apply(student.getGroupId()));					
				statement.setObject(iteratorIndex++, student.getStudentFirstName());
				statement.setObject(iteratorIndex++, student.getStudentLastName());
				statement.setObject(iteratorIndex++, student.getId());
				}
				
				result = statement.executeUpdate();
				connection.commit();
				
				setQueryResultLog(statement, result);
				
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

}

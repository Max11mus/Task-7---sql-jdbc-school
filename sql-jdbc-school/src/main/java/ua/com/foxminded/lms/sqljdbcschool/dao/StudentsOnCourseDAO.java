package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import ua.com.foxminded.lms.sqljdbcschool.entities.Course;
import ua.com.foxminded.lms.sqljdbcschool.entities.StudentsOnCourse;

public class StudentsOnCourseDAO extends TableDAO<StudentsOnCourse> {

	public StudentsOnCourseDAO(String DBName, Connection connection) throws SQLException {
		super(DBName);
		this.primaryKeysFields = getPrimaryKeysFields(connection);
	}

	@Override
	public int insertRow(StudentsOnCourse entity, Connection connection) throws SQLException {
		String columnsSQL = " ( " + String.join(", ", this.fieldsNames) + " ) ";
		String valuesSQL = " Values(?, ?);";
		String query = "INSERT INTO " + this.tableName + columnsSQL + valuesSQL;

		int result = 0;

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setObject(1, entity.getStudentId());
			statement.setObject(2, entity.getCourseId());
			
			result = statement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("insertRow failure.");
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int updateRow(StudentsOnCourse entity, Connection connection) throws SQLException {
		String setSQL = " SET " + String.join(" = ?, ", this.fieldsNames) + " = ? ";
		String whereSQL = " WHERE " + String.join(" = ? AND ", this.primaryKeysFields) + " = ? ";
		String query = " UPDATE " + this.tableName + setSQL + whereSQL;

		int result = 0;

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setObject(1, entity.getStudentId());
			statement.setObject(2, entity.getCourseId());
			statement.setObject(3, entity.getStudentId());
			statement.setObject(4, entity.getCourseId());

			result = statement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("updateRow failure.");
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int deleteOneRow(StudentsOnCourse entity, Connection connection) throws SQLException {
		String whereSQL = " WHERE " + String.join(" = ? AND ", this.primaryKeysFields) + " = ? ";
		String query = "DELETE FROM " + this.tableName + whereSQL;

		int result = 0;

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setObject(1, entity.getStudentId());
			statement.setObject(2, entity.getCourseId());
			
			result = statement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("deleteOneRow failure.");
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public ArrayList<StudentsOnCourse> getWhereConditionsJoinsAnd(HashSet<String> fieldsNames,
			ArrayList<String> whereCondition, ArrayList<String> whereValues, Connection connection)
			throws SQLException {
		String columnsSQL = String.join(", ", this.fieldsNames);
		String whereSQL = "";
		if (whereCondition.size() > 0) {
			whereSQL = " WHERE" + String.join("?, AND", whereCondition) + " ? ";
		}

		String query = "SELECT " + columnsSQL + " FROM " + this.tableName + whereSQL;

		ResultSet result = null;
		ArrayList<StudentsOnCourse> resultEntities = new ArrayList<StudentsOnCourse>();

		try (PreparedStatement statement = connection.prepareStatement(query)) {

			for (int i = 0; i < whereValues.size(); i++) {
				statement.setString(i + 1, whereValues.get(i));
			}

			result = statement.executeQuery();

			while (result.next()) {
				StudentsOnCourse studentsOnCourse = new StudentsOnCourse();
				studentsOnCourse.setStudentId((UUID) result.getObject("studentid"));
				studentsOnCourse.setCourseId((UUID) result.getObject("setcourseid"));

				resultEntities.add(studentsOnCourse);
			}
			result.close();

		} catch (SQLException e) {
			if (result != null) {
				result.close();
			}
			System.out.println("getPrimaryKeysFields failure.");
			e.printStackTrace();
			throw e;
		}
		return resultEntities;

	}

}

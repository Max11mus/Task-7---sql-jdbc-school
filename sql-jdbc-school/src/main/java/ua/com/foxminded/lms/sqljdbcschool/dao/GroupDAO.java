package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import ua.com.foxminded.lms.sqljdbcschool.entities.Group;

public class GroupDAO extends TableDAO<Group> {

	public GroupDAO(String DBName, Connection connection) throws SQLException {
		super(DBName);
		this.primaryKeysFields = getPrimaryKeysFields(connection);  
	}

	@Override
	public int insertRow(Group entity, Connection connection) throws SQLException {
		
		String columnsSQL = " ( " + String.join(", ", this.fieldsNames) + " ) ";
		String valuesSQL = " Values(?, ?);";
		String query = "INSERT INTO " + this.tableName + columnsSQL + valuesSQL ;
		
		int result = 0;

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setObject(1, entity.getId());
			statement.setString(2, entity.getGroupName());

			result = statement.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("insertRow failure.");
			e.printStackTrace();
			throw  e;
		}
		return result;
}

	@Override
	public int updateRow(Group entity, Connection connection) throws SQLException {
		
		String setSQL = " SET " + String.join(" = ?, ", this.fieldsNames) + " = ? ";
		String whereSQL = " WHERE " + String.join(" = ? AND ", this.primaryKeysFields) + " = ? ";
		String query = " UPDATE " + this.tableName + setSQL + whereSQL;
		
		int result = 0;
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setObject(1, entity.getId());
			statement.setString(2, entity.getGroupName());
			statement.setObject(3, entity.getId());

			result = statement.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("updateRow failure.");
			e.printStackTrace();
			throw  e;
		}
		return result;
	}

	@Override
	public int deleteOneRow(Group entity, Connection connection) throws SQLException {
		String whereSQL = " WHERE " + String.join(" = ? AND ", this.primaryKeysFields) + " = ? ";
		String query = "DELETE FROM " + this.tableName + whereSQL;
		
		int result = 0;

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setObject(1, entity.getId());
			
			result = statement.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("deleteOneRow failure.");
			e.printStackTrace();
			throw  e;
		}
		return result;
	}

	@Override
	public ArrayList<Group> getWhereConditionsJoinsAnd(HashSet<String> fieldsNames, ArrayList<String> whereCondition,
			ArrayList<String> whereValues, Connection connection) throws SQLException {
		String columnsSQL = String.join(", ", this.fieldsNames);
		String whereSQL ="";
		if (whereCondition.size() > 0) {
			whereSQL = " WHERE" + String.join("?, AND", whereCondition) + " ? ";	
		}
		
		String query = "SELECT " + columnsSQL + " FROM " + this.tableName + whereSQL;

		ResultSet result = null;
		ArrayList<Group> resultEntities = new ArrayList<Group>();
		
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			
			for (int i = 0; i < whereValues.size(); i++) {
				statement.setString(i+1, whereValues.get(i));
			}
			
			result = statement.executeQuery();
			
			while (result.next()) {
				Group group = new Group();
				group.setId((UUID) result.getObject("id"));
				group.setGroupName(result.getString("groupname"));
				resultEntities.add(group);
			}
			result.close();

		} catch (SQLException e) {
			if (result != null) {
				result.close();
			}
			System.out.println("getPrimaryKeysFields failure.");
			e.printStackTrace();
			throw  e;
		}
		return resultEntities ;
	}

}

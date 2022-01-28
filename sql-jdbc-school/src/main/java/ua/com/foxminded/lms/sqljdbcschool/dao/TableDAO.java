package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class TableDAO<Entity extends Object> {
	protected HashSet<String> fieldsNames;
	protected String tableName;
	protected String DBName;
	protected HashSet<String> primaryKeysFields;
	
	public TableDAO(String DBName) {
		Class<Entity> persistentClass = (Class<Entity>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0]; // Geting type of a generic parameter Entity

		this.DBName = DBName;

		fieldsNames = new HashSet<String>();
		
		primaryKeysFields=null;

		tableName = (persistentClass.getName().replace(persistentClass.getPackage().getName() + ".", "") + "s")
				.toLowerCase();

		for (Field field : persistentClass.getDeclaredFields()) {
			fieldsNames.add(field.getName().toLowerCase());
		}

		if (fieldsNames.isEmpty()) {
			throw new IllegalStateException("Entity must contain at least one field !!!");
		}
	}

	abstract public int insertRow(Entity entity, Connection connection) throws SQLException;

	abstract public int updateRow(Entity entity, Connection connection) throws SQLException;

	abstract public int deleteOneRow(Entity entity, Connection connection) throws SQLException;

	abstract public ArrayList<Entity> getWhereConditionsJoinsAnd(HashSet<String> fieldsNames, ArrayList<String> whereCondition,
			ArrayList<String> whereValues, Connection connection) throws SQLException;

	protected HashSet<String> getPrimaryKeysFields(Connection connection) throws SQLException {
				
		String query = "SELECT kcu.column_name "
				+ "FROM information_schema.tables t "
				+ "LEFT JOIN information_schema.table_constraints tc "
				+ "       ON tc.table_catalog = t.table_catalog "
				+ "      AND tc.table_schema = t.table_schema "
				+ "      AND tc.table_name = t.table_name "
				+ "      AND tc.constraint_type = 'PRIMARY KEY' "
				+ "LEFT JOIN information_schema.key_column_usage kcu "
				+ "       ON kcu.table_catalog = tc.table_catalog "
				+ "      AND kcu.table_schema = tc.table_schema "
				+ "      AND kcu.table_name = tc.table_name "
				+ "      AND kcu.constraint_name = tc.constraint_name "
				+ "WHERE     t.table_catalog = ? "
				+ "      AND t.table_name = ? ;"; // Return columns names of the
													// PRIMARY KEY.
													// Only for POSTGRESQL

		ResultSet result = null;
		HashSet<String> primaryKeysFields= new HashSet<String>();

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, DBName);
			statement.setString(2, tableName);

			result = statement.executeQuery();
			
			while (result.next()) {
				primaryKeysFields.add(result.getString(1));
	            System.out.println(result.getString(1));
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
		return primaryKeysFields;
	}

	public HashSet<String> getFieldsNames() {
		return fieldsNames;
	}

	public void setFieldsNames(HashSet<String> fieldsNames) {
		this.fieldsNames = fieldsNames;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDBName() {
		return DBName;
	}

	public void setDBName(String dBName) {
		DBName = dBName;
	}

	public HashSet<String> getPrimaryKeysFields() {
		return primaryKeysFields;
	}

	public void setPrimaryKeysFields(HashSet<String> primaryKeysFields) {
		this.primaryKeysFields = primaryKeysFields;
	}

}

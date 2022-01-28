package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;

import ua.com.foxminded.lms.sqljdbcschool.utils.SQLTitle;

public abstract class TableDAO<EntityBean extends Object> { // abstract class thats implements base CRUD operation with
															// DB table 
	protected HashSet<String> fieldsNames;
	protected String tableName;
	protected String DBName;
	protected HashSet<String> primaryKeysFields;
	protected SQLTitle SQLName = new SQLTitle();

	public TableDAO(String DBName, Connection connection) throws SQLException {
		this.DBName = DBName;
		fieldsNames = new HashSet<String>();
		primaryKeysFields = null;
				
		Class<EntityBean> persistentClass = (Class<EntityBean>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0]; // Geting type of a generic parameter Entity
	
		tableName = persistentClass.getName().replace(persistentClass.getPackage().getName() + ".", "");

		for (Field field : persistentClass.getDeclaredFields()) {
			fieldsNames.add(field.getName());
		}

		if (fieldsNames.isEmpty()) {
			throw new IllegalStateException("EntityBean must contain at least one field !!!");
		}

		this.primaryKeysFields = getPrimaryKeysFields(connection);
	}

	public int insert(EntityBean entity, Connection connection) throws SQLException {
		String columnsSQL = " ( " + String.join(", ",
				this.fieldsNames.stream().map(s -> SQLName.convert(s)).collect(Collectors.toList()))
				+ " ) ";
		String valuesSQL = " Values("
				+ String.join(", ", this.fieldsNames.stream().map(s -> "?").collect(Collectors.toList())) + ");";
		String query = "INSERT INTO " + SQLName.convert(this.tableName) + columnsSQL + valuesSQL;

		int result = 0;

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			Object statementParam = null;
			int iteratorIndex = 1;
			for (Iterator<String> iterator = fieldsNames.iterator(); iterator.hasNext();) {
				String fieldsName = (String) iterator.next();
				statementParam = callGetter(entity, fieldsName);
				statement.setObject(iteratorIndex++, statementParam);
			}
			result = statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public int update(EntityBean entity, Connection connection) throws SQLException {
		String setSQL = " SET " + String.join(", ", this.fieldsNames.stream()
				.map(s -> SQLName.convert(s) + " = ? ").collect(Collectors.toList()));
		String whereSQL = " WHERE " + String.join(" AND ", this.primaryKeysFields.stream()
				.map(s -> SQLName.convert(s) + " = ? ").collect(Collectors.toList())) + ";";
		String query = " UPDATE " + SQLName.convert(this.tableName) + setSQL + whereSQL;

		int result = 0;

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			Object statementParam = null;
			int iteratorIndex = 1;
			for (Iterator<String> iterator = fieldsNames.iterator(); iterator.hasNext();) {
				String fieldsName = (String) iterator.next();
				statementParam = callGetter(entity, fieldsName);
				statement.setObject(iteratorIndex++, statementParam);
			}

			statementParam = null;
			for (Iterator<String> iterator = primaryKeysFields.iterator(); iterator.hasNext();) {
				String fieldsName = (String) iterator.next();
				statementParam = callGetter(entity, fieldsName);
				statement.setObject(iteratorIndex++, statementParam);
			}

			result = statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public int deleteOneRow(EntityBean entity, Connection connection) throws SQLException {
		String whereSQL = " WHERE " + String.join(" AND ", this.primaryKeysFields.stream()
				.map(s -> SQLName.convert(s) + " = ? ").collect(Collectors.toList())) + ";";
		String query = "DELETE FROM " + SQLName.convert(this.tableName) + whereSQL;

		int result = 0;

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			Object statementParam = null;
			int iteratorIndex = 1;

			for (Iterator<String> iterator = primaryKeysFields.iterator(); iterator.hasNext();) {
				String fieldsName = (String) iterator.next();
				statementParam = callGetter(entity, fieldsName);
				statement.setObject(iteratorIndex++, statementParam);
			}
			result = statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public ArrayList<EntityBean> getAll(Connection connection) throws SQLException  {
		return get(this.fieldsNames, new ArrayList<String>(), new ArrayList<String>(), connection);
	}
	
	public ArrayList<EntityBean> get(HashSet<String> fieldsNames, ArrayList<String> whereCondition,
			ArrayList<String> whereValues, Connection connection) throws SQLException {
		String columnsSQL = String.join(", ",
				this.fieldsNames.stream().map(s -> SQLName.convert(s)).collect(Collectors.toList()));
		String whereSQL = ";";
		if (whereCondition.size() > 0) {
			whereSQL = " WHERE" + String.join(" AND ", whereCondition.stream()
					.map(s -> SQLName.convert(s) + " ? ").collect(Collectors.toList())) + ";";
		}

		String query = "SELECT " + columnsSQL + " FROM " + SQLName.convert(this.tableName) + whereSQL;

		ResultSet result = null;
		ArrayList<EntityBean> resultEntities = new ArrayList<EntityBean>();

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			for (int i = 0; i < whereValues.size(); i++) {
				statement.setString(i + 1, whereValues.get(i));
			}

			result = statement.executeQuery();
			
			Class<EntityBean> persistentClass = (Class<EntityBean>) ((ParameterizedType) getClass().getGenericSuperclass())
						.getActualTypeArguments()[0]; // Geting type of a generic parameter Entity
			EntityBean entity = null;

			while (result.next()) {
				try {
					entity = persistentClass.getDeclaredConstructor().newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				
				for (Iterator<String> iterator = fieldsNames.iterator(); iterator.hasNext();) {
					String fieldsName = (String) iterator.next();
					callSettter(entity, fieldsName, result.getObject(SQLName.convert(fieldsName)));
				}
				resultEntities.add(entity);
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

	protected HashSet<String> getPrimaryKeysFields(Connection connection) throws SQLException {
		String query = "SELECT kcu.column_name " + "FROM information_schema.tables t "
				+ "LEFT JOIN information_schema.table_constraints tc " + "       ON tc.table_catalog = t.table_catalog "
				+ "      AND tc.table_schema = t.table_schema " + "      AND tc.table_name = t.table_name "
				+ "      AND tc.constraint_type = 'PRIMARY KEY' " + "LEFT JOIN information_schema.key_column_usage kcu "
				+ "       ON kcu.table_catalog = tc.table_catalog " + "      AND kcu.table_schema = tc.table_schema "
				+ "      AND kcu.table_name = tc.table_name " + "      AND kcu.constraint_name = tc.constraint_name "
				+ "WHERE     t.table_catalog = ? " + "      AND t.table_name = ? ;"; // Return columns names of the
																						// PRIMARY KEY.
																						// Only for POSTGRESQL
		ResultSet result = null;
		HashSet<String> primaryKeysFields = new HashSet<String>();

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, DBName);
			statement.setString(2, SQLName.convert(tableName));

			result = statement.executeQuery();

			while (result.next()) {
				primaryKeysFields.add(result.getString(1));
			}
			result.close();

		} catch (SQLException e) {
			if (result != null) {
				result.close();
			}
		e.printStackTrace();
			throw e;
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

	protected Object callGetter(EntityBean entity, String fieldName) {
		Object variableValue = null;
		try {
			PropertyDescriptor objPropertyDescriptor = new PropertyDescriptor(fieldName, entity.getClass());
			variableValue = objPropertyDescriptor.getReadMethod().invoke(entity);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			e.printStackTrace();
		}
		return variableValue;
	}

	protected void callSettter(EntityBean entity, String fieldName, Object value) {
		try {
			PropertyDescriptor objPropertyDescriptor = new PropertyDescriptor(fieldName, entity.getClass());
			objPropertyDescriptor.getWriteMethod().invoke(entity, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			e.printStackTrace();
		}
	}

}

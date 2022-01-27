package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.util.ArrayList;

abstract class ObjectDAO<Entity> implements CRUD<Entity> {
	protected String query;
	protected Connection connection;
	protected String tableName;
	protected ArrayList<String> columnNames;
	
	

	public ObjectDAO(Connection connection) {
		query = "";
		tableName = "";
		columnNames = new ArrayList<String>();
		this.connection = connection;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ArrayList<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(ArrayList<String> columnNames) {
		this.columnNames = columnNames;
	}
	
}
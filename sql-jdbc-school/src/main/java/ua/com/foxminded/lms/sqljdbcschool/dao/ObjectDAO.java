package ua.com.foxminded.lms.sqljdbcschool.dao;

import javax.sql.DataSource;

abstract class ObjectDAO<Entity>  implements CRUD<Entity> {
	private String query;
	private DataSource datasource;
	
	

	public ObjectDAO(DataSource datasource) {
		query="";
		this.datasource=datasource;
	}

	public void ExecuteSQL() {
		
	}

	

}
package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.SQLException;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Squad;

public class SquadDAO extends TableDAO<Squad> {

	public SquadDAO(String DBName, Connection connection) throws SQLException {
		super(DBName, connection);
		// TODO Auto-generated constructor stub
	}

}

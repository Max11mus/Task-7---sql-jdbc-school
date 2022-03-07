package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;

public class GroupDAO extends BasicDAO<Group> {

	public GroupDAO(DBConnectionPool connectionPool) {
		super(connectionPool);
		}

	@Override
	public List<Group> getAll(){
 	return get("", new ArrayList<String>());
	}

	@Override
	public List<Group> get(String conditions, List<String> params){
		if (conditions == null || params == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}

		String query = "SELECT " + EOL
				+ SQLName("id") + ", " + EOL
				+ SQLName("groupName") + EOL
				+ " FROM " + EOL
				+ SQLName("Group") + EOL;
		
		if (!conditions.isEmpty()) {
			query +=  conditions +";" + EOL;
		}

		ArrayList<Group> resultGroups = new ArrayList<Group>();
		ResultSet result = null;
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
			
			Group group = null;
			while (result.next()) {
				group = new Group();
				group.setId((UUID) result.getObject(SQLName("id")));
				group.setGroupName((String) (result.getObject(SQLName("groupName"))));
				resultGroups.add(group);
			}
			setQueryResultLog(statement, result);
			
		} catch (SQLException e) {
			e.printStackTrace();
			rollbackConnection(connection);
		}
		finally {
			closeResultSet(result);
			closeStatemant(statement);
			connectionPool.checkIn(connection);
		}
		return resultGroups;
	}

	@Override
	public List<Group> getRange(int startRow, int endRow) {
			if (startRow > endRow || startRow <0 || endRow < 0) {
				throw new IllegalArgumentException("ERROR: Illegal arguments.");
			}

			String query = "SELECT * " + EOL
					+ " FROM " + EOL
					+ SQLName("Group") + EOL
					+ " LIMIT " + (endRow - startRow) + EOL
					+ " OFFSET " + startRow  + ";" + EOL;

			ArrayList<Group> resultGroups = new ArrayList<Group>();
			ResultSet result = null;
			Connection connection = null;
			PreparedStatement statement = null;
			try {
				connection = connectionPool.checkOut();
				connection.setAutoCommit(false);
				
				statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				
				result = statement.executeQuery();
				connection.commit();
				
				Group group = null;
				while (result.next()) {
					group = new Group();
					group.setId((UUID) result.getObject(SQLName("id")));
					group.setGroupName((String) (result.getObject(SQLName("groupName"))));
					resultGroups.add(group);
				}
				setQueryResultLog(statement, result);
				
			} catch (SQLException e) {
				e.printStackTrace();
				rollbackConnection(connection);
			}
			finally {
				closeResultSet(result);
				closeStatemant(statement);
				connectionPool.checkIn(connection);
			}
			return resultGroups;		
	}
	
	@Override
	public int getRowsCount() {
		int result = 0;
		ResultSet resultSet = null;

		String query = "SELECT COUNT(*) " + EOL
				+ " FROM " + EOL
				+ SQLName("Group") + ";" + EOL;
		
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
	public int delete(List<Group> entities) {
		if ((entities == null)) {
				throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
			}
			int result = 0;
			if (!entities.isEmpty()) {
			
			String query = " DELETE FROM " + EOL
					+ SQLName("Group") + EOL
					+ " WHERE " + EOL
					+ SQLName("id") + EOL
					+ " IN ("
					+ String.join(", ", Collections.nCopies(entities.size(), "?"))
					+ ");" + EOL; 
			
			Connection connection = null;
			PreparedStatement statement = null;
			
			try {
				connection = connectionPool.checkOut();
				connection.setAutoCommit(false);
				statement = connection.prepareStatement(query);

				int iteratorIndex = 1;
				for (Group group : entities) {
					statement.setObject(iteratorIndex++, group.getId());
				}
				
				result = statement.executeUpdate();
				connection.commit();
				
				setQueryResultLog(statement, result);

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
		String query = " DELETE FROM " + EOL
				+ SQLName("Group") + " ;" + EOL;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = connectionPool.checkOut();
			connection.setAutoCommit(false);
			statement = connection.prepareStatement(query);
			
			result = statement.executeUpdate();
			connection.commit();
			
			setQueryResultLog(statement, result);

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
	public int insert(List<Group> entities) {
			if (entities== null)  {
				throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
			}
			
			int result = 0;
			if (!entities.isEmpty()) {
				String query = "INSERT INTO " + EOL
					+ SQLName("Group") + EOL
					+ " ( " + SQLName("id") + ", " + EOL
					+ SQLName("groupName") + " ) " + EOL
					+ " VALUES " + EOL
					+ String.join(", ", Collections.nCopies(
							entities.size(),
							"( ?, ? )")) 
					+ ";" + EOL;
				
				Connection connection = null;
				PreparedStatement statement = null;
				try {
					connection = connectionPool.checkOut();
					connection.setAutoCommit(false);
					statement = connection.prepareStatement(query);
					int iteratorIndex = 1;
					for (Group group : entities) {
						statement.setObject(iteratorIndex++, group.getId());
						statement.setObject(iteratorIndex++, group.getGroupName());
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
	public int update(List<Group> entities) {
		if (entities == null) {
				throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
			}

			int result = 0;
			if (entities.size()!=0) {
				
			String queryUpdateRow = " UPDATE " + SQLName("Group") 
					+ " SET " 
					+ SQLName("groupName") + " = ?"
					+ " WHERE " 
					+ SQLName("id") + " = ? ;" ;
			
			String queryUpdateRows = String.join("", Collections.nCopies(entities.size(), queryUpdateRow ));
			
			PreparedStatement statement = null;
			Connection connection = null;
			
			try {
				connection = connectionPool.checkOut();
				connection.setAutoCommit(false);
				statement = connection.prepareStatement(queryUpdateRows);
						
				int iteratorIndex = 1;
				for (Group group : entities) {
				statement.setObject(iteratorIndex++, group.getGroupName());
				statement.setObject(iteratorIndex++, group.getId());
				}
				
				result = statement.executeUpdate();
				setQueryResultLog(statement, result);
				connection.commit();
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

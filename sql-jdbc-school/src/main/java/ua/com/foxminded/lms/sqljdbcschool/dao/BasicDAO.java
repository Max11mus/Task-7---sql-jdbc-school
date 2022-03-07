package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.utils.SQLQueryUtils;

public abstract class BasicDAO<EntityBean extends Object> {
	static protected String EOL = System.lineSeparator();
	protected DBConnectionPool connectionPool;
	protected String queryResultLog="";
	boolean enableLogging = false;
	protected BiFunction<Integer, String, String> nCopies = (n, s) -> String.join("", Collections.nCopies(n,s)); 
	protected Function<UUID, UUID> nullToZeroAndVersa = (uuid) -> {
		UUID result = uuid;
		if (uuid == null) {
			result = UUID.fromString("00000000-0000-0000-0000-000000000000");
		} else 
		if (uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
			result = null;
		}
		return result;
	};
	
	public BasicDAO(DBConnectionPool connectionPool) {
		super();
		this.connectionPool = connectionPool;
	}

	public abstract List<EntityBean> getAll();

	public abstract List<EntityBean> get(String conditions, List<String> params);
	
	public abstract List<EntityBean> getRange(int startRow, int endRow);
	
	public abstract int getRowsCount();

	public abstract int delete(List<EntityBean> entities);
	
	public abstract int deleteAll();

	public abstract int insert(List<EntityBean> entities);

	public abstract int update(List<EntityBean> entities);
	
	public String getQueryResultLog() {
		return queryResultLog;
	}

	public boolean isEnableLogging() {
		return enableLogging;
	}

	public void setEnableLogging(boolean enableLogging) {
		this.enableLogging = enableLogging;
	}

	public void executeQuery(String query, List<String> params) {
		if (query == null || params == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
		}

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

				setQueryResultLog(statement, result);

			} catch (SQLException e) {
				rollbackConnection(connection);
				e.printStackTrace();
			} finally {
				connectionPool.checkIn(connection);
				closeResultSet(result);
				closeStatemant(statement);
			}
		}
	
	
	protected void setQueryResultLogAppend(Statement statement, ResultSet resultSet) throws SQLException {
		if (enableLogging) {
			String oldQueryResult = queryResultLog;
			setQueryResultLog(statement, resultSet);
			queryResultLog = oldQueryResult + queryResultLog;
		}
	}
	
	protected void setQueryResultLogAppend(Statement statement, int result) throws SQLException {
		if (enableLogging) {
			String oldQueryResult = queryResultLog;
			setQueryResultLog(statement, result);
			queryResultLog = oldQueryResult + EOL + queryResultLog;
		}
	}
	
	protected void setQueryResultLog( Statement statement, ResultSet resultSet) throws SQLException {
		if (enableLogging) {
			queryResultLog = EOL;
			queryResultLog += "Query executed:" + EOL;
			queryResultLog += statement.toString() + EOL;

			queryResultLog += "Results:" + EOL;
			ResultSetMetaData metaData = resultSet.getMetaData();
			String resultHeader = "";
			int columnLabelWith = 50;
			String rowNoLabel = "|RowNo";
			resultHeader = nCopies.apply(metaData.getColumnCount() * columnLabelWith + rowNoLabel.length(), "-") + EOL;

			resultHeader += rowNoLabel;
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				resultHeader += "|" + metaData.getColumnLabel(i);
				resultHeader += nCopies.apply(columnLabelWith - metaData.getColumnLabel(i).length() - 1, " ");
			}
			resultHeader = resultHeader.substring(0, resultHeader.length() - 1) + "|" + EOL;
			resultHeader += nCopies.apply(metaData.getColumnCount() * columnLabelWith + rowNoLabel.length(), "-") + EOL;

			queryResultLog += resultHeader;

			if (resultSet.first()) {
				resultSet.beforeFirst();
				String rows = "";
				while (resultSet.next()) {
					rows += "|" + resultSet.getRow();
					rows += nCopies.apply(rowNoLabel.length() - String.valueOf(resultSet.getRow()).length() - 1, " ");

					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						if (resultSet.getString(i) != null) {
							rows += "|" + resultSet.getString(i);
							rows += nCopies.apply(columnLabelWith - resultSet.getString(i).length() - 1, " ");
						} else {
							rows += "|NULL";
							rows += nCopies.apply(columnLabelWith - 5, " ");
						}
					}

					rows = rows.substring(0, rows.length() - 1) + "|" + EOL;
				}
				queryResultLog += rows;
				queryResultLog += nCopies.apply(metaData.getColumnCount() * columnLabelWith + rowNoLabel.length(), "-")
						+ EOL;
				resultSet.beforeFirst();
			}
		}
	}
	
	protected void setQueryResultLog(Statement statement, int result) {
		if (enableLogging) {
			queryResultLog = "Query executed:" + EOL;
			queryResultLog += statement.toString() + EOL;
			queryResultLog += "Results:" + EOL;
			queryResultLog += result + " rows affected.";
		}
	}
	
	protected static String SQLName(String camelCaseName) {
		return SQLQueryUtils.convert(camelCaseName);
	};
	
	protected void rollbackConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void closeStatemant(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}

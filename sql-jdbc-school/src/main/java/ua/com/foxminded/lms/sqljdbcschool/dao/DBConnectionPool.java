package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class DBConnectionPool {
	private long expirationTime;
	private Hashtable<Connection, Long> locked; 
	private Hashtable<Connection, Long> unlocked;
	private Properties properties;

	public DBConnectionPool(Properties properties, int initialSize) throws SQLException {
		this(properties);
		ArrayList<Connection> temp = new ArrayList<Connection>() ;
		for (int i = 0; i<initialSize; i++) {
			temp.add(checkOut());
		}
		
		for (Iterator<Connection> iterator = temp.iterator(); iterator.hasNext();) {
			Connection connection = (Connection) iterator.next();
			checkIn(connection);
		}
		
	}
	
	public DBConnectionPool(Properties properties) {
		expirationTime = 360000; // 360 milliseconds - 360 seconds
		locked = new Hashtable<Connection, Long>();
		unlocked = new Hashtable<Connection, Long>();
		this.properties = properties;
	}

	protected Connection create() throws SQLException {
		try  {
			Connection connection = DriverManager.getConnection(getUrl() + getDBname(), getUser(), getPassword());
			return (connection);
		} catch (SQLException e) {
			System.out.println("SQL EXCEPTION: Can't create DB Connection !!! ");
			e.printStackTrace();
			throw e;
		}
	}

	public void expire(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean validate(Connection connection) {
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return (false);
		}
	}

	public synchronized Connection checkOut() throws SQLException {
		long now = System.currentTimeMillis();
		Connection connection;
		if (unlocked.size() > 0) {
			Enumeration<Connection> unlockedConnections = unlocked.keys();
			while (unlockedConnections.hasMoreElements()) {
				connection = unlockedConnections.nextElement();
				if ((now - unlocked.get(connection)) > expirationTime) {
					// object has expired
					unlocked.remove(connection);
					expire(connection);
					connection = null;
				} else {
					if (validate(connection)) {
						unlocked.remove(connection);
						locked.put(connection, now);
						return (connection);
					} else {
						// object failed validation
						unlocked.remove(connection);
						expire(connection);
						connection = null;
					}
				}
			}
		}
		// no objects available, create a new one
		connection = create();
		locked.put(connection, now);
		return (connection);
	}

	public synchronized void checkIn(Connection t) {
		locked.remove(t);
		unlocked.put(t, System.currentTimeMillis());
	}

	public String getUrl() {
		return properties.getProperty("url");
	}
	
	public String getDBname() {
		return properties.getProperty("dbname");
	}

	public String getUser() {
		return properties.getProperty("user");
	}

	public String getPassword() {
		return properties.getProperty("password");
	}
	
	public void closeConnections() throws SQLException{
		
		Iterator<Entry<Connection, Long>> iterator = locked.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Connection, Long> entry = (Map.Entry<Connection, Long>) iterator.next();
			entry.getKey().close();
		}
		
		iterator = unlocked.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Connection, Long> entry = (Map.Entry<Connection, Long>) iterator.next();
			entry.getKey().close();
		}
		
	}
}
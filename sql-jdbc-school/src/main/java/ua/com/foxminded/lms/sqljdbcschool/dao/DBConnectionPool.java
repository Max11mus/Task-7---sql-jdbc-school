package ua.com.foxminded.lms.sqljdbcschool.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public class DBConnectionPool {
	private long expirationTime;
	private Hashtable<Connection, Long> locked;
	private Hashtable<Connection, Long> unlocked;
	private Properties properties;

	public DBConnectionPool(Properties properties) {
		expirationTime = 100000; // 100 seconds
		locked = new Hashtable<Connection, Long>();
		unlocked = new Hashtable<Connection, Long>();
		this.properties = properties;
	}

	protected Connection create() throws SQLException {
		try  {
			Connection connection = DriverManager.getConnection(getUrl(), getUser(), getPassword());
			return (connection);
		} catch (SQLException e) {
			System.out.println("SQL EXCEPTION: Can't create DB Connection !!! ");
			e.printStackTrace();
			throw e;
		}
	}

	public void expire(Connection o) {
		try {
			o.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean validate(Connection o) {
		try {
			return (!((Connection) o).isClosed());
		} catch (SQLException e) {
			e.printStackTrace();
			return (false);
		}
	}

	public synchronized Connection checkOut() throws SQLException {
		long now = System.currentTimeMillis();
		Connection t;
		if (unlocked.size() > 0) {
			Enumeration<Connection> e = unlocked.keys();
			while (e.hasMoreElements()) {
				t = e.nextElement();
				if ((now - unlocked.get(t)) > expirationTime) {
					// object has expired
					unlocked.remove(t);
					expire(t);
					t = null;
				} else {
					if (validate(t)) {
						unlocked.remove(t);
						locked.put(t, now);
						return (t);
					} else {
						// object failed validation
						unlocked.remove(t);
						expire(t);
						t = null;
					}
				}
			}
		}
		// no objects available, create a new one
		t = create();
		locked.put(t, now);
		return (t);
	}

	public synchronized void checkIn(Connection t) {
		locked.remove(t);
		unlocked.put(t, System.currentTimeMillis());
	}

	public String getUrl() {
		return properties.getProperty("url");
	}

	public String getUser() {
		return properties.getProperty("user");
	}

	public String getPassword() {
		return properties.getProperty("password");
	}
}
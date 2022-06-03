package DBTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.lms.sqljdbcschool.dao.DBConnectionPool;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

class DBUnitTestInsertGroups extends DataSourceBasedDBTestCase {
	Properties dbProperties;
	DBConnectionPool pool;
	SchoolDAO dao;

	public DBUnitTestInsertGroups() throws IOException {
		super();
		FileLoader fileLoader = new FileLoader();
		dbProperties = new Properties();
		dbProperties.load(fileLoader.loadProperties(ClassLoader.getSystemResource("db.h2.properties")));
		pool = new DBConnectionPool(dbProperties);
		dao = new SchoolDAO(pool);
	}

	@Override
	protected DataSource getDataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL(dbProperties.getProperty("url") + dbProperties.getProperty("dbname"));
		dataSource.setUser(dbProperties.getProperty("user"));
		dataSource.setPassword(dbProperties.getProperty("password"));
		return dataSource;
	}

	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(getClass().getClassLoader().getResourceAsStream("data.xml"));
	}

	@Override
	protected DatabaseOperation getTearDownOperation() throws Exception {
		return DatabaseOperation.DELETE_ALL;
	}

	@Test
	void testInsertGroups() throws Exception {

		IDataSet databaseDataSet = getConnection().createDataSet();

		List<Group> groups = new ArrayList<Group>();
		Group group = new Group();
		group.setUuid("1395df38-f289-41ac-ad5d-29355f320e9a");
		group.setGroupName("TK-79");
		groups.add(group);

		group = new Group();
		group.setUuid("0f7ac00c-9cee-4b68-8d2d-1268d8ee2752");
		group.setGroupName("KG-21");
		groups.add(group);

		dao.insertGroups(groups);

		ITable actualTable = databaseDataSet.getTable("GROUP_1");

		IDataSet expectedDataSet = new FlatXmlDataSet(
				getClass().getClassLoader().getResourceAsStream("expectedAfterInsertGroups.xml"));
		ITable expectedTable = expectedDataSet.getTable("GROUP_1");

		assertEquals(expectedTable.getRowCount(), actualTable.getRowCount());
		assertEquals(expectedTable.getTableMetaData().getColumns().length,
				actualTable.getTableMetaData().getColumns().length);

		int columnCount = expectedTable.getTableMetaData().getColumns().length;
		int rowCount = expectedTable.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				assertEquals(
						expectedTable.getValue(i, expectedTable.getTableMetaData().getColumns()[j].getColumnName())
								.toString(),
						actualTable.getValue(i, actualTable.getTableMetaData().getColumns()[j].getColumnName())
								.toString());
			}
		}

	}
}

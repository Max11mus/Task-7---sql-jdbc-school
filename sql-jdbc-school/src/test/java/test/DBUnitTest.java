package test;

import javax.sql.DataSource;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DBUnitTest extends DataSourceBasedDBTestCase {
	@Override
	protected DataSource getDataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL("jdbc:h2:mem:default;DB_CLOSE_DELAY=-1;init=runscript from 'classpath:schema.sql'");
		dataSource.setUser("sa");
		dataSource.setPassword("sa");
		return dataSource;
    }
	
	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(getClass().getClassLoader()
		          .getResourceAsStream("data.xml"));
	}

	
//	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
//	}
//
//	@Test
//	void testInsertGroups() throws Exception {
//		 	IDataSet expectedDataSet = getDataSet();
//		    ITable expectedTable = expectedDataSet.getTable("Group");
//		    IDataSet databaseDataSet = getConnection().createDataSet();
//		    ITable actualTable = databaseDataSet.getTable("CLIENTS");
//		    assertEquals(expectedTable, actualTable);
//	}
//
//	@Test
//	void testInsertGroup() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testInsertStudents() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testInsertStudent() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testInsertCourses() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetAllStudents() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testDeleteStudent() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testFindGroupsStudentCountLessOrEquals() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetAllCourses() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testFindStudentsByCourseID() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testAddStudentToCourse() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testFindStudentCourses() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testDropoutStudentFromCourse() {
//		fail("Not yet implemented");
//	}

	
}

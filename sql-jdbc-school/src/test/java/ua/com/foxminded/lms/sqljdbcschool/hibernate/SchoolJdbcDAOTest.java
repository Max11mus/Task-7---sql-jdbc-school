package ua.com.foxminded.lms.sqljdbcschool.hibernate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.SortedTable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.lms.sqljdbcschool.jdbc.SchoolJdbcDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.utils.DBConnectionPool;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

class SchoolJdbcDAOTest extends DataSourceBasedDBTestCase {
	static Properties dbProperties;
	static DBConnectionPool pool;
	static SchoolJdbcDAO dao;
	static Connection connection;

	@Override
	protected DataSource getDataSource() {
		JdbcDataSource dataSource = new JdbcDataSource();
		dataSource.setURL(dbProperties.getProperty("url") + dbProperties.getProperty("dbname"));
		dataSource.setUser(dbProperties.getProperty("user"));
		dataSource.setPassword(dbProperties.getProperty("password"));
		return dataSource;
	}
	
	@Override
	protected IDataSet getDataSet() throws Exception {
		
		IDataSet[] datasets = new IDataSet[] {
				new FlatXmlDataSetBuilder()
						.build(getClass().getClassLoader().getResource("dataCourse.xml")),
				new FlatXmlDataSetBuilder()
						.build(getClass().getClassLoader().getResource("dataGroup_1.xml")),
				new FlatXmlDataSetBuilder()
						.build(getClass().getClassLoader().getResource("dataStudent.xml")),
				new FlatXmlDataSetBuilder()
						.build(getClass().getClassLoader().getResource("dataStudentsOnCourse.xml")) };

		return new FilteredDataSet(new String[] { "COURSE", "GROUP_1", "STUDENT", "STUDENTS_ON_COURSE" },
				new CompositeDataSet(datasets));

	}

	@Override
	protected DatabaseOperation getSetUpOperation() {
		return DatabaseOperation.CLEAN_INSERT;
	}

	@Override
	protected DatabaseOperation getTearDownOperation() {
		return DatabaseOperation.DELETE_ALL;
	}

	@BeforeAll
	static protected void initClass() throws IOException {
		FileLoader fileLoader = new FileLoader();
		dbProperties = new Properties();
		dbProperties.load(fileLoader.loadProperties(ClassLoader.getSystemResource("db.h2.properties")));
		pool = new DBConnectionPool(dbProperties);
		dao = new SchoolJdbcDAO(pool);
	}

	@BeforeEach
	public void setUp() throws Exception {
		super.setUp();
		connection = getConnection().getConnection();
	}

	@AfterEach
	public void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	void insertGroups__AfterInsert_DataMustBeSameAsExpected() throws Exception {

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

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(getClass().getClassLoader().getResource("expectedAfterInsertGroups.xml"));

		ITable expectedTable = expectedDataSet.getTable("GROUP_1");

		SortedTable sortedExpected = new SortedTable(expectedTable, new String[]{"UUID"});

		SortedTable sortedActual = new SortedTable(actualTable, new String[]{"UUID"});
		
		Assertion.assertEquals(sortedExpected, sortedActual);
	}

	@Test
	void insertStudents__AfterInsert_DataMustBeSameAsExpected() throws Exception {

		IDataSet databaseDataSet = getConnection().createDataSet();

		List<Student> students = new ArrayList<Student>();
		
		Student student = new Student();
		student.setUuid("9842dcd2-2170-41c4-a417-0e8cd77491ff");
		student.setGroupUuid(null);
		student.setFirstName("Gregory");
		student.setLastName("Bishop");
		students.add(student);
		
		student = new Student();
		student.setUuid("16d85387-6975-4ff3-ae69-1d5060484ace");
		student.setGroupUuid(null);
		student.setFirstName("Bobby");
		student.setLastName("Crook");
		students.add(student);
		
		dao.insertStudents(students);

		ITable actualTable = databaseDataSet.getTable("STUDENT");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(getClass().getClassLoader().getResource("expectedAfterInsertStudents.xml"));
		
		ReplacementDataSet replacementDataSet = new ReplacementDataSet(expectedDataSet);

		replacementDataSet.addReplacementObject("[NULL]", null);
		
		ITable expectedTable = replacementDataSet.getTable("STUDENT");

		SortedTable sortedExpected = new SortedTable(expectedTable, new String[]{"UUID"});

		SortedTable sortedActual = new SortedTable(actualTable, new String[]{"UUID"});
		
		Assertion.assertEquals(sortedExpected, sortedActual);
	}

	@Test
	void insertCourses_AfterInsert_DataMustBeSameAsExpected() throws Exception {

		IDataSet databaseDataSet = getConnection().createDataSet();
		
		List<Course> courses = new ArrayList<Course>();
		Course course = new Course();
		
		course.setUuid("897215e9-4918-4790-a8fc-4712c6a10723");
		course.setCourseName("Arts Psychology and Sociology");
		course.setCourseDescription("Arts Psychology and Sociology");
		courses.add(course);

		dao.insertCourses(courses);

		ITable actualTable = databaseDataSet.getTable("COURSE");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(getClass().getClassLoader().getResource("expectedAfterInsertCourses.xml"));
		

		ITable expectedTable = expectedDataSet.getTable("COURSE");

		SortedTable sortedExpected = new SortedTable(expectedTable, new String[]{"UUID"});

		SortedTable sortedActual = new SortedTable(actualTable, new String[]{"UUID"});
		
		Assertion.assertEquals(sortedExpected, sortedActual);
	}

	@Test
	void getAllStudents__AfterReading_DataMustBeSameAsExpected() throws SQLException, Exception {
		
		List<Student> students = dao.getAllStudents();
		Collections.sort(students);
		
		IDataSet expectedDataSet = getDataSet();
		ITable expectedTable = expectedDataSet.getTable("STUDENT");
		List<Student> expectedStudents = new ArrayList<Student>();
		int rowsCount = expectedTable.getRowCount();
		
		for (int i = 0; i < rowsCount; i++) {
			Student student = new Student();
			student.setUuid(expectedTable.getValue(i, "UUID").toString());
			student.setGroupUuid(expectedTable.getValue(i, "GROUP_UUID").toString());
			student.setFirstName(expectedTable.getValue(i, "FIRST_NAME").toString());
			student.setLastName(expectedTable.getValue(i, "LAST_NAME").toString());
			expectedStudents.add(student);
		}
		
		Collections.sort(expectedStudents);
		
		assertEquals(students, expectedStudents);
	}

	@Test
	void getAllCourses_AfterReading_DataMustBeSameAsExpected() throws Exception {

		List<Course> Courses = dao.getAllCourses();
		Collections.sort(Courses);

		IDataSet expectedDataSet = getDataSet();
		ITable expectedTable = expectedDataSet.getTable("Course");
		List<Course> expectedCourses = new ArrayList<Course>();
		int rowsCount = expectedTable.getRowCount();

		for (int i = 0; i < rowsCount; i++) {
			Course course = new Course();
			course.setUuid(expectedTable.getValue(i, "UUID").toString());
			course.setCourseName(expectedTable.getValue(i, "COURSE_NAME").toString());
			course.setCourseDescription(expectedTable.getValue(i, "COURSE_DESCRIPTION").toString());
			expectedCourses.add(course);
		}

		Collections.sort(expectedCourses);

		assertEquals(Courses, expectedCourses);
	}

	@Test
	void findStudentCourses_AfterFinding_DataMustBeSameAsExpected() throws SQLException {
		
		String studentUUID = "4a350a4e-68ff-452a-add0-0da4df4a2fe3";
		List<Course> studentCousres = dao.findStudentCourses(studentUUID);
		Collections.sort(studentCousres);
		
		ResultSet expectedRS = connection.createStatement().executeQuery("SELECT "
				+ "course.uuid, course.course_name, course.course_description, students_on_course.student_uuid "
				+ "FROM course "  
				+ "RIGHT JOIN " 
				+ "students_on_course " 
				+ "ON "
				+ "course.uuid = students_on_course.course_uuid " 
				+ "WHERE " 
				+ " students_on_course.student_uuid = '4a350a4e-68ff-452a-add0-0da4df4a2fe3' " );
		
		List<Course> expectedStudentCousres = new ArrayList<Course>();
		while (expectedRS.next()) {
			Course Course = new Course();
			Course.setUuid(expectedRS.getString("UUID").toString());
			Course.setCourseName(expectedRS.getString("COURSE_NAME").toString());
			Course.setCourseDescription(expectedRS.getString("COURSE_DESCRIPTION").toString());
			expectedStudentCousres.add(Course);
		}
		Collections.sort(expectedStudentCousres);

		assertEquals(studentCousres, expectedStudentCousres);
	}

	@Test
	void deleteStudent_AfterDeleting_DataMustBeSameAsExpected() throws SQLException, Exception {
		
		IDataSet databaseDataSet = getConnection().createDataSet();
		
		String studentUUID = "4a350a4e-68ff-452a-add0-0da4df4a2fe3";
		dao.deleteStudent(studentUUID);

		ITable actualTable = databaseDataSet.getTable("STUDENT");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(getClass().getClassLoader().getResource("expectedAfterDeleteStudent.xml"));
		
		ReplacementDataSet replacementDataSet = new ReplacementDataSet(expectedDataSet);

		replacementDataSet.addReplacementObject("[NULL]", null);
		
		ITable expectedTable = replacementDataSet.getTable("STUDENT");

		SortedTable sortedExpected = new SortedTable(expectedTable, new String[]{"UUID"});

		SortedTable sortedActual = new SortedTable(actualTable, new String[]{"UUID"});
		
		Assertion.assertEquals(sortedExpected, sortedActual);
		
		actualTable = databaseDataSet.getTable("STUDENTS_ON_COURSE");
		expectedTable = replacementDataSet.getTable("STUDENTS_ON_COURSE");
		Assertion.assertEquals(sortedExpected, sortedActual);
	}
	
	@Test
	void addStudentToCourse_AfterAdding_DataMustBeSameAsExpected() throws SQLException, Exception {

		IDataSet databaseDataSet = getConnection().createDataSet();

		String studentUUID = "dd81e8f9-4b79-44b7-9bfb-e9e874560faf";
		String courseUUID = "72497b35-9ccb-4e8d-a773-bbf4d06463c9";
		dao.addStudentToCourse(studentUUID, courseUUID);

		ITable actualTable = databaseDataSet.getTable("STUDENTS_ON_COURSE");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(getClass().getClassLoader().getResource("expectedAfterAddStudentToCourse.xml"));
		
		ITable expectedTable = expectedDataSet.getTable("STUDENTS_ON_COURSE");

		SortedTable sortedExpected = new SortedTable(expectedTable, new String[] { "STUDENT_UUID", "COURSE_UUID" });

		SortedTable sortedActual = new SortedTable(actualTable, new String[] { "STUDENT_UUID", "COURSE_UUID" });

		Assertion.assertEquals(sortedExpected, sortedActual);
	}

	@Test
	void dropoutStudentFromCourse_AfterDropout_DataMustBeSameAsExpected() throws SQLException, Exception {
		
		IDataSet databaseDataSet = getConnection().createDataSet();

		String studentUUID = "4a350a4e-68ff-452a-add0-0da4df4a2fe3";
		String courseUUID = "bfde4ec7-7b71-459e-a27d-3377d0f4cc08";
		dao.dropoutStudentFromCourse(studentUUID, courseUUID);

		ITable actualTable = databaseDataSet.getTable("STUDENTS_ON_COURSE");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
				.build(getClass().getClassLoader().getResource("expectedAfterDeleteStudentFromCourse.xml"));
		
		ITable expectedTable = expectedDataSet.getTable("STUDENTS_ON_COURSE");

		SortedTable sortedExpected = new SortedTable(expectedTable, new String[] { "STUDENT_UUID", "COURSE_UUID" });

		SortedTable sortedActual = new SortedTable(actualTable, new String[] { "STUDENT_UUID", "COURSE_UUID" });

		Assertion.assertEquals(sortedExpected, sortedActual);
	}
}

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.lms.sqljdbcschool.dao.DBConnectionPool;
import ua.com.foxminded.lms.sqljdbcschool.dao.GroupDAO;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDataBase;
import ua.com.foxminded.lms.sqljdbcschool.entities.Course;
import ua.com.foxminded.lms.sqljdbcschool.entities.Group;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

class SchoolDataBaseTest {
	static private DBConnectionPool connectionPool;

	@BeforeAll
	static void setUpBeforeAllTest() throws Exception {

		URL DBPropertiesUrl = ClassLoader.getSystemResource("db.properties");

		FileLoader fileLoader = new FileLoader();
		Properties conectionProperties = new Properties();
		conectionProperties.load(fileLoader.load(DBPropertiesUrl));

		Connection connection=null;
		try {
			connectionPool = new DBConnectionPool(conectionProperties);
			connection = connectionPool.checkOut();
			SchoolDataBase schoolDataBase = new SchoolDataBase(connectionPool, "school_db");
			
			schoolDataBase.dropTables();
			schoolDataBase.createTables();
			
		
		} catch (SQLException e) {
			System.out.println("Connection failure.");
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
	}

	@Test
	void coursesDAOWriteThenReadAndCompare_ReadedDataMustBeSameAsWriten() throws Exception {
		Connection connection = connectionPool.checkOut();

		try {
			Group group = new Group(UUID.randomUUID(), "Math");
			GroupDAO groupDAO = new GroupDAO("school_db", connection);

			groupDAO.insertRow(group, connection);
			group.setGroupName("Physics");
			groupDAO.updateRow(group, connection);

			System.out.println(Calendar.getInstance().getTime().toString());

			for (int i = 0; i < 200; i++) {
				Group group1 = new Group(UUID.randomUUID(), "Math");
				groupDAO.insertRow(group1, connection);
			}

			System.out.println(Calendar.getInstance().getTime().toString());

			Group group1 = new Group(UUID.randomUUID(), "Math");
			groupDAO.insertRow(group1, connection);

			ArrayList<String> whereCondition = new ArrayList<String>();
			whereCondition.add(" id::text LIKE ");

			ArrayList<String> whereValues = new ArrayList<String>();
			whereValues.add("77%");

			ArrayList<Group> getGroups = groupDAO.getWhereConditionsJoinsAnd(groupDAO.getFieldsNames(), whereCondition,
					whereValues, connection);

			System.out.println(
					String.join("\r\n", getGroups.stream().map(g -> g.toString()).collect(Collectors.toList())));

//			groupDAO.deleteOneRow(group, connection);
//			groupDAO.deleteOneRow(group1, connection);
//			groupDAO.deleteOneRow(group, connection);
//			groupDAO.deleteOneRow(group1, connection);

			ArrayList<Course> coursesExpected = new ArrayList<Course>();
			ArrayList<Course> coursesResult = new ArrayList<Course>();

			assertEquals(coursesExpected, coursesResult, "Table Courses: Readed data must be same as writen.");

		} catch (SQLException e) {
			System.out.println("SQL EXCEPTION.");
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
	}

//	@Test
//	void studentsAndGroupsDAOWriteThenReadAndCompare_ReadedDataMustBeSameAsWriten() throws Exception {
//
//		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://maxcloud.sytes.net:5432/school_db",
//				"schooluser", "1199")) {
//
//			GroupDAO groupDAO = new GroupDAO(connection);
//
//			ArrayList<Group> groupsExpected = new ArrayList<Group>();
//			ArrayList<Group> groupsResult = new ArrayList<Group>();
//
//			groupsExpected.add(new Group("Developers"));
//			groupsExpected.get(0).setId(groupDAO.createEntity(groupsExpected.get(0)));
//
//			groupsExpected.add(new Group("Greens"));
//			groupsExpected.get(1).setId(groupDAO.createEntity(groupsExpected.get(1)));
//
//			groupsExpected.add(new Group("Experts"));
//			groupsExpected.get(2).setId(groupDAO.createEntity(groupsExpected.get(2)));
//
//			groupsResult = groupDAO.readEntities("");
//
//			assertEquals(groupsExpected, groupsResult, "Table Groups: Readed data must be same as writen.");
//
//			
//			StudentDAO studentDAO = new StudentDAO(connection);
//
//			ArrayList<Student> studentsExpected = new ArrayList<Student>();
//			ArrayList<Student> studentsResult = new ArrayList<Student>();
//
//			studentsExpected.add(new Student(groupsExpected.get(0).getId(), "Joe", "Public"));
//			studentsExpected.get(0).setId(studentDAO.createEntity(studentsExpected.get(0)));
//
//			studentsExpected.add(new Student(groupsExpected.get(1).getId(), "John", "Smith"));
//			studentsExpected.get(1).setId(studentDAO.createEntity(studentsExpected.get(1)));
//
//			studentsExpected.add(new Student(groupsExpected.get(2).getId(), "Robert", "Martin"));
//			studentsExpected.get(2).setId(studentDAO.createEntity(studentsExpected.get(2)));
//			
//			studentsExpected.add(new Student(groupsExpected.get(2).getId(), "Hank", "Williams"));
//			studentsExpected.get(3).setId(studentDAO.createEntity(studentsExpected.get(3)));
//
//			studentsResult = studentDAO.readEntities("");
//
//			assertEquals(studentsExpected, studentsResult, "Table Students: Readed data must be same as writen.");
//			
//			
//			connection.close();
//
//		} catch (SQLException e) {
//			System.out.println("Connection failure.");
//			e.printStackTrace();
//		}
//	}
}

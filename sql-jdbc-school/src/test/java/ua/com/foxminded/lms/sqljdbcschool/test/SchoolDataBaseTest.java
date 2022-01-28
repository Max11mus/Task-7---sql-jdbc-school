package ua.com.foxminded.lms.sqljdbcschool.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.lms.sqljdbcschool.dao.CourseDAO;
import ua.com.foxminded.lms.sqljdbcschool.dao.DBConnectionPool;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDataBaseDAO;
import ua.com.foxminded.lms.sqljdbcschool.dao.SquadDAO;
import ua.com.foxminded.lms.sqljdbcschool.dao.StudentDAO;
import ua.com.foxminded.lms.sqljdbcschool.dao.StudentsOnCourseDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Squad;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.StudentsOnCourse;
import ua.com.foxminded.lms.sqljdbcschool.utils.DataGenerator;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

class SchoolDataBaseTest {
	static private DBConnectionPool connectionPool;
	static SchoolDataBaseDAO schoolDataBase;
	static DataGenerator dataGenerator = new DataGenerator();
	static ArrayList<Squad> squadArray;
	static ArrayList<Course> courseArray;
	static ArrayList<Student> studentArray;
	static HashSet<StudentsOnCourse> studentsOnCourses;

	@BeforeEach
	void setUpBeforeEachTest() throws Exception {
		URL DBPropertiesUrl = ClassLoader.getSystemResource("db.properties");
		FileLoader fileLoader = new FileLoader();
		Properties conectionProperties = new Properties();
		conectionProperties.load(fileLoader.load(DBPropertiesUrl));
		Connection connection = null;

		try {
			connectionPool = new DBConnectionPool(conectionProperties);
			connection = connectionPool.checkOut();
			schoolDataBase = new SchoolDataBaseDAO(connectionPool);
			schoolDataBase.dropTables();
			schoolDataBase.createTables();
			schoolDataBase.initTables();

			HashSet<String> squadNames = dataGenerator.getSquadNames(10);// 10 groups(squads) with randomly
			// generated names. The name should contain 2 characters, hyphen, 2 numbers
			squadArray = new ArrayList<Squad>();
			for (Iterator<String> iterator = squadNames.iterator(); iterator.hasNext();) {
				String squadName = iterator.next();
				Squad squad = new Squad();
				squad.setSquadName(squadName);
				squadArray.add(squad);
			}

			HashSet<String> courseNames = dataGenerator.getCourseNames(10); // Create 10 courses (math, biology, etc)
			courseArray = new ArrayList<Course>();
			for (Iterator<String> iterator = courseNames.iterator(); iterator.hasNext();) {
				String courseName = iterator.next();
				Course course = new Course();
				course.setCourseName(courseName);
				course.setCourseDescription(courseName);
				courseArray.add(course);
			}

			ArrayList<String> studentNames = (ArrayList<String>) dataGenerator.getStudentNames(200).stream()
					.collect(Collectors.toList());
			ArrayList<String> studentSurNames = (ArrayList<String>) dataGenerator.getStudentSurNames(200).stream()
					.collect(Collectors.toList()); // 200 students. with random names
			studentArray = new ArrayList<Student>();
			for (int i = 0; i < studentNames.size(); i++) {
				Student student = new Student();
				student.setStudentFirstName(studentNames.get(i));
				student.setStudentLastName(studentSurNames.get(i));
				studentArray.add(student);
			}

			int randomIndex = 0; // Randomly assign students to suads
			for (Student student : studentArray) {
				randomIndex = (int) Math.floor(Math.random() * squadArray.size());
				student.setSquadId(squadArray.get(randomIndex).getId());
			}

			int courseNumberForStudent; // Randomly assign from 1 to 3 courses for each student
			randomIndex = 0;
			StudentsOnCourse studentsOnCourse = null;
			studentsOnCourses = new HashSet<StudentsOnCourse>();
			for (Student student : studentArray) {
				courseNumberForStudent = (int) Math.floor(Math.random() * 3);
				while (courseNumberForStudent >= 0) {
					randomIndex = (int) Math.floor(Math.random() * courseArray.size());
					studentsOnCourse = new StudentsOnCourse();
					studentsOnCourse.setStudentId(student.getId());
					studentsOnCourse.setCourseId(courseArray.get(randomIndex).getId());
					studentsOnCourses.add(studentsOnCourse);
					courseNumberForStudent--;
				}
			}

		} catch (SQLException e) {
			System.out.println("Connection failure.");
			e.printStackTrace();
		} finally {
			connectionPool.checkIn(connection);
		}
	}

	@Test
	void tableDAOClass_InsertAndGetTest_WriteThenReadAndCompare_ReadedDataMustBeSameAsWriten() throws Exception {
		Connection connection = null;
		try {
			connection = connectionPool.checkOut();

			CourseDAO courseDAO = new CourseDAO(connectionPool.getDBname(), connection);
			for (Course course : courseArray) {
				courseDAO.insert(course, connection);
			}

			SquadDAO squadDAO = new SquadDAO(connectionPool.getDBname(), connection);
			for (Squad squad : squadArray) {
				squadDAO.insert(squad, connection);
			}

			StudentDAO studentDAO = new StudentDAO(connectionPool.getDBname(), connection);
			for (Student student : studentArray) {
				studentDAO.insert(student, connection);
			}

			StudentsOnCourseDAO studentsOnCourseDAO = new StudentsOnCourseDAO(connectionPool.getDBname(), connection);
			for (StudentsOnCourse studentsOnCourse : studentsOnCourses) {
				studentsOnCourseDAO.insert(studentsOnCourse, connection);
			}

			ArrayList<Squad> actualSquadArray = squadDAO.getAll(connection);
			ArrayList<Course> actualCourseArray = courseDAO.getAll(connection);
			ArrayList<Student> actualStudentArray = studentDAO.getAll(connection);
			HashSet<StudentsOnCourse> actualStudentsOnCourses = new HashSet<StudentsOnCourse>();
			actualStudentsOnCourses.addAll(studentsOnCourseDAO.getAll(connection));

			assertEquals(squadArray, actualSquadArray, "Readed Data from squad table must be same as writeln !!!");
			assertEquals(courseArray, actualCourseArray, "Readed Data from course table must be same as writeln !!!");
			assertEquals(studentArray, actualStudentArray,
					"Readed Data from student table must be same as writeln !!!");
			assertEquals(studentsOnCourses, actualStudentsOnCourses,
					"Readed Data from students_on_course table must be same as writeln !!!");

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (connection != null) {
				connectionPool.checkIn(connection);
			}
		}
	}

}

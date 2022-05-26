package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import ua.com.foxminded.lms.sqljdbcschool.dao.DBConnectionPool;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDBInitializer;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

public class SchoolDBApp {
	private static FileLoader fileLoader = new FileLoader();
	private static URL DBPropertiesUrl = ClassLoader.getSystemResource("db.posgresql.properties");;
	private static Properties conectionProperties = new Properties();
	private static Function<Properties, DBConnectionPool> initPool = (properties) -> {
		try {
			properties.load(fileLoader.load(DBPropertiesUrl));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new DBConnectionPool(properties);
	};
	private static DBConnectionPool connectionPool = initPool.apply(conectionProperties);
	private static SchoolDBInitializer schoolDBInit;
	private static SchoolDAO schoolDAO = new SchoolDAO(connectionPool);
	private static Scanner in = new Scanner(System.in);
	private static PrintWriter out = new PrintWriter(System.out, true);

	public static void main(String[] args) throws IOException, SQLException {
		
		out.println();
		out.println("Using connection properties");
		conectionProperties.forEach((k, v) -> out.println(k + " =  " + v));

		prepareDB();

		Menu appMenu = new Menu(in, out, "Choose an option");

		FindGroupsStudentCountLessOrEquals findGroupsStudentCountLessOrEquals = new FindGroupsStudentCountLessOrEquals(
				in, out, schoolDAO);
		findGroupsStudentCountLessOrEquals.setName("Find all groups with less or equals student count");

		FindStudentsByCourseName findStudentsByCourseName = new FindStudentsByCourseName(in, out, schoolDAO);
		findStudentsByCourseName.setName("Find all students related to course with given name");

		AddStudent addStudent = new AddStudent(in, out, schoolDAO);
		addStudent.setName("Add new student");

		DeleteStudent deleteStudent = new DeleteStudent(in, out, schoolDAO);
		deleteStudent.setName("Delete student by STUDENT_ID");

		AddStudentToCourse addStudentToCourse = new AddStudentToCourse(in, out, schoolDAO);
		addStudentToCourse.setName("Add a student to the course (from a list)");

		DropoutStudentFromCourse dropoutStudentFromCourse = new DropoutStudentFromCourse(in, out, schoolDAO);
		dropoutStudentFromCourse.setName("Remove the student from one of his or her courses");

		appMenu.addMenuOption(findGroupsStudentCountLessOrEquals);
		appMenu.addMenuOption(findStudentsByCourseName);
		appMenu.addMenuOption(addStudent);
		appMenu.addMenuOption(deleteStudent);
		appMenu.addMenuOption(addStudentToCourse);
		appMenu.addMenuOption(dropoutStudentFromCourse);

		appMenu.runCycle();

		out.close();
		in.close();
		connectionPool.closeConnections();

	}

	private static void prepareDB() throws SQLException, IOException {

		out.println();
		out.println("Preparing Database");

		schoolDBInit = new SchoolDBInitializer(connectionPool);
		out.println("Drop Tables");
		schoolDBInit.dropTables();
		out.println("Create Tables");
		schoolDBInit.createTables();

		EntitiesGenerator entitiesGenerator = new EntitiesGenerator();
		List<Group> groups = entitiesGenerator.getRandomGroups();
		List<Student> students = entitiesGenerator.getRandomStudents();
		List<Course> courses = entitiesGenerator.getRandomCourses();
		entitiesGenerator.randomEnrollStudentsToGroups(students, groups);
		ConcurrentHashMap<String, List<Student>> enrolledStudents = entitiesGenerator
				.randomEnrollStudentsToCourses(students, courses);
		
		schoolDAO.insertGroups(groups);
		schoolDAO.insertStudents(students);
		schoolDAO.insertCourses(courses);
		enrolledStudents.forEach((courseUuid, studentList) -> studentList.parallelStream()
				.forEach(student -> schoolDAO.addStudentToCourse(student.getUuid(), courseUuid)));
	}

}
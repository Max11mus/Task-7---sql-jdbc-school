package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import ua.com.foxminded.lms.sqljdbcschool.dao.DBConnectionPool;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDBInitializer;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.utils.DataGenerator;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

public class SchoolDBApp {
	private static URL DBPropertiesUrl;
	private static Properties conectionProperties = new Properties();
	private static DBConnectionPool connectionPool;
	private static SchoolDBInitializer schoolDBInit;
	private static SchoolDAO schoolDAO ;
	private static DataGenerator dataGenerator;
	private static Scanner in = new Scanner(System.in);
	private static PrintWriter out = new PrintWriter(System.out, true);
	
	public static void main(String[] args) throws IOException, SQLException {
		
		DBPropertiesUrl = ClassLoader.getSystemResource("db.posgresql.properties");
		FileLoader fileLoader = new FileLoader();
		Properties properties = new Properties() ;
		properties.load(fileLoader.load(DBPropertiesUrl));
		conectionProperties.load(fileLoader.load(DBPropertiesUrl));
		connectionPool = new DBConnectionPool(conectionProperties);

		out.println();
		out.println("Using connection properties");
		conectionProperties.forEach((k, v) -> out.println(k + " =  " + v));

		schoolDAO = new SchoolDAO(connectionPool);
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

		dataGenerator = new DataGenerator();
		out.println();
		out.println("Create 10 groups ");
		ArrayList<String> groupNames = dataGenerator.getGroupNames(10);
		ArrayList<Group> groups = new ArrayList<Group>();
		for (Iterator<String> iterator = groupNames.iterator(); iterator.hasNext();) {
			String groupName = iterator.next();
			Group group = new Group();
			group.setGroupName(groupName);
			groups.add(group);
		}

		out.println();
		out.println("Create 10 courses ");
		ArrayList<String> courseNames = dataGenerator.getCourseNames(10);
		ArrayList<Course> courses = new ArrayList<Course>();
		for (Iterator<String> iterator = courseNames.iterator(); iterator.hasNext();) {
			String courseName = iterator.next();
			Course course = new Course();
			course.setCourseName(courseName);
			course.setCourseDescription(courseName);
			courses.add(course);
		}

		out.println();
		out.println("Create 200 students");
		ArrayList<String> studentsNames = dataGenerator.getStudentNames(200);
		ArrayList<String> studentSurNames = dataGenerator.getStudentSurNames(200);
		ArrayList<Student> students = new ArrayList<Student>();
		for (int i = 0; i < studentsNames.size(); i++) {
			Student student = new Student();
			student.setStudentFirstName(studentsNames.get(i));
			student.setStudentLastName(studentSurNames.get(i));
			students.add(student);
		}

		out.println();
		out.println("Randomly assign 100 student to groups");
		int randomIndex = 0;
		Random randomGenerator = new Random();
		ArrayList<Student> studentsWithGroups = new ArrayList<Student>();
		randomGenerator.ints(0, students.size()).distinct().limit(100)
				.forEach(i -> studentsWithGroups.add(students.get(i)));
		for (Student student : studentsWithGroups) {
			randomIndex = randomGenerator.ints(0, groups.size()).findFirst().getAsInt();
			student.enrollTo(groups.get(randomIndex));
		}

		out.println();
		out.println("Randomly assign from 1 to 3 courses for each student");
		int cousresPerStudent = 0;
		randomIndex = 0;
		for (Student student : students) {
			cousresPerStudent = randomGenerator.ints(1, 4).findFirst().getAsInt();
			randomGenerator.ints(0, courses.size()).distinct().limit(cousresPerStudent)
					.forEach(i -> courses.get(i).enroll(student));
		}

		schoolDAO.insertGroups(groups);
		schoolDAO.insertStudents(students);
		schoolDAO.insertCourses(courses);
	}

}
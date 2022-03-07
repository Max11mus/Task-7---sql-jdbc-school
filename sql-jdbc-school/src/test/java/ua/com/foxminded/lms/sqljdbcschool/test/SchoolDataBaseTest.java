package ua.com.foxminded.lms.sqljdbcschool.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ua.com.foxminded.lms.sqljdbcschool.dao.CourseDAO;
import ua.com.foxminded.lms.sqljdbcschool.dao.DBConnectionPool;
import ua.com.foxminded.lms.sqljdbcschool.dao.GroupDAO;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDBIntializer;
import ua.com.foxminded.lms.sqljdbcschool.dao.StudentDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.utils.DataGenerator;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

class SchoolDataBaseTest {
	static private DBConnectionPool connectionPool;
	static private SchoolDBIntializer schoolDataBase;
	static private DataGenerator dataGenerator;
	static private ArrayList<Group> groups;
	static private ArrayList<Course> courses;
	static private ArrayList<Student> students;

	@BeforeEach
	void setUpBeforeEachTest() throws Exception {
		dataGenerator = new DataGenerator();
		URL DBPropertiesUrl = ClassLoader.getSystemResource("db.h2.properties");
		FileLoader fileLoader = new FileLoader();
		Properties conectionProperties = new Properties();
		conectionProperties.load(fileLoader.load(DBPropertiesUrl));

		try {
			int DBPoolSize = 10;
			connectionPool = new DBConnectionPool(conectionProperties, DBPoolSize);
			schoolDataBase = new SchoolDBIntializer(connectionPool);
			schoolDataBase.dropTables();
			schoolDataBase.createTables();

			ArrayList<String> groupNames = dataGenerator.getGroupNames(10);// 10 groups names
			groups = new ArrayList<Group>();

			for (Iterator<String> iterator = groupNames.iterator(); iterator.hasNext();) {
				String groupName = iterator.next();
				Group group = new Group();
				group.setGroupName(groupName);
				groups.add(group);
			}

			ArrayList<String> courseNames = dataGenerator.getCourseNames(10); // Create 10 courses (math, biology, etc)
			courses = new ArrayList<Course>();
			for (Iterator<String> iterator = courseNames.iterator(); iterator.hasNext();) {
				String courseName = iterator.next();
				Course course = new Course();
				course.setCourseName(courseName);
				course.setCourseDescription(courseName);
				courses.add(course);
			}

			ArrayList<String> studentsNames = dataGenerator.getStudentNames(1000); // Create 1000 students
			ArrayList<String> studentSurNames = dataGenerator.getStudentSurNames(1000);
			students = new ArrayList<Student>();
			for (int i = 0; i < studentsNames.size(); i++) {
				Student student = new Student();
				student.setStudentFirstName(studentsNames.get(i));
				student.setStudentLastName(studentSurNames.get(i));
				students.add(student);
			}

			int randomIndex = 0; // Randomly assign 90 student to groups
			Random randomGenerator = new Random();

			ArrayList<Student> studentsWithGroups = new ArrayList<Student>();
			randomGenerator.ints(0, students.size()).distinct().limit(90)
					.forEach(i -> studentsWithGroups.add(students.get(i)));

			for (Student student : studentsWithGroups) {
				randomIndex = randomGenerator.ints(0, groups.size()).findFirst().getAsInt();
				student.enrollTo(groups.get(randomIndex));
			}

			int cousresPerStudent = 0; // Randomly assign from 1 to 3 courses for each student
			randomIndex = 0;
			for (Student student : students) {
				cousresPerStudent = randomGenerator.ints(1, 4).findFirst().getAsInt();
				randomGenerator.ints(0, courses.size()).distinct().limit(cousresPerStudent)
						.forEach(i -> courses.get(i).enroll(student));
			}

		} catch (SQLException e) {
			System.out.println("Connection failure.");
			e.printStackTrace();
		}
	}

	@AfterEach
	void afterEachTest() throws SQLException {
		connectionPool.closeConnections();
	}

	@Test
	void DAO_InsertAndGetTest_WriteThenReadAndCompare_ReadedDataMustBeSameAsWriten() {

		GroupDAO groupDAO = new GroupDAO(connectionPool);
		groupDAO.insert(groups);

		StudentDAO studentDAO = new StudentDAO(connectionPool);
		studentDAO.insert(students);

		CourseDAO courseDAO = new CourseDAO(connectionPool);
		courseDAO.insert(courses);

		List<Group> actualGroupArray = groupDAO.getAll();
		List<Student> actualStudentArray = studentDAO.getAll();
		List<Course> actualCourseArray = courseDAO.getAll();

		assertEquals(groups, actualGroupArray, "Readed Data from group table must be same as writeln !!!");
		assertEquals(courses, actualCourseArray, "Readed Data from course table must be same as writeln !!!");
		assertEquals(students, actualStudentArray, "Readed Data from student table must be same as writeln !!!");

		assertEquals(groups.size(), groupDAO.getRowsCount(), "Number of group table must be same as writeln !!!");
		assertEquals(courses.size(), courseDAO.getRowsCount(),
				"Number of from course table must be same as writeln !!!");
		assertEquals(students.size(), studentDAO.getRowsCount(), "Number of student must be same as writeln !!!");

		assertEquals(groups.subList(0, groups.size() / 2), groupDAO.getRange(0, groups.size() / 2),
				"Readed Data from group table must be same as writeln !!!");
		assertEquals(courses.subList(0, courses.size() / 2), courseDAO.getRange(0, courses.size() / 2),
				"Readed Data from course table must be same as writeln !!!");
		assertEquals(students.subList(0, students.size() / 2), studentDAO.getRange(0, students.size() / 2),
				"Readed Data from student table must be same as writeln !!!");

	}

	@Test
	void DAO_InsertAndUpdateAndGetTest_WriteThenChangeThenReadAndCompare_ReadedDataMustBeSameAsWriten() {

		GroupDAO groupDAO = new GroupDAO(connectionPool);
		groupDAO.insert(groups);

		StudentDAO studentDAO = new StudentDAO(connectionPool);
		studentDAO.insert(students);

		CourseDAO courseDAO = new CourseDAO(connectionPool);
		courseDAO.insert(courses);

		Random randomGenerator = new Random();
		ArrayList<Group> groupsUpdated = new ArrayList<Group>(); // update random 4 groups
		randomGenerator.ints(0, groups.size()).distinct().limit(4).forEach(index -> {
			groupsUpdated.add(groups.get(index));
			groups.get(index).setGroupName("UpdateTest");
		});
		groupDAO.update(groupsUpdated);

		ArrayList<Course> coursesUpdated = new ArrayList<Course>(); // update random 4 courses
		randomGenerator.ints(0, courses.size()).distinct().limit(4).forEach(index -> {
			coursesUpdated.add(courses.get(index));
			courses.get(index).setCourseName("UpdateTest");
			courses.get(index).setCourseDescription("UpdateTest");
		});
		courseDAO.update(coursesUpdated);

		ArrayList<Student> studentsUpdated = new ArrayList<Student>(); // update random 50 students
		randomGenerator.ints(0, students.size()).distinct().limit(50).forEach(index -> {
			studentsUpdated.add(students.get(index));
			students.get(index).setStudentFirstName("UpdateTest");
			students.get(index).setStudentLastName("UpdateTest");
		});
		studentDAO.update(studentsUpdated);

		List<Group> actualGroupArray = groupDAO.getAll();
		List<Student> actualStudentArray = studentDAO.getAll();
		List<Course> actualCourseArray = courseDAO.getAll();

		Collections.sort(groups);
		Collections.sort(students);
		Collections.sort(courses);
		Collections.sort(actualGroupArray);
		Collections.sort(actualStudentArray);
		Collections.sort(actualCourseArray);

		assertEquals(groups, actualGroupArray, "Readed Data from group table must be same as writeln !!!");
		assertEquals(students, actualStudentArray, "Readed Data from student table must be same as writeln !!!");
		assertEquals(courses, actualCourseArray, "Readed Data from course table must be same as writeln !!!");
	}

	@Test
	void DAO_InsertAndDeleteAndGetTest_WriteThenDeleteThenReadAndCompare_ReadedDataMustBeSameAsWriten() {

		GroupDAO groupDAO = new GroupDAO(connectionPool);
		groupDAO.insert(groups);

		StudentDAO studentDAO = new StudentDAO(connectionPool);
		studentDAO.insert(students);

		CourseDAO courseDAO = new CourseDAO(connectionPool);
		courseDAO.insert(courses);

		Random randomGenerator = new Random();

		ArrayList<Student> studentsDeleted = new ArrayList<Student>(); // delete random 50 students
		randomGenerator.ints(0, students.size()).distinct().limit(50)
				.forEach(index -> studentsDeleted.add(students.get(index)));
		students.removeAll(studentsDeleted);
		Course.dropoutFrom(studentsDeleted, courses);
		studentDAO.delete(studentsDeleted);

		ArrayList<Course> coursesDeleted = new ArrayList<Course>(); // delete random 3 courses
		randomGenerator.ints(0, courses.size()).distinct().limit(3)
				.forEach(index -> coursesDeleted.add(courses.get(index)));
		courses.removeAll(coursesDeleted);
		courseDAO.delete(coursesDeleted);

		ArrayList<Group> groupsDeleted = new ArrayList<Group>(); // delete random 4 groups
		randomGenerator.ints(0, groups.size()).distinct().limit(4)
				.forEach(index -> groupsDeleted.add(groups.get(index)));
		groups.removeAll(groupsDeleted);
		Student.dropoutFrom(students, groupsDeleted);
		groupDAO.delete(groupsDeleted);

		List<Group> actualGroupArray = groupDAO.getAll();
		List<Student> actualStudentArray = studentDAO.getAll();
		List<Course> actualCourseArray = courseDAO.getAll();

		Collections.sort(groups);
		Collections.sort(students);
		Collections.sort(courses);
		Collections.sort(actualGroupArray);
		Collections.sort(actualStudentArray);
		Collections.sort(actualCourseArray);

		assertEquals(groups, actualGroupArray, "Readed Data from group table must be same as writeln !!!");
		assertEquals(students, actualStudentArray, "Readed Data from student table must be same as writeln !!!");
		assertEquals(courses, actualCourseArray, "Readed Data from course table must be same as writeln !!!");

	}

	@Test
	void DAO_HugeParralelInsertAndGetTestWithBenchmark_WriteThenReadAndCompare_ReadedDataMustBeSameAsWriten()
			throws IOException, InterruptedException, ExecutionException {

		int numberOfStudents = 300_000; 
		long startTime = 0;
		long elapsed = 0;
		List<Student> actualStudentArray = new ArrayList<Student>();
		
		ArrayList<String> studentsNames = dataGenerator.getStudentNames(numberOfStudents);
		ArrayList<String> studentSurNames = dataGenerator.getStudentSurNames(numberOfStudents);
		students = new ArrayList<Student>();
		for (int i = 0; i < studentsNames.size(); i++) {
			Student student = new Student();
			student.setStudentFirstName(studentsNames.get(i));
			student.setStudentLastName(studentSurNames.get(i));
			students.add(student);
		}

		StudentDAO studentDAO = new StudentDAO(connectionPool);
		studentDAO.setEnableLogging(false); 
		studentDAO.deleteAll();
		
		int chunkSize = 8000;
		int chunksNumber = (students.size() / chunkSize) + ((students.size() % chunkSize) > 0 ? 1 : 0);
		
		List<List<Student>> splitedStudentsToWrite = IntStream.range(0, chunksNumber).parallel()
				.mapToObj(i -> students.subList(i * chunkSize,
						(i + 1) * chunkSize > students.size() ? students.size() : (i + 1) * chunkSize))
				.collect(Collectors.toList());

		startTime = System.currentTimeMillis();
		System.out.println("Inserting " + numberOfStudents + " rows by sequential SQL queries (" + chunkSize
				+ " rows) using one connection.");
		splitedStudentsToWrite.stream().forEach(list -> studentDAO.insert((list)));
		elapsed = System.currentTimeMillis() - startTime;
		System.out.println("Elapsed " + ((double) elapsed / 1000) + " seconds.");
		System.out.println();
		
		startTime = System.currentTimeMillis();
		System.out.println("Reading " + numberOfStudents + " rows by sequential SQL queries (" + chunkSize
				+ " rows) using one connection.");
		List<List<Student>> splitedStudentsToRead = IntStream.range(0, chunksNumber)
				.mapToObj(i -> studentDAO.getRange(i * chunkSize, 
						(i + 1) * chunkSize > students.size()
								? students.size()
								: (i + 1) * chunkSize))
				.collect(Collectors.toList());
		elapsed = System.currentTimeMillis() - startTime;
		System.out.println("Elapsed " + ((double) elapsed / 1000) + " seconds.");
		System.out.println();
		
		splitedStudentsToRead.stream().forEach(array -> actualStudentArray.addAll(array));
	
		assertEquals(students, actualStudentArray, "Readed Data from student table must be same as writeln !!!");

		studentDAO.deleteAll();
		
		startTime = System.currentTimeMillis();
		System.out.println("Inserting " + numberOfStudents
				+ " rows by async parralel SQL queries  (" + chunkSize + " rows) using several connections.");
		List<CompletableFuture<Void>> tasksInsert = splitedStudentsToWrite.stream()
				.map(list -> CompletableFuture.runAsync(() -> studentDAO.insert((list))))
				.collect(Collectors.toList());
		tasksInsert.parallelStream().forEach(t -> {
			try {
				t.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		elapsed = System.currentTimeMillis() - startTime;
		System.out.println("Elapsed " + ((double) elapsed / 1000) + " seconds.");
		System.out.println();
		
		actualStudentArray.clear();
		startTime = System.currentTimeMillis();
		System.out.println("Reading " + numberOfStudents + " rows by async parralel SQL queries (" + chunkSize
				+ " rows) using several connections.");
		List<CompletableFuture<List<Student>>> tasksRead = IntStream.range(0, chunksNumber)
				.mapToObj(i -> CompletableFuture
						.supplyAsync(() -> studentDAO.getRange(i * chunkSize,
								(i + 1) * chunkSize > students.size()
										? students.size()
										: (i + 1) * chunkSize)))
				.collect(Collectors.toList());
		tasksRead.parallelStream().forEach(t -> {
			try {
				actualStudentArray.addAll(t.get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		elapsed = System.currentTimeMillis() - startTime;
		System.out.println("Elapsed " + ((double) elapsed / 1000) + " seconds.");
		
		Collections.sort(students);
		Collections.sort(actualStudentArray);
		assertEquals(students, actualStudentArray, "Readed Data from student table must be same as writeln !!!");
	}

}

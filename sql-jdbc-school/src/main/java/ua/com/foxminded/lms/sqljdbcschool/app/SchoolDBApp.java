package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

public class SchoolDBApp {
	static private String EOL = System.lineSeparator();
	static private Scanner keyInput = new Scanner(System.in);
	private static URL DBPropertiesUrl;
	private static FileLoader fileLoader = new FileLoader();
	private static Properties conectionProperties = new Properties();
	private static DBConnectionPool connectionPool;
	private static SchoolDBIntializer schoolDBInit;
	private static GroupDAO groupDAO;
	private static StudentDAO studentDAO;
	private static CourseDAO courseDAO;
	private static DataGenerator dataGenerator;

	public static void main(String[] args) throws IOException, SQLException {
			
		DBPropertiesUrl = ClassLoader.getSystemResource("db.h2.properties");

		Menu chooseDBMenu = new Menu("Choose a database to work with (by default H2 Database)");

		MenuItem H2Database = new MenuItem("H2 Database - internal in memory",
				() -> DBPropertiesUrl = ClassLoader.getSystemResource("db.h2.properties"));

		MenuItem PostgreSQLDatabase = new MenuItem("PostgreSQL Database - external",
				() -> DBPropertiesUrl = ClassLoader.getSystemResource("db.posgresql.properties"));

		chooseDBMenu.addCommand(H2Database);
		chooseDBMenu.addCommand(PostgreSQLDatabase);

		chooseDBMenu.runCommandThenExit();

		conectionProperties.load(fileLoader.load(DBPropertiesUrl));
		connectionPool = new DBConnectionPool(conectionProperties, 10);

		System.out.println();
		System.out.println("Using connection properties");
		conectionProperties.forEach((k, v) -> System.out.println(k + " =  " + v));

		prepareDB();

		groupDAO = new GroupDAO(connectionPool);
		groupDAO.setEnableLogging(true);
		studentDAO = new StudentDAO(connectionPool);
		studentDAO .setEnableLogging(true);
		courseDAO = new CourseDAO(connectionPool);
		courseDAO.setEnableLogging(true);
		
		Menu appMenu = new Menu("Choose an option");
		
		Menu showMenu = new Menu("Show ... ");
		Menu deleteMenu = new Menu("Delete ... ");
		Menu insertMenu = new Menu("Insert ... ");
		Menu updateMenu = new Menu("Update ... ");
		MenuItem task7A = new MenuItem("a. Find all groups with less or equals student count", () -> runTask7A());
		MenuItem task7B = new MenuItem("b. Find all students related to course with given name", () -> runTask7B());
		MenuItem task7C = new MenuItem("c. Add new student", () -> runTask7C());
		MenuItem task7D = new MenuItem("d. Delete student by STUDENT_ID", () -> runTask7D());
		MenuItem task7E = new MenuItem("e. Add a student to the course (from a list)", () -> runTask7E());
		MenuItem task7F = new MenuItem("f. Remove the student from one of his or her courses", () -> runTask7F());
		MenuItem benchmark = new MenuItem("Benchmark", () -> {
			try {
				runBencmark();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		appMenu.addCommand(showMenu);
		appMenu.addCommand(deleteMenu);
		appMenu.addCommand(insertMenu);
		appMenu.addCommand(updateMenu);
		appMenu.addCommand(benchmark);
		appMenu.addCommand(task7A);
		appMenu.addCommand(task7B);
		appMenu.addCommand(task7C);
		appMenu.addCommand(task7D);
		appMenu.addCommand(task7E);
		appMenu.addCommand(task7F);
		

		MenuItem showAllGroups = new MenuItem("Show all groups ", () -> {
			groupDAO.getAll();
			System.out.println(groupDAO.getQueryResultLog());
		});

		MenuItem showAllStudents = new MenuItem("Show all students ", () -> {
			studentDAO.getAll();
			System.out.println(studentDAO.getQueryResultLog());
		});

		MenuItem showAllCourses = new MenuItem("Show all courses ", () -> {
			courseDAO.getAll();
			System.out.println(courseDAO.getQueryResultLog());
		});
		
		MenuItem showStudentsNumber = new MenuItem("Show students number", () -> {
			studentDAO.getRowsCount();
			System.out.println(studentDAO.getQueryResultLog());
		});
		
		MenuItem showCoursesNumber = new MenuItem("Show courses number", () -> {
			courseDAO.getRowsCount();
			System.out.println(courseDAO.getQueryResultLog());
		});
		
		MenuItem showGroupsNumber = new MenuItem("Show groups Number", () -> {
			groupDAO.getRowsCount();
			System.out.println(groupDAO.getQueryResultLog());
		});
		
		MenuItem deleteOneStudent = new MenuItem("Delete one student", () -> deleteOneStudent());
		MenuItem deleteOneGroup = new MenuItem("Delete one group", () -> deleteOneGroup());
		MenuItem deleteOneCourse = new MenuItem("Delete one course", () -> deleteOneCourse());
		
		MenuItem deleteAllStudent = new MenuItem("Delete all students", () -> {
			studentDAO.deleteAll();
			System.out.println(studentDAO.getQueryResultLog());
		});
		MenuItem deleteAllGroup = new MenuItem("Delete all groups", () -> {
			groupDAO.deleteAll();
			System.out.println(groupDAO.getQueryResultLog());
		});
		MenuItem deleteAllCourse = new MenuItem("Delete all courses", () -> {
			courseDAO.deleteAll();
			System.out.println(courseDAO.getQueryResultLog());
		});

		MenuItem insertOneStudent = new MenuItem("Insert one student", () -> insertOneStudent());
		MenuItem insertOneGroup = new MenuItem("Insert one group", () -> insertOneGroup());
		MenuItem insertOneCourse = new MenuItem("Insert one course", () -> insertOneCourse());
		
		MenuItem updateOneStudent = new MenuItem("Update one student", () -> updateOneStudent());
		MenuItem updateOneGroup = new MenuItem("Update one group", () -> updateOneGroup());
		MenuItem updateOneCourse = new MenuItem("Update one course", () -> updateOneCourse());
		
		showMenu.addCommand(showAllStudents);
		showMenu.addCommand(showAllGroups);
		showMenu.addCommand(showAllCourses);
		
		showMenu.addCommand(showStudentsNumber);
		showMenu.addCommand(showGroupsNumber);
		showMenu.addCommand(showCoursesNumber);
		
		deleteMenu.addCommand(deleteOneStudent);
		deleteMenu.addCommand(deleteOneGroup);
		deleteMenu.addCommand(deleteOneCourse);
		
		deleteMenu.addCommand(deleteAllStudent);
		deleteMenu.addCommand(deleteAllGroup);
		deleteMenu.addCommand(deleteAllCourse);
		
		insertMenu.addCommand(insertOneStudent);
		insertMenu.addCommand(insertOneGroup);
		insertMenu.addCommand(insertOneCourse);
		
		updateMenu.addCommand(updateOneStudent);
		updateMenu.addCommand(updateOneGroup);
		updateMenu.addCommand(updateOneCourse);
		
		appMenu.runCycle();

		appMenu.closeInput();

	}

	private static void prepareDB() throws SQLException, IOException {

		System.out.println();
		System.out.println("Preparing Database");

		schoolDBInit = new SchoolDBIntializer(connectionPool);
		System.out.println("Drop Tables");
		schoolDBInit.dropTables();
		System.out.println("Create Tables");
		schoolDBInit.createTables();

		dataGenerator = new DataGenerator();
		System.out.println();
		System.out.println("Create 10 groups ");
		ArrayList<String> groupNames = dataGenerator.getGroupNames(10);
		ArrayList<Group> groups = new ArrayList<Group>();
		for (Iterator<String> iterator = groupNames.iterator(); iterator.hasNext();) {
			String groupName = iterator.next();
			Group group = new Group();
			group.setGroupName(groupName);
			groups.add(group);
		}

		System.out.println();
		System.out.println("Create 10 courses ");
		ArrayList<String> courseNames = dataGenerator.getCourseNames(10);
		ArrayList<Course> courses = new ArrayList<Course>();
		for (Iterator<String> iterator = courseNames.iterator(); iterator.hasNext();) {
			String courseName = iterator.next();
			Course course = new Course();
			course.setCourseName(courseName);
			course.setCourseDescription(courseName);
			courses.add(course);
		}

		System.out.println();
		System.out.println("Create 200 students");
		ArrayList<String> studentsNames = dataGenerator.getStudentNames(200);
		ArrayList<String> studentSurNames = dataGenerator.getStudentSurNames(200);
		ArrayList<Student> students = new ArrayList<Student>();
		for (int i = 0; i < studentsNames.size(); i++) {
			Student student = new Student();
			student.setStudentFirstName(studentsNames.get(i));
			student.setStudentLastName(studentSurNames.get(i));
			students.add(student);
		}

		System.out.println();
		System.out.println("Randomly assign 100 student to groups");
		int randomIndex = 0;
		Random randomGenerator = new Random();
		ArrayList<Student> studentsWithGroups = new ArrayList<Student>();
		randomGenerator.ints(0, students.size()).distinct().limit(100)
				.forEach(i -> studentsWithGroups.add(students.get(i)));
		for (Student student : studentsWithGroups) {
			randomIndex = randomGenerator.ints(0, groups.size()).findFirst().getAsInt();
			student.enrollTo(groups.get(randomIndex));
		}

		System.out.println();
		System.out.println("Randomly assign from 1 to 3 courses for each student");
		int cousresPerStudent = 0;
		randomIndex = 0;
		for (Student student : students) {
			cousresPerStudent = randomGenerator.ints(1, 4).findFirst().getAsInt();
			randomGenerator.ints(0, courses.size()).distinct().limit(cousresPerStudent)
					.forEach(i -> courses.get(i).enroll(student));
		}

		GroupDAO groupDAO = new GroupDAO(connectionPool);
		groupDAO.insert(groups);

		StudentDAO studentDAO = new StudentDAO(connectionPool);
		studentDAO.insert(students);

		CourseDAO courseDAO = new CourseDAO(connectionPool);
		courseDAO.insert(courses);
	}

	private static void deleteOneCourse() {
		Course temp = null;
		int rowNo = 0;

		List<Course> allCourses = courseDAO.getAll();
		System.out.println(courseDAO.getQueryResultLog());

		if (!allCourses.isEmpty()) {

			System.out.print("Enter RowNo: ");

			try {
				rowNo = Integer.parseInt(keyInput.nextLine());

			} catch (NumberFormatException e) {
				System.out.println("Invalid selection. Numbers only please.");
				return;
			}

			if (rowNo < 1 || rowNo > allCourses.size()) {
				System.out.println("RowNo outside of range.");
				return;
			}

			System.out.println();
			temp = allCourses.get(rowNo-1); 
			ArrayList<Course> tempArray = new ArrayList<Course>();
			tempArray.add(temp);
			courseDAO.delete(tempArray);
			System.out.println(courseDAO.getQueryResultLog());
		}

	}

	private static void deleteOneGroup() {
		Group temp = null;
		int rowNo = 0;

		List<Group> allGroups = groupDAO.getAll();
		System.out.println(groupDAO.getQueryResultLog());

		if (!allGroups.isEmpty()) {

			System.out.print("Enter RowNo: ");

			try {
				rowNo = Integer.parseInt(keyInput.nextLine());

			} catch (NumberFormatException e) {
				System.out.println("Invalid selection. Numbers only please.");
				return;
			}

			if (rowNo < 1 || rowNo > allGroups.size()) {
				System.out.println("RowNo outside of range.");
				return;
			}

			System.out.println();
			temp = allGroups.get(rowNo - 1);
			ArrayList<Group> tempArray = new ArrayList<Group>();
			tempArray.add(temp);
			groupDAO.delete(tempArray);
			System.out.println(groupDAO.getQueryResultLog());
		}
	}
	
	private static void deleteOneStudent() {
		Student temp = null;
		int rowNo = 0;

		List<Student> allStudents = studentDAO.getAll();
		System.out.println(studentDAO.getQueryResultLog());

		if (!allStudents.isEmpty()) {

			System.out.print("Enter RowNo: ");

			try {
				rowNo = Integer.parseInt(keyInput.nextLine());

			} catch (NumberFormatException e) {
				System.out.println("Invalid selection. Numbers only please.");
				return;
			}

			if (rowNo < 1 || rowNo > allStudents.size()) {
				System.out.println("RowNo outside of range.");
				return;
			}
			
			System.out.println();
			temp = allStudents.get(rowNo - 1);
			ArrayList<Student> tempArray = new ArrayList<Student>();
			tempArray.add(temp);
			studentDAO.delete(tempArray);
			System.out.println(studentDAO.getQueryResultLog());
		}

	}

	private static void insertOneCourse() {
		Course temp = new Course();
		System.out.println("Insert new course:");
		System.out.println("ID = " + temp.getId().toString());
		
		System.out.print("Enter course name: ");
		temp.setCourseName(keyInput.nextLine());
		
		System.out.print("Enter course description: ");
		temp.setCourseDescription(keyInput.nextLine());
		
		System.out.println();
		ArrayList<Course> tempArray = new ArrayList<Course>();
		tempArray.add(temp);
		courseDAO.insert(tempArray);
		System.out.println(courseDAO.getQueryResultLog());
	}

	private static void insertOneGroup() {
		Group temp = new Group();
		System.out.println("Insert new group:");
		System.out.println("ID = " + temp.getId().toString());

		System.out.print("Enter course name: ");
		temp.setGroupName(keyInput.nextLine());

		System.out.println();
		ArrayList<Group> tempArray = new ArrayList<Group>();
		tempArray.add(temp);
		groupDAO.insert(tempArray);
		System.out.println(groupDAO.getQueryResultLog());
	}
	
	private static void insertOneStudent() {
		Student temp = new Student();
		System.out.println("Insert new student:");
		System.out.println("ID = " + temp.getId().toString());
		System.out.println("GroupID = " + temp.getGroupId().toString());

		System.out.print("Enter first name: ");
		temp.setStudentFirstName(keyInput.nextLine());

		System.out.print("Enter last name: ");
		temp.setStudentLastName(keyInput.nextLine());

		System.out.println();
		ArrayList<Student> tempArray = new ArrayList<Student>();
		tempArray.add(temp);
		studentDAO.insert(tempArray);
		System.out.println(studentDAO.getQueryResultLog());
	}

	private static void updateOneStudent() {
		Student temp = null;
		int rowNo = 0;

		List<Student> allStudents = studentDAO.getAll();
		System.out.println(studentDAO.getQueryResultLog());

		if (!allStudents.isEmpty()) {

			System.out.print("Enter RowNo: ");

			try {
				rowNo = Integer.parseInt(keyInput.nextLine());

			} catch (NumberFormatException e) {
				System.out.println("Invalid selection. Numbers only please.");
				return;
			}

			if (rowNo < 1 || rowNo > allStudents.size()) {
				System.out.println("RowNo outside of range.");
				return;
			}

			temp = allStudents.get(rowNo - 1);

			System.out.println("Update student:");
			System.out.println("ID = " + temp.getId().toString());
			System.out.println("GroupID = " + temp.getGroupId().toString());

			System.out.print("Enter first name: ");
			temp.setStudentFirstName(keyInput.nextLine());

			System.out.print("Enter last name: ");
			temp.setStudentLastName(keyInput.nextLine());

			System.out.println();
			ArrayList<Student> tempArray = new ArrayList<Student>();
			tempArray.add(temp);
			studentDAO.update(tempArray);
			System.out.println(studentDAO.getQueryResultLog());
			
		}

	}

	private static void updateOneGroup() {
		Group temp = null;
		int rowNo = 0;

		List<Group> allGroups = groupDAO.getAll();
		System.out.println(groupDAO.getQueryResultLog());

		if (!allGroups.isEmpty()) {

			System.out.print("Enter RowNo: ");

			try {
				rowNo = Integer.parseInt(keyInput.nextLine());

			} catch (NumberFormatException e) {
				System.out.println("Invalid selection. Numbers only please.");
				return;
			}

			if (rowNo < 1 || rowNo > allGroups.size()) {
				System.out.println("RowNo outside of range.");
				return;
			}
			
			System.out.println();
			temp = allGroups.get(rowNo - 1);
			System.out.println("Update Group:");
			System.out.println("ID = " + temp.getId().toString());
			System.out.print("Enter group name: ");
			temp.setGroupName(keyInput.nextLine());

			ArrayList<Group> tempArray = new ArrayList<Group>();
			tempArray.add(temp);
			groupDAO.update(tempArray);
			System.out.println(groupDAO.getQueryResultLog());
		}

	}

	private static void updateOneCourse() {
		Course temp = null;
		int rowNo = 0;

		List<Course> allCourses = courseDAO.getAll();
		System.out.println(courseDAO.getQueryResultLog());

		if (!allCourses.isEmpty()) {

			System.out.print("Enter RowNo: ");

			try {
				rowNo = Integer.parseInt(keyInput.nextLine());

			} catch (NumberFormatException e) {
				System.out.println("Invalid selection. Numbers only please.");
				return;
			}

			if (rowNo < 1 || rowNo > allCourses.size()) {
				System.out.println("RowNo outside of range.");
				return;
			}

			temp = allCourses.get(rowNo - 1);

			System.out.println("Update course:");
			System.out.println("ID = " + temp.getId().toString());
			System.out.print("Enter course name: ");
			temp.setCourseName(keyInput.nextLine());
			System.out.print("Enter course description: ");
			temp.setCourseDescription(keyInput.nextLine());
			
			System.out.println();
			ArrayList<Course> tempArray = new ArrayList<Course>();
			tempArray.add(temp);
			courseDAO.update(tempArray);
			System.out.println(courseDAO.getQueryResultLog());
		}
	}

	private static void runTask7A() {
//	"a. Find all groups with less or equals student count
		int studentCount = 0;
		System.out.print("Enter student count: ");
		
		try {
			studentCount = Integer.parseInt(keyInput.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Invalid selection. Numbers only please.");
			return;
		}

		if (studentCount < 0) {
			System.out.println("Student count must be greater then zero.");
			return;
		} else {
		
		String query =  "SELECT " + EOL
				+ "\"group\".id," + EOL
				+ "\"group\".group_name,"
				+ "COUNT(*) AS \"Student Count\"" + EOL
				+ "FROM \"group\"" + EOL
				+ "INNER JOIN student" + EOL
				+ "ON  student.group_id = \"group\".id" + EOL
				+ "GROUP BY \"group\".id" + EOL
				+ "HAVING COUNT(*) <= CAST( ? AS INT) ;"; 
		
		List<String> params = new ArrayList<String>();
		params.add(String.valueOf(studentCount));
		groupDAO.executeQuery(query, params);
		System.out.println(groupDAO.getQueryResultLog());
	}

}

	private static void runTask7B() {
//		"b. Find all students related to course with given name"
		String courseName = "";
		
		String query = "SELECT * " + EOL
				+ "FROM" + EOL
				+ "course ;";

		List<String> params = new ArrayList<String>();
		courseDAO.executeQuery(query, params);
		System.out.println(courseDAO.getQueryResultLog());
		System.out.println();
		
		System.out.print("Enter course name:");
		courseName = keyInput.nextLine().trim();;
		params.clear();
		params.add(courseName);
		courseDAO.get(" WHERE course_name = ? ;", params);
		System.out.println(courseDAO.getQueryResultLog());
	}
	
	private static void runTask7C() {
//		c. Add new student"
		insertOneStudent();
	}
	
	private static void runTask7D() {
//		"d. Delete student by STUDENT_ID"
		deleteOneStudent();
	}

	private static void runTask7E() {
//		e. Add a student to the course (from a list)
		int rowNo = 0;
		
		List<Student> allStudents = studentDAO.getAll();
		System.out.println(studentDAO.getQueryResultLog());
		
		System.out.println("Choose student - enter RowNo:");
		
		try {
			rowNo = Integer.parseInt(keyInput.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Invalid selection. Numbers only please.");
			return;
		}

		if (rowNo < 1 || rowNo > allStudents.size()) {
			System.out.println("RowNo outside of range.");
			return;
		}

		Student student = allStudents.get(rowNo - 1); 

		List<Course> allCourses = courseDAO.getAll();

		String query = "SELECT * " + EOL
				+ "FROM" + EOL
				+ "course ;";

		List<String> params = new ArrayList<String>();
		courseDAO.executeQuery(query, params);
		System.out.println(courseDAO.getQueryResultLog());
		
		System.out.println("Choose course - enter RowNo:");
		
		try {
			rowNo = Integer.parseInt(keyInput.nextLine());

		} catch (NumberFormatException e) {
			System.out.println("Invalid selection. Numbers only please.");
			return;
		}

		if (rowNo < 1 || rowNo > allStudents.size()) {
			System.out.println("RowNo outside of range.");
			return;
		}

		Course course = allCourses.get(rowNo - 1);
		
		course.enroll(student);
		
		List<Course> updatedCourses = new ArrayList<Course>();
		updatedCourses.add(course);
		courseDAO.update(updatedCourses);
		System.out.println(courseDAO.getQueryResultLog());
		
		params.clear();
		params.add(course.getId().toString());
		courseDAO.get(" WHERE id IN ( ?::uuid ) ", params);
		
		System.out.println(courseDAO.getQueryResultLog());
	}
	
	private static void runTask7F() {
//		Menu task7F = new MenuItem("f. Remove the student from one of his or her courses", () -> runTask7B());
		int rowNo = 0;
		
		List<Student> allStudents = studentDAO.getAll();
		System.out.println(studentDAO.getQueryResultLog());
		
		System.out.println("Choose student - enter RowNo:");
		
		try {
			rowNo = Integer.parseInt(keyInput.nextLine());
		} catch (NumberFormatException e) {
			System.out.println("Invalid selection. Numbers only please.");
			return;
		}

		if (rowNo < 1 || rowNo > allStudents.size()) {
			System.out.println("RowNo outside of range.");
			return;
		}

		Student student = allStudents.get(rowNo - 1); 
		List<String> params = new ArrayList<String>();
		params.add(student.getId().toString());
		String query = "SELECT " + EOL
				+ "course.id," + EOL
				+ "course.course_name," + EOL 
				+ "course.course_description," + EOL
				+ "students_on_course.student_id" + EOL
				+ "FROM" + EOL
				+ "course" + EOL
				+ "RIGHT JOIN" + EOL 
				+ "students_on_course" + EOL
				+ "ON" + EOL
				+ " course.id = students_on_course.course_id " + EOL
				+ "WHERE" + EOL
				+ " students_on_course.student_id = ?::uuid " + EOL; 

		courseDAO.executeQuery(query, params);
		System.out.println(courseDAO.getQueryResultLog());
		
		System.out.println("Enter course name from wich you wish dropout student");
		String courseName = keyInput.nextLine().trim();
				
		params.clear();
		params.add(courseName);
		List<Course> courses = courseDAO.get(" WHERE course_name = ? ;", params);
		
		if (courses.isEmpty()) {
		System.out.println(courseDAO.getQueryResultLog());
		System.out.println("Course with name - " + courseName + " not found");
		return;
		}
		
		Course course = courses.get(0);
		course.dropout(student);
		courseDAO.update(courses);
		System.out.println(courseDAO.getQueryResultLog());
		
		params.clear();
		params.add(student.getId().toString());
		courseDAO.executeQuery(query, params);
		System.out.println(courseDAO.getQueryResultLog());
		
	}
	
	private static void runBencmark() throws IOException {
		int numberOfStudents = 300_000; 
		long startTime = 0;
		long elapsed = 0;
		List<Student> actualStudentArray = new ArrayList<Student>();
		
		ArrayList<String> studentsNames = dataGenerator.getStudentNames(numberOfStudents);
		ArrayList<String> studentSurNames = dataGenerator.getStudentSurNames(numberOfStudents);
		ArrayList<Student>students = new ArrayList<Student>();
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
				t.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		elapsed = System.currentTimeMillis() - startTime;
		System.out.println("Elapsed " + ((double) elapsed / 1000) + " seconds.");
		studentDAO.deleteAll();
	}
}
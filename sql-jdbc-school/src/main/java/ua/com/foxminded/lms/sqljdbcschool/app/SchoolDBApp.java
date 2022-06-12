package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.spring.SpringContextConfig;

public class SchoolDBApp {
	private static SchoolDAO  schoolDAO = null;
	private static Scanner in = new Scanner(System.in);
	private static PrintWriter out = new PrintWriter(System.out, true);

	public static void main(String[] args) throws IOException, SQLException {

		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(SpringContextConfig.class);
		applicationContext.refresh();

		schoolDAO = applicationContext.getBean("dao", SchoolDAO.class);
		prepareDB();

		Menu appMenu = new Menu(in, out, "Choose an option");

		FindGroupsStudentCountLessOrEquals findGroupsStudentCountLessOrEquals = applicationContext
				.getBean(FindGroupsStudentCountLessOrEquals.class, in, out);
		findGroupsStudentCountLessOrEquals.setName("Find all groups with less or equals student count");

		FindStudentsByCourseName findStudentsByCourseName = applicationContext.getBean(FindStudentsByCourseName.class,
				in, out);
		findStudentsByCourseName.setName("Find all students related to course with given name");

		AddStudent addStudent = applicationContext.getBean(AddStudent.class, in, out);
		addStudent.setName("Add new student");

		DeleteStudent deleteStudent = applicationContext.getBean(DeleteStudent.class, in, out);
		deleteStudent.setName("Delete student by STUDENT_ID");

		AddStudentToCourse addStudentToCourse = applicationContext.getBean(AddStudentToCourse.class, in, out);
		addStudentToCourse.setName("Add a student to the course (from a list)");

		DropoutStudentFromCourse dropoutStudentFromCourse = applicationContext.getBean(DropoutStudentFromCourse.class,
				in, out);
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
		
		applicationContext.close();
	}

	private static void prepareDB() throws SQLException, IOException {

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
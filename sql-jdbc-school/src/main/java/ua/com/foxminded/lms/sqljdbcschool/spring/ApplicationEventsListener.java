package ua.com.foxminded.lms.sqljdbcschool.spring;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import ua.com.foxminded.lms.sqljdbcschool.app.AddStudent;
import ua.com.foxminded.lms.sqljdbcschool.app.AddStudentToCourse;
import ua.com.foxminded.lms.sqljdbcschool.app.DeleteStudent;
import ua.com.foxminded.lms.sqljdbcschool.app.DropoutStudentFromCourse;
import ua.com.foxminded.lms.sqljdbcschool.app.EntitiesGenerator;
import ua.com.foxminded.lms.sqljdbcschool.app.FindGroupsStudentCountLessOrEquals;
import ua.com.foxminded.lms.sqljdbcschool.app.FindStudentsByCourseName;
import ua.com.foxminded.lms.sqljdbcschool.app.Menu;
import ua.com.foxminded.lms.sqljdbcschool.dao.DBConnectionPool;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDBInitializer;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;


@Component
public class ApplicationEventsListener implements ApplicationListener<ApplicationContextEvent> {
	private Scanner in = new Scanner(System.in);
	private PrintWriter out = new PrintWriter(System.out, true);
	private int initPoolSize = 5;
	private FileLoader fileLoader = new FileLoader();
	private URL propertiesURL = ClassPathResource.class.getResource("/db.posgresql.properties");
	private Properties conectionProperties = new Properties();
	private Function<Properties, DBConnectionPool> initPool = (properties) -> {
		try {
			properties.load(fileLoader.loadProperties(propertiesURL));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new DBConnectionPool(properties, initPoolSize);
	};
	
	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
 		if (event instanceof ContextRefreshedEvent) {
			init((ContextRefreshedEvent) event);
		}
		
		if (event instanceof ContextClosedEvent) {
			destroy((ContextClosedEvent) event);
		}
		
	}
	
	private void init( ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();

		DBConnectionPool connectionPool = context.getBean(DBConnectionPool.class, initPool.apply(conectionProperties));

		SchoolDBInitializer schoolDBInitializer = context.getBean(SchoolDBInitializer.class, connectionPool);
		try {
			schoolDBInitializer.dropTables();
			schoolDBInitializer.createTables();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		
		SchoolDAO dao = context.getBean(SchoolDAO.class, connectionPool);

		EntitiesGenerator entitiesGenerator;
		try {
			entitiesGenerator = new EntitiesGenerator();
			List<Group> groups = entitiesGenerator.getRandomGroups();
			List<Student> students = entitiesGenerator.getRandomStudents();
			List<Course> courses = entitiesGenerator.getRandomCourses();
			entitiesGenerator.randomEnrollStudentsToGroups(students, groups);
			ConcurrentHashMap<String, List<Student>> enrolledStudents = entitiesGenerator
					.randomEnrollStudentsToCourses(students, courses);

			dao.insertGroups(groups);
			dao.insertStudents(students);
			dao.insertCourses(courses);
			enrolledStudents.forEach((courseUuid, studentList) -> studentList.parallelStream()
					.forEach(student -> dao.addStudentToCourse(student.getUuid(), courseUuid)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		AddStudent addStudent = context.getBean(AddStudent.class, in, out);
		addStudent.setName("Add new student");
		
		FindGroupsStudentCountLessOrEquals findGroupsStudentCountLessOrEquals = context
				.getBean(FindGroupsStudentCountLessOrEquals.class, in, out);
		findGroupsStudentCountLessOrEquals.setName("Find all groups with less or equals student count");

		FindStudentsByCourseName findStudentsByCourseName = context.getBean(FindStudentsByCourseName.class,
				in, out);
		findStudentsByCourseName.setName("Find all students related to course with given name");
		
		DeleteStudent deleteStudent = context.getBean(DeleteStudent.class, in, out);
		deleteStudent.setName("Delete student by STUDENT_ID");

		AddStudentToCourse addStudentToCourse = context.getBean(AddStudentToCourse.class, in, out);
		addStudentToCourse.setName("Add a student to the course (from a list)");

		DropoutStudentFromCourse dropoutStudentFromCourse = context.getBean(DropoutStudentFromCourse.class,
				in, out);
		dropoutStudentFromCourse.setName("Remove the student from one of his or her courses");
		
		Menu appMenu = context.getBean(Menu.class, in, out, "Choose an option");
		appMenu.addMenuOption(findGroupsStudentCountLessOrEquals);
		appMenu.addMenuOption(findStudentsByCourseName);
		appMenu.addMenuOption(addStudent);
		appMenu.addMenuOption(deleteStudent);
		appMenu.addMenuOption(addStudentToCourse);
		appMenu.addMenuOption(dropoutStudentFromCourse);
	}	

	private void destroy(ContextClosedEvent event) {
		try {
			ApplicationContext context = event.getApplicationContext();
			context.getBean(DBConnectionPool.class).closeConnections();
			in.close();
			out.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}

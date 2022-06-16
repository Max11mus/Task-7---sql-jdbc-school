package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDBInitializer;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.spring.SpringContextConfig;

public class SchoolDBApp {

	public static void main(String[] args) throws IOException, SQLException {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(SpringContextConfig.class);
		applicationContext.refresh();
		
		SchoolDBInitializer schoolDBInitializer = applicationContext.getBean(SchoolDBInitializer.class);
		schoolDBInitializer.dropTables();
		schoolDBInitializer.createTables();
		
		SchoolDAO schoolDAO = applicationContext.getBean(SchoolDAO.class);
		prepareDB(schoolDAO);

		Menu appMenu = applicationContext.getBean(Menu.class);
		FindGroupsStudentCountLessOrEquals findGroupsStudentCountLessOrEquals = applicationContext
				.getBean(FindGroupsStudentCountLessOrEquals.class);
		FindStudentsByCourseName findStudentsByCourseName = applicationContext.getBean(FindStudentsByCourseName.class);
		AddStudent addStudent = applicationContext.getBean(AddStudent.class);
		DeleteStudent deleteStudent = applicationContext.getBean(DeleteStudent.class);
		AddStudentToCourse addStudentToCourse = applicationContext.getBean(AddStudentToCourse.class);
		DropoutStudentFromCourse dropoutStudentFromCourse = applicationContext.getBean(DropoutStudentFromCourse.class);
		
		appMenu.addMenuOption(findGroupsStudentCountLessOrEquals);
		appMenu.addMenuOption(findStudentsByCourseName);
		appMenu.addMenuOption(addStudent);
		appMenu.addMenuOption(deleteStudent);
		appMenu.addMenuOption(addStudentToCourse);
		appMenu.addMenuOption(dropoutStudentFromCourse);
		appMenu.runCycle();
		
		applicationContext.getBean("closeResourses");
		applicationContext.close();
	}

	private static void prepareDB(SchoolDAO schoolDAO) throws SQLException, IOException {
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
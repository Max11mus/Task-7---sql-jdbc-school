package ua.com.foxminded.lms.sqljdbcschool.spring;

import java.io.PrintWriter;
import java.util.Scanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import ua.com.foxminded.lms.sqljdbcschool.app.AddStudent;
import ua.com.foxminded.lms.sqljdbcschool.app.AddStudentToCourse;
import ua.com.foxminded.lms.sqljdbcschool.app.DeleteStudent;
import ua.com.foxminded.lms.sqljdbcschool.app.DropoutStudentFromCourse;
import ua.com.foxminded.lms.sqljdbcschool.app.FindGroupsStudentCountLessOrEquals;
import ua.com.foxminded.lms.sqljdbcschool.app.FindStudentsByCourseName;
import ua.com.foxminded.lms.sqljdbcschool.app.Menu;
import ua.com.foxminded.lms.sqljdbcschool.dao.DBConnectionPool;

@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.lms.sqljdbcschool.spring, ua.com.foxminded.lms.sqljdbcschool.app,"
		+ " ua.com.foxminded.lms.sqljdbcschool.dao")
public class SpringContextConfig {

	@Bean
	@Lazy
	public DBConnectionPool dBConnectionPool(DBConnectionPool init) {
		return init;
	}

	@Bean
	@Lazy
	public AddStudent addStudent(Scanner in, PrintWriter out) {
		AddStudent addStudent = new AddStudent(in, out);
		return addStudent;
	}

	@Bean
	@Lazy
	public AddStudentToCourse addStudentToCourse(Scanner in, PrintWriter out) {
		AddStudentToCourse addStudentToCourse = new AddStudentToCourse(in, out);
		return addStudentToCourse;
	}

	@Bean
	@Lazy
	public DeleteStudent deleteStudent(Scanner in, PrintWriter out) {
		DeleteStudent deleteStudent = new DeleteStudent(in, out);
		return deleteStudent;
	}

	@Bean
	@Lazy
	public DropoutStudentFromCourse dropoutStudentFromCourse(Scanner in, PrintWriter out) {
		DropoutStudentFromCourse dropoutStudentFromCourse = new DropoutStudentFromCourse(in, out);
		return dropoutStudentFromCourse;
	}

	@Bean
	@Lazy
	public FindGroupsStudentCountLessOrEquals findGroupsStudentCountLessOrEquals(Scanner in, PrintWriter out) {
		FindGroupsStudentCountLessOrEquals findGroupsStudentCountLessOrEquals = new FindGroupsStudentCountLessOrEquals(
				in, out);
		return findGroupsStudentCountLessOrEquals;
	}
	
	@Bean
	@Lazy
	public Menu menu(Scanner in, PrintWriter out, String Name) {
		return new Menu(in, out, "Choose an option");
	}

	@Bean
	@Lazy
	public FindStudentsByCourseName findStudentsByCourseName(Scanner in, PrintWriter out) {
		FindStudentsByCourseName findStudentsByCourseName = new FindStudentsByCourseName(in, out);
		return findStudentsByCourseName;
	}

}

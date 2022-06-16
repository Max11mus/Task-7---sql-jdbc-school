package ua.com.foxminded.lms.sqljdbcschool.spring;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.util.function.Function;

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
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.lms.sqljdbcschool.app, ua.com.foxminded.lms.sqljdbcschool.dao")
public class SpringContextConfig {
	private Scanner in = new Scanner(System.in);
	private PrintWriter out = new PrintWriter(System.out, true);
	private int initPoolSize = 5;
	private FileLoader fileLoader = new FileLoader();
	private URL propertiesURL = ClassLoader.getSystemResource("db.posgresql.properties");
	private Properties conectionProperties = new Properties();
	private Function<Properties, DBConnectionPool> initPool = (properties) -> {
		try {
			properties.load(fileLoader.loadProperties(propertiesURL));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new DBConnectionPool(properties, initPoolSize);
	};
	private DBConnectionPool connectionPool = initPool.apply(conectionProperties);

	@Bean
	public DBConnectionPool dBConnectionPool() {
		return connectionPool;
	}

	@Bean
	public AddStudent addStudent() {
		AddStudent addStudent = new AddStudent(in, out);
		addStudent.setName("Add new student");
		return addStudent;
	}

	@Bean
	public AddStudentToCourse addStudentToCourse() {
		AddStudentToCourse addStudentToCourse = new AddStudentToCourse(in, out);
		addStudentToCourse.setName("Add a student to the course (from a list)");
		return addStudentToCourse;
	}

	@Bean
	public DeleteStudent deleteStudent() {
		DeleteStudent deleteStudent = new DeleteStudent(in, out);
		deleteStudent.setName("Delete student by STUDENT_ID");
		return deleteStudent;
	}

	@Bean
	public DropoutStudentFromCourse dropoutStudentFromCourse() {
		DropoutStudentFromCourse dropoutStudentFromCourse = new DropoutStudentFromCourse(in, out);
		dropoutStudentFromCourse.setName("Remove the student from one of his or her courses");
		return dropoutStudentFromCourse;
	}

	@Bean
	public FindGroupsStudentCountLessOrEquals findGroupsStudentCountLessOrEquals() {
		FindGroupsStudentCountLessOrEquals findGroupsStudentCountLessOrEquals = new FindGroupsStudentCountLessOrEquals(
				in, out);
		findGroupsStudentCountLessOrEquals.setName("Find all groups with less or equals student count");
		return findGroupsStudentCountLessOrEquals;
	}
	
	@Bean
	public Menu menu() {
		return new Menu(in, out, "Choose an option");
	}

	@Bean
	public FindStudentsByCourseName findStudentsByCourseName() {
		FindStudentsByCourseName findStudentsByCourseName = new FindStudentsByCourseName(in, out);
		findStudentsByCourseName.setName("Find all students related to course with given name");
		return findStudentsByCourseName;
	}

	@Bean
	@Lazy
	public void closeResourses() {
		in.close();
		out.close();
		try {
			connectionPool.closeConnections();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

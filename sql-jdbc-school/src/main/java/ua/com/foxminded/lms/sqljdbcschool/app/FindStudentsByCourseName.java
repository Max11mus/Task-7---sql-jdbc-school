package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;

@Component
@Scope("prototype")
public class FindStudentsByCourseName extends ConsoleMenuCommand {
	@Autowired
	public FindStudentsByCourseName(Scanner input, PrintWriter output) {
		super(input, output);
	}

	@Override
	public void run() {
		output.println();
		String courseName;
		Course course = null;

		List<Course> allCourses = dao.getAllCourses();
		output.println();

		output.println("Enter course name:");
		courseName = input.nextLine().trim();
		course = allCourses.stream().filter(e -> e.getCourseName().equals(courseName)).findAny().orElse(null);

		if (course == null) {
			output.println("Course with name " + courseName + " not present.");
		} else {
			dao.findStudentsByCourseID(course.getUuid());
		}

	}

}

package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;

public class FindStudentsByCourseName extends ConsoleMenuCommand {
	public FindStudentsByCourseName(Scanner input, PrintWriter output, SchoolDAO dao) {
		super(input, output, dao);
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
			dao.FindStudentsByCourseID(course.getUuid());
		}

	}

}

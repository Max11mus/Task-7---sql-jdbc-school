package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class AddStudentToCourse extends ConsoleMenuCommand {
	public AddStudentToCourse(Scanner input, PrintWriter output) {
		super(input, output);
	}

	@Override
	public void run() {
		int rowNo = 0;
		output.println();

		List<Student> allStudents = dao.getAllStudents();

		output.println("Choose student - enter RowNo:");

		rowNo = inputIntFromRange(1, allStudents.size());

		Student student = allStudents.get(rowNo - 1);

		List<Course> allCourses = dao.getAllCourses();

		output.println("Choose course - enter RowNo:");

		rowNo = inputIntFromRange(1, allCourses.size());

		Course course = allCourses.get(rowNo - 1);

		dao.addStudentToCourse(student.getUuid(), course.getUuid());
	}

}

package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

@Component
@Scope("prototype")
public class AddStudentToCourse extends ConsoleMenuCommand {
	@Autowired
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

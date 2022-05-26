package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class DropoutStudentFromCourse extends ConsoleMenuCommand {
	public DropoutStudentFromCourse(Scanner input, PrintWriter output, SchoolDAO dao) {
		super(input, output, dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		
		int rowNo = 0;
		dao.setEnableOutputToConsole(true);
		output.println();
		
		List<Student> allStudents = dao.getAllStudents();
		
		System.out.println("Choose student - enter RowNo:");
		
		rowNo = inputIntFromRange(1, allStudents.size());
		
		Student student = allStudents.get(rowNo - 1); 

		List<Course> studentCourses = dao.findStudentCourses(student.getUuid());
		
		if (studentCourses.isEmpty()) {
			return;
		}
		
		output.println("Choose course - enter RowNo:");
		
		rowNo = inputIntFromRange(1, studentCourses.size());
		
		Course course = studentCourses.get(rowNo - 1);
		
		dao.dropoutStudentFromCourse(student.getUuid(), course.getUuid());
	}

}
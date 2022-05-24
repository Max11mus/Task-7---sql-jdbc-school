package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class DropoutStudentFromCourse extends Command {
	public DropoutStudentFromCourse(Scanner input, PrintWriter output, SchoolDAO dao) {
		super(input, output, dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		
		int rowNo = 0;
		dao.setEnableLogging(true);
		output.println();
		
		List<Student> allStudents = dao.getAllStudents();
		output.println(dao.getQueryResultLog());
		
		System.out.println("Choose student - enter RowNo:");
		
		try {
			rowNo = Integer.parseInt(input.nextLine());
		} catch (NumberFormatException e) {
			output.println("Invalid selection. Numbers only please.");
			return;
		}

		if (rowNo < 1 || rowNo > allStudents.size()) {
			output.println("RowNo outside of range.");
			return;
		}

		Student student = allStudents.get(rowNo - 1); 

		List<Course> studentCourses = dao.findStudentCourses(student.getId());
		output.println(dao.getQueryResultLog());
		
		output.println("Choose course - enter RowNo:");
		
		try {
			rowNo = Integer.parseInt(input.nextLine());

		} catch (NumberFormatException e) {
			output.println("Invalid selection. Numbers only please.");
			return;
		}

		if (rowNo < 1 || rowNo > studentCourses.size()) {
			output.println("RowNo outside of range.");
			return;
		}

		Course course = studentCourses.get(rowNo - 1);
		
		dao.dropoutStudentFromCourse(student.getId(), course.getId());
		output.println(dao.getQueryResultLog());
	}

}

package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.Scanner;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class AddStudent extends Command {
	public AddStudent(Scanner input, PrintWriter output, SchoolDAO dao) {
		super(input, output, dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		output.println();
		Student student = new Student();
		output.println("Insert new student:");
		output.println("ID = " + student.getId().toString());
		output.println("GroupID = " + student.getGroupId().toString());

		output.println("Enter first name: ");
		student.setStudentFirstName(input.nextLine());

		output.println("Enter last name: ");
		student.setStudentLastName(input.nextLine());

		output.println();
		dao.setEnableLogging(true);
		dao.insert(student);
		output.println(dao.getQueryResultLog());

	}

}

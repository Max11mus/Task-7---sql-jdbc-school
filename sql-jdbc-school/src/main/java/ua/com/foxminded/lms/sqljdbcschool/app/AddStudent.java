package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.Scanner;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class AddStudent extends ConsoleMenuCommand {
	public AddStudent(Scanner input, PrintWriter output) {
		super(input, output);
	}

	@Override
	public void run() {
		output.println();
		Student student = new Student();
		output.println("Insert new student:");
		output.println("UUID = " + student.getUuid().toString());
		output.println("GroupID = " + student.getGroupUuid());

		output.println("Enter first name: ");
		student.setFirstName(input.nextLine());

		output.println("Enter last name: ");
		student.setLastName(input.nextLine());

		output.println();
		dao.insertStudent(student);

	}

}

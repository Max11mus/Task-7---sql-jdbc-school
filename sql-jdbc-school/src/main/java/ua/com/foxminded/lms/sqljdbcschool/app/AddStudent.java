package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

@Component
@Scope("prototype")
public class AddStudent extends ConsoleMenuCommand {
	@Autowired
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

package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class FindGroupsStudentCountLessOrEquals extends ConsoleMenuCommand {
	@Autowired
	public FindGroupsStudentCountLessOrEquals(Scanner input, PrintWriter output) {
		super(input, output);
	}

	@Override
	public void run() {
		output.println();
		int studentCount = 0;

		output.println("Enter student count: ");
		try {
			studentCount = Integer.parseInt(input.nextLine());
		} catch (NumberFormatException e) {
			output.println("Invalid selection. Numbers only please.");
			return;
		}

		if (studentCount < 0) {
			System.out.println("Student count must be greater then zero.");
			return;
		} else {
			dao.findGroupsStudentCountLessOrEquals(studentCount);

		}
	}

}

package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.Scanner;

public class FindGroupsStudentCountLessOrEquals extends ConsoleMenuCommand {
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

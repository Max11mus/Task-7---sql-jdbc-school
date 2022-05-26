package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class FindGroupsStudentCountLessOrEquals extends ConsoleMenuCommand {
	public FindGroupsStudentCountLessOrEquals(Scanner input, PrintWriter output, SchoolDAO dao) {
		super(input, output, dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		output.println();
		dao.setEnableOutputToConsole(true);
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

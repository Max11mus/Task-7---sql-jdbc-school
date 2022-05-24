package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class DeleteStudent extends Command {
	public DeleteStudent(Scanner input, PrintWriter output, SchoolDAO dao) {
		super(input, output, dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		Student student = null;
		int rowNo = 0;
		output.println();
		
		dao.setEnableLogging(true);
		List<Student> allStudents = dao.getAllStudents();
		output.println(dao.getQueryResultLog());

		if (!allStudents.isEmpty()) {

			output.println("Enter RowNo: ");

			try {
				rowNo = Integer.parseInt(input.nextLine());

			} catch (NumberFormatException e) {
				output.println("Invalid selection. Numbers only please.");
				return;
			}

			if (rowNo < 1 || rowNo > allStudents.size()) {
				System.out.println("RowNo outside of range.");
				return;
			}

			output.println();
			student = allStudents.get(rowNo - 1);
			dao.deleteStudent(student.getId());
			output.println(dao.getQueryResultLog());
			
		}
	}

}

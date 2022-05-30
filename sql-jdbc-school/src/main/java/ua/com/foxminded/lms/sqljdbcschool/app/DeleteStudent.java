package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class DeleteStudent extends ConsoleMenuCommand {
	public DeleteStudent(Scanner input, PrintWriter output, SchoolDAO dao) {
		super(input, output, dao);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		Student student = null;
		int rowNo = 0;
		output.println();
		
		List<Student> allStudents = dao.getAllStudents();

		if (!allStudents.isEmpty()) {

			output.println("Enter RowNo: ");

			rowNo = inputIntFromRange(1, allStudents.size());

			output.println();
			student = allStudents.get(rowNo - 1);
			dao.deleteStudent(student.getUuid());
			
		}
	}

}

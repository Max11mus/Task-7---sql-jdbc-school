package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

@Component
@Scope("prototype")
public class DeleteStudent extends ConsoleMenuCommand {
	@Autowired
	public DeleteStudent(Scanner input, PrintWriter output) {
		super(input, output);
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

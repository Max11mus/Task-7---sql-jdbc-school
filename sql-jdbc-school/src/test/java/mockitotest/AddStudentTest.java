package mockitotest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import ua.com.foxminded.lms.sqljdbcschool.app.AddStudent;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class AddStudentTest {
	final String EOL = System.lineSeparator();
	
	@Test
	void run_AfterExecute_WorkFlowAndResultMustBeAsExpected() {
		//given
		String studentFirstName = "John";
		String studentLastName = "Lennon";
		Scanner input = new Scanner(
				new ByteArrayInputStream((studentFirstName + EOL + studentLastName + EOL).getBytes()));

		ByteArrayOutputStream outStream =  new ByteArrayOutputStream();
		PrintWriter output = new PrintWriter(outStream);
		
		SchoolDAO dao = Mockito.mock(SchoolDAO.class);
		
		AddStudent addStudent = new AddStudent(input, output, dao);
		
		List<String> expectedOutput = Stream
				.of("", "Insert new student:", "UUID = ", "GroupID = null", "Enter first name: ", "Enter last name: ")
				.collect(Collectors.toList()); 
		
		//when
		addStudent.run();
		
		//then
		output.flush();
		List<String> actualOutput = Arrays.asList(outStream.toString().split(EOL));
		assertEquals(expectedOutput.size(), actualOutput.size());
		int index = 0;
		assertEquals(expectedOutput.get(index), actualOutput.get(index++));
		assertEquals(expectedOutput.get(index), actualOutput.get(index++));
		assertEquals(expectedOutput.get(index), actualOutput.get(index++).substring(0, 7));
		assertEquals(expectedOutput.get(index), actualOutput.get(index++));
		assertEquals(expectedOutput.get(index), actualOutput.get(index++));
		assertEquals(expectedOutput.get(index), actualOutput.get(index++));
		
		InOrder daoOrder = Mockito.inOrder(dao);
		daoOrder.verify(dao)
				.insertStudent(new Student(actualOutput.get(2).substring(7), null, studentFirstName, studentLastName));
	}
}

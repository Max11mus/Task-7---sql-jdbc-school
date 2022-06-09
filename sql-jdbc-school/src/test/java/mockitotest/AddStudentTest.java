package mockitotest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
		
		//when
		addStudent.run();
		
		//then
		output.flush();
		List<String> actualOutput = Arrays.asList(outStream.toString().split(EOL));
		assertEquals(6, actualOutput.size());
		int index = 0;
		assertEquals("", actualOutput.get(index++));
		assertEquals("Insert new student:", actualOutput.get(index++));
		assertEquals("UUID = ", actualOutput.get(index++).substring(0,7));
		assertEquals("GroupID = null", actualOutput.get(index++));
		assertEquals("Enter first name: ", actualOutput.get(index++));
		assertEquals("Enter last name: ", actualOutput.get(index++));
		
		InOrder daoOrder = Mockito.inOrder(dao);
		Student expectedStudent = new Student(actualOutput.get(2).substring(7), null, studentFirstName,
				studentLastName); 
		daoOrder.verify(dao).insertStudent(expectedStudent);
	}
}

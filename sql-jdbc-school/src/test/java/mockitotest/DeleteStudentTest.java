package mockitotest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import ua.com.foxminded.lms.sqljdbcschool.app.DeleteStudent;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class DeleteStudentTest {
	final String EOL = System.lineSeparator();

	@Test
	public void run_AfterExecute_WorkFlowAndResultMustBeAsExpected() {
		//given
		String studentUuid = "9723a706-edd1-4ea9-8629-70a91504ab2a";
		String studentFirstName = "John";
		String studentLastName = "Lennon";
		Student student = new Student(studentUuid, null, studentFirstName,studentLastName);
		List<Student> students = new ArrayList<Student>();
		students.add(student);
		int studentChoose = 1; 
		
		Scanner input = new Scanner(
				new ByteArrayInputStream((studentChoose + EOL).getBytes()));

		ByteArrayOutputStream outStream =  new ByteArrayOutputStream();
		PrintWriter output = new PrintWriter(outStream);
		
		SchoolDAO dao = Mockito.mock(SchoolDAO.class);
		
		DeleteStudent deleteStudent = new DeleteStudent(input, output, dao);  
		when(dao.getAllStudents()).thenReturn(students);
		
		//when
		deleteStudent.run();

		//then
		output.flush();
		List<String> actualOutput = Arrays.asList(outStream.toString().split(EOL));
		assertEquals(2, actualOutput.size());
		int index = 0;
		assertEquals("", actualOutput.get(index++));
		assertEquals("Enter RowNo: ", actualOutput.get(index++));
		
		InOrder daoOrder = Mockito.inOrder(dao);
		daoOrder.verify(dao).getAllStudents();
		daoOrder.verify(dao).deleteStudent(studentUuid);
		}	
}

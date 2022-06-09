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

import ua.com.foxminded.lms.sqljdbcschool.app.FindGroupsStudentCountLessOrEquals;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;

public class FindGroupsStudentCountLessOrEqualsTest {
	final String EOL = System.lineSeparator();

	@Test
	public void run_AfterExecute_WorkFlowAndResultMustBeAsExpected() {
		//given
		int studentCount = 1; 

		Scanner input = new Scanner(
				new ByteArrayInputStream((studentCount + EOL).getBytes()));

		ByteArrayOutputStream outStream =  new ByteArrayOutputStream();
		PrintWriter output = new PrintWriter(outStream);
		
		SchoolDAO dao = Mockito.mock(SchoolDAO.class);
		
		FindGroupsStudentCountLessOrEquals findGroupsStudentCountLessOrEquals = new FindGroupsStudentCountLessOrEquals(
				input, output, dao);
		
		//when
		findGroupsStudentCountLessOrEquals.run();
		
		//then
		output.flush();
		List<String> actualOutput = Arrays.asList(outStream.toString().split(EOL));
		assertEquals(2, actualOutput.size());
		int index = 0;
		assertEquals("", actualOutput.get(index++));
		assertEquals("Enter student count: ", actualOutput.get(index++));
		
		InOrder daoOrder = Mockito.inOrder(dao);
		daoOrder.verify(dao).findGroupsStudentCountLessOrEquals(studentCount);
	}
}

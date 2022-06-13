package mockitotest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		
		List<String> expectedOutput = Stream
				.of("", "Enter student count: ")
				.collect(Collectors.toList());
		
		//when
		findGroupsStudentCountLessOrEquals.run();
		
		//then
		output.flush();
		List<String> actualOutput = Arrays.asList(outStream.toString().split(EOL));
		assertEquals(expectedOutput.size(), actualOutput.size());
		int index = 0;
		assertEquals(expectedOutput.get(index), actualOutput.get(index++));
		assertEquals(expectedOutput.get(index), actualOutput.get(index++));
		
		InOrder daoOrder = Mockito.inOrder(dao);
		daoOrder.verify(dao).findGroupsStudentCountLessOrEquals(studentCount);
	}
}

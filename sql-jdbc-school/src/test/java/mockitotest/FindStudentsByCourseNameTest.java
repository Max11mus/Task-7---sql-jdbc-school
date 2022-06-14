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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import ua.com.foxminded.lms.sqljdbcschool.app.FindStudentsByCourseName;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;

public class FindStudentsByCourseNameTest {
	final String EOL = System.lineSeparator();
	
	@Test
	public void run_AfterExecute_WorkFlowAndResultMustBeAsExpected() {
		//given
		String courseUuid = "7894f0de-5820-49bc-8562-b1240f0587b1";
		String courseName = "Music Theory";
		String courseDescription = "For Cool Guys";
		Course course = new Course(courseUuid, courseName,  courseDescription );
		List<Course> courses = new ArrayList<Course>();
		courses.add(course);
		
		
		Scanner input = new Scanner(new ByteArrayInputStream((courseName + EOL).getBytes()));

		ByteArrayOutputStream outStream =  new ByteArrayOutputStream();
		PrintWriter output = new PrintWriter(outStream);
		
		SchoolDAO dao = Mockito.mock(SchoolDAO.class);
		
		FindStudentsByCourseName findStudentsByCourseName = new FindStudentsByCourseName(input, output, dao);  
		
		List<String> expectedOutput = Stream
				.of("", "", "Enter course name:")
				.collect(Collectors.toList());
		
		when(dao.getAllCourses()).thenReturn(courses);
		
		//when
		findStudentsByCourseName.run();
		
		//then
		output.flush();
		List<String> actualOutput = Arrays.asList(outStream.toString().split(EOL));
		assertEquals(expectedOutput.size(), actualOutput.size());
		int index = 0;
		assertEquals(expectedOutput.get(index), actualOutput.get(index++));
		assertEquals(expectedOutput.get(index), actualOutput.get(index++));
		assertEquals(expectedOutput.get(index), actualOutput.get(index++));
		
		InOrder daoOrder = Mockito.inOrder(dao);
		daoOrder.verify(dao).getAllCourses();
		daoOrder.verify(dao).findStudentsByCourseID(courseUuid);
	}	
}

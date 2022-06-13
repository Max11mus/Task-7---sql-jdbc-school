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

import ua.com.foxminded.lms.sqljdbcschool.app.AddStudentToCourse;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class AddStudentToCourseTest {
	final String EOL = System.lineSeparator();
	
	@Test
	void run_AfterExecute_WorkFlowAndResultMustBeAsExpected() {
		// given
		String studentUuid = "2ea5cb92-65f2-48da-be32-410aaa5e7e67";
		String studentFirstName = "John";
		String studentLastName = "Lennon";
		Student student = new Student(studentUuid, null, studentFirstName, studentLastName);
		List<Student> students = new ArrayList<Student>();
		students.add(student);
		int studentChoose = 1; 
		
		String courseUuid = "c6609ba4-1036-4a7e-8ac1-d435c261e958";
		String courseName = "Music Thery";
		String courseDescription = "For Cool Guys";
		Course course = new Course(courseUuid, courseName, courseDescription);
		List<Course> courses = new ArrayList<Course>();
		courses.add(course);
		int courseChoose = 1; 
		
		Scanner input = new Scanner(
				new ByteArrayInputStream((studentChoose + EOL + courseChoose + EOL).getBytes()));

		ByteArrayOutputStream outStream =  new ByteArrayOutputStream();
		PrintWriter output = new PrintWriter(outStream);
		
		SchoolDAO dao = Mockito.mock(SchoolDAO.class);
		
		AddStudentToCourse addStudentToCourse = new AddStudentToCourse(input, output, dao);
		
		List<String> expectedOutput = Stream
				.of("", "Choose student - enter RowNo:", "Choose course - enter RowNo:")
				.collect(Collectors.toList());
		when(dao.getAllStudents()).thenReturn(students);
		when(dao.getAllCourses()).thenReturn(courses);

		//when
		addStudentToCourse.run();
		
		//then
		output.flush();
		List<String> actualOutput = Arrays.asList(outStream.toString().split(EOL));
		assertEquals(expectedOutput.size(), actualOutput.size());
		int index = 0;
		assertEquals(expectedOutput.get(index), actualOutput.get(index++));
		assertEquals(expectedOutput.get(index), actualOutput.get(index++));
		assertEquals(expectedOutput.get(index), actualOutput.get(index++));
		
		InOrder daoOrder = Mockito.inOrder(dao);
		daoOrder.verify(dao).getAllStudents();
		daoOrder.verify(dao).getAllCourses();
		daoOrder.verify(dao).addStudentToCourse(studentUuid, courseUuid);
	}
}

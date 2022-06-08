package mockitotest;

import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ua.com.foxminded.lms.sqljdbcschool.app.AddStudent;
import ua.com.foxminded.lms.sqljdbcschool.app.AddStudentToCourse;
import ua.com.foxminded.lms.sqljdbcschool.app.DeleteStudent;
import ua.com.foxminded.lms.sqljdbcschool.app.DropoutStudentFromCourse;
import ua.com.foxminded.lms.sqljdbcschool.app.FindGroupsStudentCountLessOrEquals;
import ua.com.foxminded.lms.sqljdbcschool.app.FindStudentsByCourseName;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;

@RunWith(MockitoJUnitRunner.class)
public class ConsoleCommandsTests {
	
	@Mock
	private SchoolDAO schoolDAO;
	
	@Mock
	private  Scanner input;
	
	@Mock
	protected PrintWriter output;
	
	@InjectMocks	
	private AddStudent addStudent = new AddStudent(input,output,schoolDAO);  
	
	@InjectMocks
	private AddStudentToCourse addStudentToCourse = new AddStudentToCourse(input, output, schoolDAO);
	
	@InjectMocks
	private DeleteStudent deleteStudent = new DeleteStudent(input, output, schoolDAO);
	
	@InjectMocks
	private DropoutStudentFromCourse dropoutStudentFromCourse = new DropoutStudentFromCourse(input, output, schoolDAO);
	
	@InjectMocks
	private FindGroupsStudentCountLessOrEquals findGroupsStudentCountLessOrEquals
			= new FindGroupsStudentCountLessOrEquals(input, output, schoolDAO);
	
	@InjectMocks
	private FindStudentsByCourseName findStudentsByCourseName = new FindStudentsByCourseName(input, output, schoolDAO);
	
	@BeforeAll
	public void setUpBeforeClass() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testAddStudent() {
		
	}

}

package ControllersTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.lms.sqljdbcschool.controllers.AddStudentToCourseController;
import ua.com.foxminded.lms.sqljdbcschool.dao.ApplicationEventsListener;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDBInitializer;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.utils.DBConnectionPool;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@WebAppConfiguration
class AddStudentToCourseControllerTest {

	@Autowired 
	SchoolDAO schoolDAO;  
	@MockBean
	DBConnectionPool dBConnectionPool;
	
	private MockMvc mockMvc;

	@BeforeEach
	void setUpTest() {
		mockMvc = MockMvcBuilders.standaloneSetup(new AddStudentToCourseController()).build();
		
	}
	
	@Test
	void mustReturnExpectedView_WhenGETCalled() throws Exception {
		//given
		String studentUuid = "9723a706-edd1-4ea9-8629-70a91504ab2a";
		String studentFirstName = "John";
		String studentLastName = "Lennon";
		Student student = new Student(studentUuid, null, studentFirstName, studentLastName);
		List<Student> students = new ArrayList<Student>();
		students.add(student);
		
		String courseUuid = "7894f0de-5820-49bc-8562-b1240f0587b1";
		String courseName = "Music Theory";
		String courseDescription = "For Cool Guys";
		Course course = new Course(courseUuid, courseName, courseDescription);
		List<Course> courses = new ArrayList<Course>();
		courses.add(course);
		
		String URIPath = "/add_student_to_course";
		String expectedView = "add_student_to_course";

		when(schoolDAO.getAllStudents()).thenReturn(students);
		when(schoolDAO.getAllCourses()).thenReturn(courses);
		
		//when
		
		//then
		mockMvc.perform(get(URIPath)).andExpect(view().name(expectedView));
		
		
	}

}

package ControllersTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.lms.sqljdbcschool.controllers.FindStudentsByCourseNameController;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes = {TestConfig.class})
@WebAppConfiguration
class FindStudentsByCourseNameControllerTest {
	private MockMvc mockMvc;
	
	@Autowired
	SchoolDAO schoolDAO;
	
	@Autowired
	@InjectMocks
	FindStudentsByCourseNameController findStudentsByCourseNameController;
	
	@BeforeEach
	void setUpTest() {
		mockMvc = MockMvcBuilders.standaloneSetup(findStudentsByCourseNameController).build();
	}
	
	@Test
	void mustReturnExpectedView_WhenGETCalled_thenMustReturnExpectedView_WhenPOSTCalled() throws Exception {
		// GET mapping without params
		// given
		String courseUuid = "7894f0de-5820-49bc-8562-b1240f0587b1";
		String courseName = "Music Theory";
		String courseDescription = "For Cool Guys";
		Course course = new Course(courseUuid, courseName, courseDescription);
		List<Course> courses = new ArrayList<Course>();
		courses.add(course);

		String attributeCoursesName = "courses";
		List<Course> expectedCourses = courses;

		String attributeCourseRowNoName = "courserowno";
		Integer expectedCourseRowNo = new Integer(0);
		
		String GETURIPath = "/find_students_by_course_name";
		String expectedGETView = "find_students_by_course_name_tl";

		when(schoolDAO.getAllCourses()).thenReturn(courses);

		// when
		ResultActions actualGETResult = mockMvc.perform(get(GETURIPath));

		// then
		actualGETResult
				.andExpect(view().name(expectedGETView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(attributeCoursesName, expectedCourses))
				.andExpect(model().attribute(attributeCourseRowNoName, expectedCourseRowNo));
		
		InOrder daoGETOrder = Mockito.inOrder(schoolDAO);
		daoGETOrder.verify(schoolDAO).getAllCourses();

		// POST mapping with params: StudentRowNo, CourseRowNo
		// given
		String studentUuid = "9723a706-edd1-4ea9-8629-70a91504ab2a";
		String studentFirstName = "John";
		String studentLastName = "Lennon";
		Student student = new Student(studentUuid, null, studentFirstName, studentLastName);
		
		List<Student> students = new ArrayList<Student>();
		students.add(student);

		String attributeStudentsName = "students";
		List<Student> expectedStudents = students;
		
		String paramCourseRowNoName = "courserowno";
		Integer paramCourseRowNo = new Integer(1);

		when(schoolDAO.findStudentsByCourseID(courses.get(paramCourseRowNo - 1).getUuid())).thenReturn(students);
				
		String expectedMsgName = "msg";
		StringBuilder expectedMsg = new StringBuilder(); 
		expectedMsg.append("Students enlisted to course ")
				.append(courses.get(paramCourseRowNo - 1).getCourseName())
				.append(" !!!");

		String POSTURIPath = "/find_students_by_course_name";
		String expectedPOSTView = "finded_students_by_course_name_tl";
		
		// when
		ResultActions actualPOSTResult = mockMvc.perform(post(POSTURIPath)
				.flashAttr(paramCourseRowNoName, paramCourseRowNo));

		// then
		actualPOSTResult
				.andExpect(view().name(expectedPOSTView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(attributeStudentsName, expectedStudents))
				.andExpect(model().attribute(expectedMsgName, expectedMsg.toString()));

		InOrder daoPOSTOrder = Mockito.inOrder(schoolDAO);
		daoPOSTOrder.verify(schoolDAO).findStudentsByCourseID(courses.get(paramCourseRowNo - 1).getUuid());
	}	
	
}

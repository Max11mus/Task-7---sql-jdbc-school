package ControllersTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

import ua.com.foxminded.lms.sqljdbcschool.controllers.AddStudentController;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes = {TestConfig.class})
@WebAppConfiguration
class AddStudentControllerTest {
	private MockMvc mockMvc;
	
	@Autowired
	SchoolDAO schoolDAO;
	
	@Autowired
	@InjectMocks
	AddStudentController addStudentController;
	
	@BeforeEach
	void setUpTest() {
		mockMvc = MockMvcBuilders.standaloneSetup(addStudentController).build();
	}
	
	@Test
	void showAddStudentForm_mustReturnExpectedView_WhenGetRequest() throws Exception {
		// GET mapping without params
		// given
		String attributeStudentName = "student";

		String uriPath = "/add_student";
		String expectedView = "add_student_tl";

		// when
		ResultActions actualResult = mockMvc.perform(get(uriPath));

		// then
		actualResult.andExpect(view().name(expectedView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attributeExists(attributeStudentName));
	}

	@Test
	void saveStudent_mustReturnExpectedView_WhenPostRequest() throws Exception {
		// POST mapping with params: Student
		// given
		String studentUuid = "9723a706-edd1-4ea9-8629-70a91504ab2a";
		String studentFirstName = "John";
		String studentLastName = "Lennon";
		Student student = new Student(studentUuid, null, studentFirstName, studentLastName);

		String paramStudentName = "student";

		String uriPath = "/add_student";
		String expectedView = "student_saved_tl";

		// when
		ResultActions actualResult = mockMvc.perform(post(uriPath)
				.flashAttr(paramStudentName, student));

		// then
		actualResult
				.andExpect(view().name(expectedView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors());

		InOrder daoOrder = Mockito.inOrder(schoolDAO);
		daoOrder.verify(schoolDAO).insertStudent(student);
	}
	
}

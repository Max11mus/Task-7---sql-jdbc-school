package ControllersTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import ua.com.foxminded.lms.sqljdbcschool.controllers.ShowAllStudentsController;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes = {TestConfig.class})
@WebAppConfiguration
class ShowAllStudentsControllerTest {
	private MockMvc mockMvc;
	
	@Autowired
	SchoolDAO schoolDAO;
	
	@Autowired
	@InjectMocks
	ShowAllStudentsController showAllStudentsController;
	
	@BeforeEach
	void setUpTest() {
		mockMvc = MockMvcBuilders.standaloneSetup(showAllStudentsController).build();
	}
	
	@Test
	void mustReturnExpectedView_WhenGETCalled() throws Exception {
		// GET mapping without params
		// given
		String studentUuid = "9723a706-edd1-4ea9-8629-70a91504ab2a";
		String studentFirstName = "John";
		String studentLastName = "Lennon";
		Student student = new Student(studentUuid, null, studentFirstName, studentLastName);
		List<Student> students = new ArrayList<Student>();
		students.add(student);

		String attributeStudentsName = "students";
		List<Student> expectedStudents = students;

		String GETURIPath = "/get_all_students";
		String expectedGETView = "get_all_students_tl";

		when(schoolDAO.getAllStudents()).thenReturn(students);

		// when
		ResultActions actualGETResult = mockMvc.perform(get(GETURIPath));

		// then
		actualGETResult
				.andExpect(view().name(expectedGETView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(attributeStudentsName, expectedStudents));
		
		InOrder daoGETOrder = Mockito.inOrder(schoolDAO);
		daoGETOrder.verify(schoolDAO).getAllStudents();
	}	
	
}

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

import ua.com.foxminded.lms.sqljdbcschool.controllers.DeleteStudentController;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes = {TestConfig.class})
@WebAppConfiguration
class DeleteStudentControllerTest {
	private MockMvc mockMvc;
	
	@Autowired
	SchoolDAO schoolDAO;
	
	@Autowired
	@InjectMocks
	DeleteStudentController deleteStudentController;
	
	@BeforeEach
	void setUpTest() {
		mockMvc = MockMvcBuilders.standaloneSetup(deleteStudentController).build();
	}
	
	@Test
	void mustReturnExpectedView_WhenGETCalled_thenMustReturnExpectedView_WhenPOSTCalled() throws Exception {
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

		String attributeStudentRowNoName = "studentrowno";
		Integer expectedStudentRowNo = new Integer(0);
		
		String GETURIPath = "/delete_student";
		String expectedGETView = "delete_student_tl";

		when(schoolDAO.getAllStudents()).thenReturn(students);

		// when
		ResultActions actualGETResult = mockMvc.perform(get(GETURIPath));

		// then
		actualGETResult
				.andExpect(view().name(expectedGETView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(attributeStudentRowNoName, expectedStudentRowNo));
		
		InOrder daoGETOrder = Mockito.inOrder(schoolDAO);
		daoGETOrder.verify(schoolDAO).getAllStudents();

		// POST mapping with params: StudentRowNo
		// given
		String paramStudentRowNoName = "studentrowno";
		Integer paramStudentRowNo = new Integer(1);

		String expectedMsgName = "msg";
		StringBuilder expectedMsg = new StringBuilder(); 
		expectedMsg.append("Student Deleted: ")
				.append(student.toString())
				.append(" !!!");

		String POSTURIPath = "/delete_student";
		String expectedPOSTView = "student_deleted_tl";
		
		List<Student> expectedPOSTStudents = new ArrayList<>(expectedStudents);
		expectedPOSTStudents.remove(student);
		
		// when
		ResultActions actualPOSTResult = mockMvc.perform(post(POSTURIPath)
				.flashAttr(paramStudentRowNoName, paramStudentRowNo));

		// then
		actualPOSTResult
				.andExpect(view().name(expectedPOSTView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(paramStudentRowNoName, paramStudentRowNo))
				.andExpect(model().attribute(attributeStudentsName, expectedPOSTStudents))
				.andExpect(model().attribute(expectedMsgName, expectedMsg.toString()));

		InOrder daoPOSTOrder = Mockito.inOrder(schoolDAO);
		daoPOSTOrder.verify(schoolDAO).deleteStudent(student.getUuid());
	}	
	
}

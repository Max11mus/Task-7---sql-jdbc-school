package ControllersTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashMap;

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

import ua.com.foxminded.lms.sqljdbcschool.controllers.FindGroupsStudentCountLessOrEqualsController;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes = {TestConfig.class})
@WebAppConfiguration
class FindGroupsStudentCountLessOrEqualsControllerTest {
	private MockMvc mockMvc;
	
	@Autowired
	SchoolDAO schoolDAO;
	
	@Autowired
	@InjectMocks
	FindGroupsStudentCountLessOrEqualsController findGroupsStudentCountLessOrEqualsController;
	
	@BeforeEach
	void setUpTest() {
		mockMvc = MockMvcBuilders.standaloneSetup(findGroupsStudentCountLessOrEqualsController).build();
	}
	
	@Test
	void mustReturnExpectedView_WhenGETCalled_thenMustReturnExpectedView_WhenPOSTCalled() throws Exception {
		// GET mapping without params
		// given
		String attributeStudentCountName = "studentcount";
		Integer attributeStudentCount = new Integer(0);
		
		String GETURIPath = "/find_groups_student_countlessorequals";
		String expectedGETView = "find_groups_student_countlessorequals_tl";

		// when
		ResultActions actualGETResult = mockMvc.perform(get(GETURIPath));

		// then
		actualGETResult
				.andExpect(view().name(expectedGETView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(attributeStudentCountName, attributeStudentCount));
		
		// POST mapping with params: studentCount
		// given
		String paramStudentCountName = "studentcount";
		Integer paramStudentCount = new Integer(10);

		String groupUuid = "9723a706-edd1-4ea9-8629-70a91504ab2a";
		String groupName = "IO-45";
		Group group = new Group(groupUuid, groupName);
		
		String studentCountGroupsName = "studentcountgroups"; 
		HashMap<Group, Integer> studentCountGroups = new HashMap<>();
		studentCountGroups.put(group, paramStudentCount);
		
		when(schoolDAO.findGroupsStudentCountLessOrEquals(paramStudentCount)).thenReturn(studentCountGroups);
		
		String expectedMsgName = "msg";
		StringBuilder expectedMsg = new StringBuilder(); 
		expectedMsg
				.append("Groups with student count <= ")
				.append(paramStudentCount);

		String POSTURIPath = "/find_groups_student_countlessorequals";
		String expectedPOSTView = "finded_groups_student_countlessorequals_tl";
		
		// when
		ResultActions actualPOSTResult = mockMvc.perform(post(POSTURIPath)
				.flashAttr(paramStudentCountName, paramStudentCount));

		// then
		actualPOSTResult
				.andExpect(view().name(expectedPOSTView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(studentCountGroupsName, studentCountGroups))
				.andExpect(model().attribute(expectedMsgName, expectedMsg.toString()));

		InOrder daoPOSTOrder = Mockito.inOrder(schoolDAO);
		daoPOSTOrder.verify(schoolDAO).findGroupsStudentCountLessOrEquals(paramStudentCount);
	}	
	
}

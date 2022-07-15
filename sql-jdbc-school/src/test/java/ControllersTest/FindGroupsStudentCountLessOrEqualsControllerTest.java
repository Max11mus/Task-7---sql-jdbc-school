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
import ua.com.foxminded.lms.sqljdbcschool.jdbc.SchoolJdbcDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes = {TestConfig.class})
@WebAppConfiguration
class FindGroupsStudentCountLessOrEqualsControllerTest {
	private MockMvc mockMvc;
	
	@Autowired
	SchoolJdbcDAO schoolDAO;
	
	@Autowired
	@InjectMocks
	FindGroupsStudentCountLessOrEqualsController findGroupsStudentCountLessOrEqualsController;
	
	@BeforeEach
	void setUpTest() {
		mockMvc = MockMvcBuilders.standaloneSetup(findGroupsStudentCountLessOrEqualsController).build();
	}

	@Test
	void enterStudentsCountForm_mustReturnExpectedView_WhenGetRequest() throws Exception {
		// Get mapping without params
		// given
		String attributeStudentCountName = "studentcount";
		Integer attributeStudentCount = Integer.valueOf(0);
		
		String uriPath = "/find_groups_student_countlessorequals";
		String expectedView = "find_groups_student_countlessorequals_tl";

		// when
		ResultActions actualResult = mockMvc.perform(get(uriPath));

		// then
		actualResult
				.andExpect(view().name(expectedView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(attributeStudentCountName, attributeStudentCount));
	}

	@Test
	void showGroupsStudentsCountForm_MustReturnExpectedView_WhenPostRequest() throws Exception {
		// Post mapping with params: studentCount
		// given
		String paramStudentCountName = "studentcount";
		Integer paramStudentCount = Integer.valueOf(10);

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

		String uriPath = "/find_groups_student_countlessorequals";
		String expectedView = "finded_groups_student_countlessorequals_tl";

		// when
		ResultActions actualResult = mockMvc.perform(post(uriPath)
				.flashAttr(paramStudentCountName, paramStudentCount));

		// then
		actualResult
				.andExpect(view().name(expectedView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(studentCountGroupsName, studentCountGroups))
				.andExpect(model().attribute(expectedMsgName, expectedMsg.toString()));

		InOrder daoOrder = Mockito.inOrder(schoolDAO);
		daoOrder.verify(schoolDAO).findGroupsStudentCountLessOrEquals(paramStudentCount);
	}
}

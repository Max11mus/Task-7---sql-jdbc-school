package ua.com.foxminded.lms.sqljdbcschool.controllers;

import static org.junit.Assert.*;
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

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.hibernate.SchoolHibernateDAO;
import ua.com.foxminded.lms.sqljdbcschool.jdbc.SchoolJdbcDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

import javax.servlet.http.HttpSession;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes = {TestConfig.class})
@WebAppConfiguration
class DeleteStudentControllerTest {
	private MockMvc mockMvc;

	@Autowired
	SchoolDAO dao;
	
	@Autowired
	@InjectMocks
	DeleteStudentController deleteStudentController;
	
	@BeforeEach
	void setUpTest() {
		mockMvc = MockMvcBuilders.standaloneSetup(deleteStudentController).build();
	}
	
	@Test
	void showDeleteStudentForm_MustReturnExpectedView_WhenGetRequest() throws Exception {
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
		Integer expectedStudentRowNo = Integer.valueOf(0);
		
		String uriPath = "/delete_student";
		String expectedView = "delete_student_tl";

		when(dao.getAllStudents()).thenReturn(students);

		// when
		ResultActions actualResult = mockMvc.perform(get(uriPath));

		// then
		actualResult
				.andExpect(view().name(expectedView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(attributeStudentsName, expectedStudents));
		
		InOrder daoOrder = Mockito.inOrder(dao);
		daoOrder.verify(dao).getAllStudents();
	}

	@Test
	void deleteStudent_mustReturnExpectedView_WhenPostRequest() throws Exception {
		// POST mapping with params: StudentRowNo
		// given
		String studentUuid = "9723a706-edd1-4ea9-8629-70a91504ab2a";
		String studentFirstName = "John";
		String studentLastName = "Lennon";
		Student student = new Student(studentUuid, null, studentFirstName, studentLastName);
		List<Student> students = new ArrayList<Student>();
		students.add(student);

		String attributeStudentsName = "students";
		List<Student> expectedStudents = students;

		String paramStudentRowNoName = "studentrowno";
		Integer paramStudentRowNo = Integer.valueOf(1);

		String expectedMsgName = "msg";
		StringBuilder expectedMsg = new StringBuilder();
		expectedMsg.append("Student Deleted: ")
				.append(student.toString())
				.append(" !!!");

		String uriPath = "/delete_student";
		String expectedView = "redirect:/student_deleted";

		when(dao.getAllStudents()).thenReturn(students);

		List<Student> expectedSessionStudents = new ArrayList<>(expectedStudents);
		expectedSessionStudents.remove(student);

		// when
		ResultActions actualResult = mockMvc.perform(post(uriPath)
				.flashAttr(paramStudentRowNoName, paramStudentRowNo));

		// then
		HttpSession session = actualResult
				.andExpect(view().name(expectedView))
				.andExpect(status().is3xxRedirection())
				.andExpect(model().hasNoErrors())
				.andReturn()
				.getRequest()
				.getSession();

		InOrder daoOrder = Mockito.inOrder(dao);
		daoOrder.verify(dao).getAllStudents();
		daoOrder.verify(dao).deleteStudent(student.getUuid());

		assertEquals(expectedSessionStudents, session.getAttribute(attributeStudentsName));
		assertEquals(expectedMsg.toString(), session.getAttribute(expectedMsgName));
	}

	@Test
	void showDeletedStudent_mustReturnExpectedView_WhenGetRequest() throws Exception {
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

		String paramStudentRowNoName = "studentrowno";
		Integer paramStudentRowNo = Integer.valueOf(1);

		String expectedMsgName = "msg";
		StringBuilder expectedMsg = new StringBuilder();
		expectedMsg.append("Student Deleted: ")
				.append(student.toString())
				.append(" !!!");

		String uriPath = "/student_deleted";
		String expectedView = "student_deleted_tl";

		List<Student> expectedSessionStudents = new ArrayList<>(expectedStudents);
		expectedSessionStudents.remove(student);

		// when
		ResultActions actualResult = mockMvc.perform(get(uriPath)
				.sessionAttr(attributeStudentsName, expectedSessionStudents)
				.sessionAttr(expectedMsgName, expectedMsg.toString()));

		// then
		HttpSession session = actualResult
				.andExpect(view().name(expectedView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(attributeStudentsName, expectedSessionStudents))
				.andExpect(model().attribute(expectedMsgName, expectedMsg.toString()))
				.andReturn()
				.getRequest()
				.getSession();

		assertEquals(session.getAttributeNames().hasMoreElements(), false);
	}
}

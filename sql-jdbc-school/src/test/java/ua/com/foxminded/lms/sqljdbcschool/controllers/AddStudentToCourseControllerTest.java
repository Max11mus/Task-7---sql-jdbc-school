package ua.com.foxminded.lms.sqljdbcschool.controllers;

import static org.junit.Assert.assertEquals;
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
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

import javax.servlet.http.HttpSession;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes = {TestConfig.class})
@WebAppConfiguration
class AddStudentToCourseControllerTest {
	private MockMvc mockMvc;

	@Autowired
	SchoolDAO dao;
	
	@Autowired
	@InjectMocks
	AddStudentToCourseController addStudentToCourseController;
	
	@BeforeEach
	void setUpTest() {
		mockMvc = MockMvcBuilders.standaloneSetup(addStudentToCourseController).build();
	}
	
	@Test
	void showAddStudentForm_mustReturnExpectedView_WhenGetRequest() throws Exception {
		// GET mapping without params
		// given
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

		String attributeStudentsName = "students";
		List<Student> expectedStudents = students;

		String attributeCoursesName = "courses";
		List<Course> expectedCourses = courses;

		String attributeStudentRowNoName = "studentrowno";
		Integer expectedStudentRowNo = Integer.valueOf(0);

		String attributeCourseRowNoName = "courserowno";
		Integer expectedCourseRowNo = Integer.valueOf(0);
		
		String uriPath = "/add_student_to_course";
		String expectedView = "add_student_to_course_tl";

		when(dao.getAllStudents()).thenReturn(students);
		when(dao.getAllCourses()).thenReturn(courses);

		// when
		ResultActions actualResult = mockMvc.perform(get(uriPath));

		// then
		actualResult
				.andExpect(view().name(expectedView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(attributeStudentsName, expectedStudents))
				.andExpect(model().attribute(attributeCoursesName, expectedCourses));
		
		InOrder daoOrder = Mockito.inOrder(dao);
		daoOrder.verify(dao).getAllStudents();
		daoOrder.verify(dao).getAllCourses();
	}

	@Test
	void addStudentToCourse_MustReturnExpectedView_WhenPostRequest() throws Exception {
		// POST mapping with params: StudentRowNo, CourseRowNo
		// given
		String studentUuid = "9723a706-edd1-4ea9-8629-70a91504ab2a";
		String studentFirstName = "John";
		String studentLastName = "Lennon";
		Student student = new Student(studentUuid, null, studentFirstName, studentLastName);
		List<Student> students = new ArrayList<Student>();
		students.add(student);
		String expectedAttrStudentsName = "students";

		String courseUuid = "7894f0de-5820-49bc-8562-b1240f0587b1";
		String courseName = "Music Theory";
		String courseDescription = "For Cool Guys";
		Course course = new Course(courseUuid, courseName, courseDescription);
		List<Course> courses = new ArrayList<Course>();
		courses.add(course);
		String expectedAttrCoursesName = "courses";

		String paramStudentRowNoName = "studentrowno";
		Integer paramStudentRowNo = Integer.valueOf(1);

		String paramCourseRowNoName = "courserowno";
		Integer paramCourseRowNo = Integer.valueOf(1);

		String expectedMsgName = "msg";
		StringBuilder expectedMsg = new StringBuilder();
		expectedMsg.append("Student added: ")
				.append(student.toString())
				.append(" to course ")
				.append(course.toString())
				.append(" !!!");

		String uriPath = "/add_student_to_course";
		String expectedView = "redirect:/student_added_to_course";

		when(dao.getAllStudents()).thenReturn(students);
		when(dao.getAllCourses()).thenReturn(courses);

		// when
		ResultActions actualResult = mockMvc.perform(post(uriPath)
				.flashAttr(paramStudentRowNoName, paramStudentRowNo)
				.flashAttr(paramCourseRowNoName, paramCourseRowNo));

		// then
		HttpSession session = actualResult
				.andExpect(view().name(expectedView))
				.andExpect(status().is3xxRedirection())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(paramStudentRowNoName, paramStudentRowNo))
				.andExpect(model().attribute(paramCourseRowNoName, paramCourseRowNo))
				.andReturn()
				.getRequest()
				.getSession();

		InOrder daoOrder = Mockito.inOrder(dao);
		daoOrder.verify(dao).getAllStudents();
		daoOrder.verify(dao).getAllCourses();
		daoOrder.verify(dao).addStudentToCourse(student.getUuid(), course.getUuid());

		assertEquals(session.getAttribute(expectedAttrStudentsName) , students);
		assertEquals(session.getAttribute(expectedAttrCoursesName) , courses);
		assertEquals(session.getAttribute(expectedMsgName) , expectedMsg.toString());
	}

	void showAddedToCourseStudent_MustReturnExpectedView_WhenGetRequest() throws Exception {
		// Get mapping without params:
		// given
		String studentUuid = "9723a706-edd1-4ea9-8629-70a91504ab2a";
		String studentFirstName = "John";
		String studentLastName = "Lennon";
		Student student = new Student(studentUuid, null, studentFirstName, studentLastName);
		List<Student> students = new ArrayList<Student>();
		students.add(student);
		String expectedAttrStudentsName = "students";

		String courseUuid = "7894f0de-5820-49bc-8562-b1240f0587b1";
		String courseName = "Music Theory";
		String courseDescription = "For Cool Guys";
		Course course = new Course(courseUuid, courseName, courseDescription);
		List<Course> courses = new ArrayList<Course>();
		courses.add(course);
		String expectedAttrCoursesName = "courses";

		String paramStudentRowNoName = "studentrowno";
		Integer paramStudentRowNo = Integer.valueOf(1);

		String paramCourseRowNoName = "courserowno";
		Integer paramCourseRowNo = Integer.valueOf(1);

		String expectedMsgName = "msg";
		StringBuilder expectedMsg = new StringBuilder();
		expectedMsg.append("Student added: ")
				.append(student.toString())
				.append(" to course ")
				.append(course.toString())
				.append(" !!!");

		String uriPath = "/student_added_to_course";
		String expectedView = "student_added_to_course_tl";

		// when
		ResultActions actualResult = mockMvc.perform(get(uriPath)
				.sessionAttr(expectedAttrStudentsName, students)
				.sessionAttr(expectedAttrStudentsName, students)
				.sessionAttr(expectedMsgName, expectedMsg.toString()));

		// then
		HttpSession session = actualResult
				.andExpect(view().name(expectedView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(expectedAttrStudentsName, students))
				.andExpect(model().attribute(expectedAttrCoursesName, courses))
				.andExpect(model().attribute(expectedMsgName, expectedMsg.toString()))
				.andReturn()
				.getRequest()
				.getSession();

		assertEquals(session.getAttributeNames().hasMoreElements(), false);
	}

}

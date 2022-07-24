package ua.com.foxminded.lms.sqljdbcschool.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
class DropoutStudentFromCourseControllerTest {
	private MockMvc mockMvc;

	@Autowired
	SchoolDAO dao;
	
	@Autowired
	@InjectMocks
	DropoutStudentFromCourseController dropoutStudentFromCourseController;
	
	@BeforeEach
	void setUpTest() {
		mockMvc = MockMvcBuilders.standaloneSetup(dropoutStudentFromCourseController).build();
	}
	
	@Test
	void chooseStudent_mustReturnExpectedView_WhenGetRequest() throws Exception {
		// Get mapping without params
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

		String attributeStudentRowNoName = "studentrowno";
		Integer expectedStudentRowNo = Integer.valueOf(0);

		String uriPath = "/dropout_student_from_course/choose_student";
		String expectedView = "dropout_student_from_course_choose_student_tl";

		when(dao.getAllStudents()).thenReturn(students);

		// when
		ResultActions actualResult = mockMvc.perform(get(uriPath));

		// then
		HttpSession session = actualResult
				.andExpect(view().name(expectedView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(attributeStudentsName, expectedStudents))
				.andReturn()
				.getRequest()
				.getSession();

		assertEquals((List<Student>) session.getAttribute(attributeStudentsName) , expectedStudents);

		InOrder daoOrder = Mockito.inOrder(dao);
		daoOrder.verify(dao).getAllStudents();

	}

	@Test
	void chooseStudentCourse_mustReturnExpectedView_WhenGetRequest() throws Exception {
		// Get mapping with params: studentrowno
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
		
		String paramStudentRowNoName = "studentrowno";
		Integer paramStudentRowNo = Integer.valueOf(1);

		String attributeStudentName = "student";
		Student expectedStudent = student;

		String attributeCoursesName = "courses";
		List<Course> expectedCourses = new ArrayList<Course>(courses);

		String uriPathWithParam = "/dropout_student_from_course/choose_course";
		String expectedView = "dropout_student_from_course_choose_course_tl";

		when(dao.findStudentCourses(student.getUuid())).thenReturn(courses);

		// when
		ResultActions actualResult = mockMvc.perform(get(uriPathWithParam)
				.param(paramStudentRowNoName, paramStudentRowNo.toString())
				.sessionAttr("students", students));
		
		// then
		HttpSession session = actualResult
				.andExpect(view().name(expectedView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(attributeCoursesName, expectedCourses))
				.andReturn()
				.getRequest()
				.getSession();

		assertEquals((Student) session.getAttribute(attributeStudentName) , expectedStudent);
		assertEquals((List<Course>) session.getAttribute(attributeCoursesName) , expectedCourses);

		InOrder daoOrder = Mockito.inOrder(dao);
		daoOrder.verify(dao).findStudentCourses(student.getUuid());
	}

	@Test
	void dropoutStudentFromCourse_mustReturnExpectedView_WhenPostRequest() throws Exception {
		// Post mapping with params: CourseRowNo
		// given
		String studentUuid = "9723a706-edd1-4ea9-8629-70a91504ab2a";
		String studentFirstName = "John";
		String studentLastName = "Lennon";
		Student student = new Student(studentUuid, null, studentFirstName, studentLastName);

		String courseUuid = "7894f0de-5820-49bc-8562-b1240f0587b1";
		String courseName = "Music Theory";
		String courseDescription = "For Cool Guys";
		Course course = new Course(courseUuid, courseName, courseDescription);
		List<Course> courses = new ArrayList<Course>();
		courses.add(course);

		String paramCourseRowNoName = "courserowno";
		Integer paramCourseRowNo = Integer.valueOf(1);

		String attributeCoursesNamePost = "courses";
		List<Course> attributeCoursesPost = new ArrayList<>(courses);
		attributeCoursesPost.remove(course);

		String attributeStudentName = "student";
		Student expectedStudent = student;

		String expectedMsgName = "msg";
		StringBuilder expectedMsg = new StringBuilder();
		expectedMsg.append("Student RowNo ")
				.append(student.toString())
				.append(" dropouted from ")
				.append(course.toString());

		String uriPath = "/dropout_student_from_course";
		String expectedView = "student_dropouted_from_course_choose_course_tl";

		// when
		ResultActions actualResult = mockMvc.perform(post(uriPath)
				.flashAttr(paramCourseRowNoName, paramCourseRowNo)
				.sessionAttr(attributeStudentName, expectedStudent)
				.sessionAttr(attributeCoursesNamePost, courses));

		// then
		HttpSession session = actualResult
				.andExpect(view().name(expectedView))
				.andExpect(status().isOk())
				.andExpect(model().hasNoErrors())
				.andExpect(model().attribute(paramCourseRowNoName, paramCourseRowNo))
				.andExpect(model().attribute(attributeCoursesNamePost, attributeCoursesPost))
				.andExpect(model().attribute(expectedMsgName, expectedMsg.toString()))
				.andReturn()
				.getRequest()
				.getSession();

		assertEquals(session.getAttributeNames().hasMoreElements(), false);

		InOrder daoOrder = Mockito.inOrder(dao);
		daoOrder.verify(dao).dropoutStudentFromCourse(student.getUuid(), course.getUuid());
	}
}

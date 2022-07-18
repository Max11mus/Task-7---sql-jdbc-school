package ua.com.foxminded.lms.sqljdbcschool.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AddStudentToCourseController {
	@Autowired
	ChooseDaoEngine chooseDaoEngine;
	
	@GetMapping("/add_student_to_course")
	public String showAddStudentForm(Model model) {
		SchoolDAO dao = chooseDaoEngine.getCurrentDaoEngine();

		List<Student> students = dao.getAllStudents();
		List<Course> courses = dao.getAllCourses();

		model.addAttribute("students", students);
		model.addAttribute("courses", courses);

		return "add_student_to_course_tl";
	}

	@PostMapping("/add_student_to_course")
	public String addStudentToCourse(HttpServletRequest request,
							  @ModelAttribute("studentrowno") Integer studentRowNo,
							  @ModelAttribute("courserowno") Integer courseRowNo) {
		SchoolDAO dao = chooseDaoEngine.getCurrentDaoEngine();

		HttpSession session = request.getSession();
		StringBuilder msg = new StringBuilder();

		List<Student> students = dao.getAllStudents();
		List<Course> courses = dao.getAllCourses();

		if (students.isEmpty()) {
			msg.append("No students is present !!! ");
		}

		if (courses.isEmpty()) {
			msg.append("No courses is present !!! ");
		}

		if (studentRowNo < 1 || studentRowNo > students.size()) {
			msg.append("Student RowNo ")
					.append(studentRowNo)
					.append(" is out of range (1 - ")
					.append(students.size())
					.append(") ");
		}

		if (courseRowNo < 1 || courseRowNo > courses.size()) {
			msg.append("Courses RowNo ")
					.append(courseRowNo)
					.append(" is out of range (1 - ")
					.append(courses.size())
					.append(") ");
		}

		if (msg.length() == 0) {
			msg.append("Student added: ")
					.append(students.get(studentRowNo - 1).toString())
					.append(" to course ")
					.append(courses.get(courseRowNo - 1).toString())
					.append(" !!!");
			dao.addStudentToCourse(students.get(studentRowNo - 1).getUuid(),
					courses.get(courseRowNo - 1).getUuid());

		}

		session.setAttribute("students", students);
		session.setAttribute("courses", courses);
		session.setAttribute("msg", msg.toString());

		return "redirect:/student_added_to_course";
	}

	@GetMapping("/student_added_to_course")
	public String showAddedToCourseStudent(HttpServletRequest request,
									 Model model) {
		HttpSession session = request.getSession();
		StringBuilder msg = new StringBuilder();

		List<Student> students = (List<Student>) session.getAttribute("students");
		List<Course> courses = (List<Course>) session.getAttribute("courses");

		if (students == null || courses == null) {
			session.invalidate();
			return "redirect:/add_student_to_course";
		}

		model.addAttribute("students", students);
		model.addAttribute("courses", courses);
		model.addAttribute("msg", msg.toString());

		session.invalidate();

		return "student_added_to_course_tl";
	}

}

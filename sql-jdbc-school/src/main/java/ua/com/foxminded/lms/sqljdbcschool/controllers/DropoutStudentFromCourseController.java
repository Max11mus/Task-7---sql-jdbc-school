package ua.com.foxminded.lms.sqljdbcschool.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class DropoutStudentFromCourseController {
	@Autowired
	@Lazy
	SchoolDAO dao;

	@GetMapping("/dropout_student_from_course/choose_student")
	public String chooseStudent(HttpServletRequest request, Model model) {
		List<Student> students = dao.getAllStudents();

		HttpSession session = request.getSession();
		session.setAttribute("students", students);
		model.addAttribute("students", students);
		return "dropout_student_from_course_choose_student_tl";
	}
	
	@GetMapping("/dropout_student_from_course/choose_course")
	public String chooseStudentCourse(HttpServletRequest request,
									  @RequestParam("studentrowno") Integer studentRowNo,
									  Model model) {
		HttpSession session = request.getSession();
		List<Student> students = (List<Student>) session.getAttribute("students");
		if (students == null) {
			session.invalidate();
			return "forward:/dropout_student_from_course/choose_student";
		}

		Student student = students.get(studentRowNo - 1);
		List<Course> courses = dao.findStudentCourses(student.getUuid());
		session.setAttribute("student", student);
		session.setAttribute("courses", courses);

		model.addAttribute("courses", courses);

		return "dropout_student_from_course_choose_course_tl";
	}

	@PostMapping("/dropout_student_from_course")
	public String dropoutStudentFromCourse(HttpServletRequest request,
										   @ModelAttribute("courserowno") Integer courseRowNo,
										   Model model) {
		HttpSession session = request.getSession();
		Student student = (Student) session.getAttribute("student");
		if (student == null) {
			session.invalidate();
			return "forward:/dropout_student_from_course/choose_student";
		}
		List<Course> courses = (List<Course>) session.getAttribute("courses");
		Course course = courses.get(courseRowNo - 1);

		StringBuilder msg = new StringBuilder();

			if (courseRowNo < 1 || courseRowNo > courses.size()) {
				msg.append("Courses RowNo ")
						.append(courseRowNo)
						.append(" is out of range (1 - ")
						.append(courses.size())
						.append(") ");
			}

			if (msg.length() == 0) {
				msg.append("Student RowNo ")
						.append(student.toString())
						.append(" dropouted from ")
						.append(courses.get(courseRowNo - 1).toString());
				dao.dropoutStudentFromCourse(student.getUuid(), courses.get(courseRowNo - 1).getUuid());
				courses.remove(courseRowNo - 1);
				session.invalidate();
			}

		model.addAttribute("courses", courses);
		model.addAttribute("msg", msg.toString());

		return "student_dropouted_from_course_choose_course_tl";
	}
}

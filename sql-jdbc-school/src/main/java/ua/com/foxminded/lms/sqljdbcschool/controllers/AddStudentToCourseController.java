package ua.com.foxminded.lms.sqljdbcschool.controllers;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

@Controller
public class AddStudentToCourseController {
	@Autowired
	@Lazy
	SchoolDAO dao;
	
	boolean posted = false;

	@GetMapping("/add_student_to_course")
	public String showAddStudentForm(Model model) {
		posted = false;
		List<Student> students = dao.getAllStudents();
		List<Course> courses = dao.getAllCourses();

		model.addAttribute("students", students);
		model.addAttribute("courses", courses);

		return "add_student_to_course_tl";
	}

	@PostMapping("/add_student_to_course")
	public String saveStudent(@ModelAttribute("studentrowno") Integer studentRowNo,
							  @ModelAttribute("courserowno") Integer courseRowNo,
							  Model model) {
		StringBuilder msg = new StringBuilder();

		List<Student> students = dao.getAllStudents();
		List<Course> courses = dao.getAllCourses();

		if (!posted) {
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

			posted = true;
		}

		model.addAttribute("students", students);
		model.addAttribute("courses", courses);
		model.addAttribute("msg", msg.toString());

		return "student_added_to_course_tl";
	}

}

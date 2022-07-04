package ua.com.foxminded.lms.sqljdbcschool.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

@Controller

public class DropoutStudentFromCourseController {
	@Autowired
	@Lazy
	SchoolDAO dao;
	boolean posted = false;
	List<Student> students;
	Student student;
	List<Course> courses;

	@GetMapping("/dropout_student_from_course")
	public String chooseStudent(Model model) {
		posted = false;
		students = dao.getAllStudents();

		model.addAttribute("students", students);
		model.addAttribute("studentrowno", new Integer(0));

		return "dropout_student_from_course_choose_student_tl";
	}
	
	@GetMapping(value = "/dropout_student_from_course", params = "studentrowno")
	public String chooseStudentCourse(@RequestParam(name = "studentrowno") Integer studentRowNo, Model model) {
		posted = false;

		student = students.get(studentRowNo - 1);

		String msg = student.toString() + " enlisted courses";
		courses = dao.findStudentCourses(student.getUuid());

		model.addAttribute("msg", msg);
		model.addAttribute("courses", courses);
		model.addAttribute("courserowno", new Integer(0));

		return "dropout_student_from_course_choose_course_tl";
	}

	@PostMapping("/dropout_student_from_course")
	public String saveStudent(@ModelAttribute("courserowno") Integer courseRowNo, Model model) {
		StringBuilder msg = new StringBuilder();

		if (!posted) {
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
			}

			posted = true;
		}

		model.addAttribute("courses", courses);
		model.addAttribute("msg", msg.toString());

		return "student_dropouted_from_course_choose_course_tl";
	}

}

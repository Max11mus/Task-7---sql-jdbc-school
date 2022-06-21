package ua.com.foxminded.lms.sqljdbcschool.controllers;

import java.util.List;

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
@Lazy
public class AddStudentToCourseController {
	@Autowired
	SchoolDAO dao;
	boolean posted = false; 
	List<Student> students;
	List<Course> courses;
	
	
	@GetMapping("/add_student_to_course")
	public String showAddStudentForm(Model model) {
		posted = false;
		students = dao.getAllStudents();
		courses = dao.getAllCourses();
		
		model.addAttribute("students", students);
		model.addAttribute("courses", courses);
		model.addAttribute("studentrowno", new Integer(0));
		model.addAttribute("courserowno", new Integer(0));
		
		return "add_student_to_course";
	}

	@PostMapping("/add_student_to_course")
	public String saveStudent(@ModelAttribute("studentrowno") Integer studentRowNo,
			@ModelAttribute("courserowno") Integer courseRowNo, Model model) {
		String msg = "";

		if (!posted) {

			if (students.isEmpty()) {
				msg += "No students is present !!! ";
			}
			
			if (courses.isEmpty()) {
				msg += "No courses is present !!! ";
			}

			if (studentRowNo < 1 || studentRowNo > students.size()) {
				msg += "Student RowNo " + studentRowNo + " is out of range (1 - " + students.size() + ") ";
			}
			
			if (courseRowNo < 1 || courseRowNo > courses.size()) {
				msg += "Courses RowNo " + courseRowNo + " is out of range (1 - " + courses.size() + ") ";
			}

			if (msg.isEmpty()) {

			msg = "Student added: " + students.get(studentRowNo - 1).toString() + " to course "
						+ courses.get(courseRowNo - 1).toString() + " !!!";
			dao.addStudentToCourse(students.get(studentRowNo - 1).getUuid(), courses.get(courseRowNo - 1).getUuid());
			}
			
			posted = true;
		}

		model.addAttribute("students", students);
		model.addAttribute("courses", courses);
		model.addAttribute("msg", msg);

		return "student_added_to_course";
	}

}

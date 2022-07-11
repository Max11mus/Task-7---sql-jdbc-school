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
public class FindStudentsByCourseNameController {
	@Autowired
	@Lazy
	SchoolDAO dao;
	boolean posted = false; 

	@GetMapping("/find_students_by_course_name")
	public String showChooseCourseForm(Model model) {
		posted = false;
		List<Course> courses = dao.getAllCourses();
		
		model.addAttribute("courses", courses);
		model.addAttribute("courserowno", Integer.valueOf(0));
		
		return "find_students_by_course_name_tl";
	}

	@PostMapping("/find_students_by_course_name")
	public String findStudentsByCourse(@ModelAttribute("courserowno") Integer courseRowNo, Model model) {
		StringBuilder msg = new StringBuilder();
		List<Course> courses = dao.getAllCourses();
		List<Student> students = null;

		if (!posted) {
			if (courses.isEmpty()) {
				msg.append("No courses is present !!! ");
			}

			if (courseRowNo < 1 || courseRowNo > courses.size()) {
				msg.append("Courses RowNo ")
						.append(courseRowNo)
						.append(" is out of range (1 - ")
						.append(courses.size())
						.append(") ");
			}

			if (msg.length() == 0) {
				msg.append("Students enlisted to course ")
						.append(courses.get(courseRowNo - 1).getCourseName())
						.append(" !!!");
				students =  dao.findStudentsByCourseID(courses.get(courseRowNo - 1).getUuid());
			}
			
			posted = true;
		}

		model.addAttribute("students", students);
		model.addAttribute("msg", msg.toString());

		return "finded_students_by_course_name_tl";
	}
}

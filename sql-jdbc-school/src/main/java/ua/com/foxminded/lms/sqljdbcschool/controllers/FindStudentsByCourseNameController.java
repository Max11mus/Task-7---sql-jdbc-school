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
public class FindStudentsByCourseNameController {
	@Autowired
	SchoolDAO dao;
	boolean posted = false; 
	List<Student> students;
	List<Course> courses;
	
	
	@GetMapping("/find_students_by_course_name")
	public String showAddStudentForm(Model model) {
		posted = false;
		courses = dao.getAllCourses();
		
		model.addAttribute("courses", courses);
		model.addAttribute("courserowno", new Integer(0));
		
		return "find_students_by_course_name";
	}

	@PostMapping("/find_students_by_course_name")
	public String saveStudent(@ModelAttribute("courserowno") Integer courseRowNo, Model model) {
		String msg = "";

		if (!posted) {

			if (courses.isEmpty()) {
				msg += "No courses is present !!! ";
			}

			if (courseRowNo < 1 || courseRowNo > courses.size()) {
				msg += "Courses RowNo " + courseRowNo + " is out of range (1 - " + courses.size() + ") ";
			}

			if (msg.isEmpty()) {

			msg = "Students enlisted to course " + courses.get(courseRowNo - 1).getCourseName() + " !!!";
			students =  dao.findStudentsByCourseID(courses.get(courseRowNo - 1).getUuid());
			}
			
			posted = true;
		}

		model.addAttribute("students", students);
		model.addAttribute("msg", msg);

		return "finded_students_by_course_name";
	}

}

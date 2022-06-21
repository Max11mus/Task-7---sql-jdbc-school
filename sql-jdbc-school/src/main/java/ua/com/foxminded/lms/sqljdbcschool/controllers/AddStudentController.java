package ua.com.foxminded.lms.sqljdbcschool.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

@Controller
@Lazy
public class AddStudentController {
	@Autowired
	SchoolDAO dao;
	boolean posted = false; 
	
	@GetMapping("/add_student")
	public String showAddStudentForm(Model model) {
		posted = false;
		Student student = new Student();
		model.addAttribute("student", student);
		return "add_student";
	}

	@PostMapping("/add_student")
	public String  saveStudent(@ModelAttribute("student") Student student, Model model) {
		if (!posted) {
			dao.insertStudent(student);
			posted = true;
		}
		
		return "student_saved";
	}

}

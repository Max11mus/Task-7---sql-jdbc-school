package ua.com.foxminded.lms.sqljdbcschool.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.hibernate.SchoolHibernateDAO;

@Controller

public class ShowAllStudentsController {
	@Autowired
	SchoolDAO dao;
	
	@GetMapping("/get_all_students")
	public String getAllStudents(Model model) {
		model.addAttribute("students", dao.getAllStudents());
	    return "get_all_students_tl";
	  }

}

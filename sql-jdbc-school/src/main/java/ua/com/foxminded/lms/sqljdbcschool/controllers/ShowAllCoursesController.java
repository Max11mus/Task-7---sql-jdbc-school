package ua.com.foxminded.lms.sqljdbcschool.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.hibernate.SchoolHibernateDAO;

@Controller
@Lazy
public class ShowAllCoursesController {
	@Autowired
	SchoolDAO dao;
	
	@GetMapping("/get_all_courses")
	public String getAllCources(Model model) {
		model.addAttribute("courses", dao.getAllCourses());
	    return "get_all_courses_tl";
	  }

}

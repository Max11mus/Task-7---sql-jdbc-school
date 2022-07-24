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
import ua.com.foxminded.lms.sqljdbcschool.hibernate.SchoolHibernateDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AddStudentController {
	@Autowired
	SchoolDAO dao;

	@GetMapping("/add_student")
	public String showAddStudentForm(Model model) {
		Student student = new Student();
		model.addAttribute("student", student);
		return "add_student_tl";
	}

	@GetMapping("/student_saved")
	public String showSavedStudent(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		Student student = (Student) session.getAttribute("student");

		if (student == null) {
			session.invalidate();
			return "redirect:/add_student";
		}

		model.addAttribute("student", student);
		session.invalidate();
		return "student_saved_tl";
	}

	@PostMapping("/add_student")
	public String saveStudent(HttpServletRequest request, @ModelAttribute("student") Student student) {
		dao.insertStudent(student);

		HttpSession session = request.getSession();
		session.setAttribute("student", student);

		return "redirect:/student_saved";
	}

}

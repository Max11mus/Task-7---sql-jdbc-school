package ua.com.foxminded.lms.sqljdbcschool.controllers;

import java.util.HashMap;
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
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

@Controller
@Lazy
public class FindGroupsStudentCountLessOrEqualsController {
	@Autowired
	SchoolDAO dao;
	boolean posted = false; 
	HashMap<Group, Integer> studentCountGroups;
	
	@GetMapping("/find_groups_student_countlessorequals")
	public String showAddStudentForm(Model model) {
		posted = false;
		model.addAttribute("studentcount", new Integer(0));
		
		return "find_groups_student_countlessorequals";
	}

	@PostMapping("/find_groups_student_countlessorequals")
	public String saveStudent(@ModelAttribute("studentcount") Integer studentCount, Model model) {
		String msg = "";

		if (!posted) {

			if (studentCount < 0) {
				msg += "Student Count " + studentCount + " Must be Greater then Zero ! ";
			}

			if (msg.isEmpty()) {
				msg = "Groups with student count <= " + studentCount;
				studentCountGroups = dao.findGroupsStudentCountLessOrEquals(studentCount);
			}
			
			posted = true;
		}

		model.addAttribute("studentcountgroups", studentCountGroups);
		model.addAttribute("msg", msg);

		return "finded_groups_student_countlessorequals";
	}

}

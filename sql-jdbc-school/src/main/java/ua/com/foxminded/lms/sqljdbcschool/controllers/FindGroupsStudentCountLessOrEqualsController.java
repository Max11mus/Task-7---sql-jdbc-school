package ua.com.foxminded.lms.sqljdbcschool.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;

@Controller
public class FindGroupsStudentCountLessOrEqualsController {
	@Autowired
	@Lazy
	SchoolDAO dao;
	boolean posted = false;
	HashMap<Group, Integer> studentCountGroups;

	@GetMapping("/find_groups_student_countlessorequals")
	public String showAddStudentForm(Model model) {
		posted = false;
		model.addAttribute("studentcount", new Integer(0));

		return "find_groups_student_countlessorequals_tl";
	}

	@PostMapping("/find_groups_student_countlessorequals")
	public String saveStudent(@ModelAttribute("studentcount") Integer studentCount, Model model) {
		StringBuilder msg = new StringBuilder();

		if (!posted) {

			if (studentCount < 0) {
				msg.append("Student Count ")
						.append(studentCount)
						.append(" Must be Greater then Zero ! ");
			}

			if (msg.length() == 0) {
				msg.append("Groups with student count <= ")
						.append(studentCount);
				studentCountGroups = dao.findGroupsStudentCountLessOrEquals(studentCount);
			}

			posted = true;
		}

		model.addAttribute("studentcountgroups", studentCountGroups);
		model.addAttribute("msg", msg.toString());

		return "finded_groups_student_countlessorequals_tl";
	}

}

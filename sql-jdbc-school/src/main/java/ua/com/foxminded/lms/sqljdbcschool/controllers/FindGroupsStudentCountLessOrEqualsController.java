package ua.com.foxminded.lms.sqljdbcschool.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.hibernate.SchoolHibernateDAO;

import java.util.Map;

@Controller
public class FindGroupsStudentCountLessOrEqualsController {
	@Autowired
	SchoolDAO dao;

	Map<Group, Integer> studentCountGroups;

	@GetMapping("/find_groups_student_countlessorequals")
	public String enterStudentsCountForm(Model model) {
		model.addAttribute("studentcount", Integer.valueOf(0));

		return "find_groups_student_countlessorequals_tl";
	}

	@PostMapping("/find_groups_student_countlessorequals")
	public String showGroupsStudentsCountForm(@ModelAttribute("studentcount") Integer studentCount, Model model) {
		StringBuilder msg = new StringBuilder();

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

		model.addAttribute("studentcountgroups", studentCountGroups);
		model.addAttribute("msg", msg.toString());

		return "finded_groups_student_countlessorequals_tl";
	}

}

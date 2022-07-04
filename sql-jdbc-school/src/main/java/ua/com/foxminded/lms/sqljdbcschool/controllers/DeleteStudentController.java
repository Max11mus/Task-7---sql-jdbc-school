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
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

@Controller
public class DeleteStudentController {
	@Autowired
	@Lazy
	SchoolDAO dao;
	boolean posted = false;
	List<Student> students;

	@GetMapping("/delete_student")
	public String showAddStudentForm(Model model) {
		posted = false;
		students = dao.getAllStudents();
		model.addAttribute("students", students);
		model.addAttribute("studentrowno", new Integer(0));
		return "delete_student_tl";
	}

	@PostMapping("/delete_student")
	public String saveStudent(@ModelAttribute("studentrowno") Integer studentRowNo, Model model) {
		StringBuilder msg = new StringBuilder();

		if (!posted) {
			if (!students.isEmpty()) {
				if (studentRowNo < 1 || studentRowNo > students.size()) {
					msg.append("RowNo ")
							.append(studentRowNo)
							.append(" is out of range (1 - ")
							.append(students.size())
							.append(") ");
				} else {
					msg.append("Student Deleted: ")
							.append(students.get(studentRowNo - 1).toString())
							.append(" !!!");
					dao.deleteStudent(students.get(studentRowNo - 1).getUuid());
					students.remove(studentRowNo - 1);
					posted = true;
				}
			} else {
				msg.append("No students is present !!!");
			}
		}

		model.addAttribute("students", students);
		model.addAttribute("msg", msg.toString());

		return "student_deleted_tl";
	}

}

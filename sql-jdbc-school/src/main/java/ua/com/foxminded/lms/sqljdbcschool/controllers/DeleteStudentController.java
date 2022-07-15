package ua.com.foxminded.lms.sqljdbcschool.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ua.com.foxminded.lms.sqljdbcschool.jdbc.SchoolJdbcDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class DeleteStudentController {
	@Autowired
	@Lazy
	SchoolJdbcDAO dao;

	@GetMapping("/delete_student")
	public String showDeleteStudentForm(Model model) {
		List<Student> students = dao.getAllStudents();
		model.addAttribute("students", students);
		return "delete_student_tl";
	}

	@PostMapping("/delete_student")
	public String deleteStudent(HttpServletRequest request, @ModelAttribute("studentrowno") Integer studentRowNo) {
		StringBuilder msg = new StringBuilder();
		HttpSession session = request.getSession();

		List<Student> students = dao.getAllStudents();

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
			}
		} else {
			msg.append("No students is present !!!");
		}

		session.setAttribute("students", students);
		session.setAttribute("msg", msg.toString());

		return "redirect:/student_deleted";
	}

	@GetMapping("/student_deleted")
	public String showDeletedStudent(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		List<Student> students = (List<Student>) session.getAttribute("students");
		String msg = (String) session.getAttribute("msg");

		if (students == null) {
			session.invalidate();
			return "redirect:/delete_student";
		}

		model.addAttribute("students", students);
		model.addAttribute("msg", msg);

		session.invalidate();
		return "student_deleted_tl";
	}

}

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
@Lazy
public class DeleteStudentController {
		@Autowired
		SchoolDAO dao;
		boolean posted = false;
		List<Student> students; 
		
		@GetMapping("/delete_student")
		public String showAddStudentForm(Model model) {
			posted = false;
			students = dao.getAllStudents();
			model.addAttribute("students", students);
			model.addAttribute("studentrowno", new Integer(0));
			return "delete_student";
		}

		@PostMapping("/delete_student")
		public String saveStudent(@ModelAttribute("studentrowno") Integer studentRowNo, Model model) {
			String msg = "";

			if (!posted) {
				if (!students.isEmpty()) {
					if (studentRowNo < 1 || studentRowNo > students.size()) {
						msg = "RowNo " + studentRowNo + " is out of range (1 - " + students.size() + ")";
					} else {
						msg = "Student Deleted: " + students.get(studentRowNo - 1).toString() + " !!!";
						dao.deleteStudent(students.get(studentRowNo - 1).getUuid());
						students.remove(studentRowNo - 1);
						posted = true;
					}
				} else {
					msg = "No students is present !!!";
				}
			}
			
			model.addAttribute("students", students);
			model.addAttribute("msg", msg);

			return "student_deleted";
		}

	}

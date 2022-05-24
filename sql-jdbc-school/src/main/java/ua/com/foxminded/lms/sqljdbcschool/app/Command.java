package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.Scanner;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;

public abstract class Command implements Runnable {
	protected Scanner input;
	protected PrintWriter output;
	protected SchoolDAO dao;
	protected String name="";
	
	public Command(String name) {
		super();
		this.name = name;
	}

	public Command(Scanner input, PrintWriter output, SchoolDAO dao) {
		super();
		this.input = input;
		this.output = output;
		this.dao = dao;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	


}

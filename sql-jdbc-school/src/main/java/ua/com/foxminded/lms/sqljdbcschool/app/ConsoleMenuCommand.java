package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;

public abstract class ConsoleMenuCommand implements Command {
	protected Scanner input;
	protected PrintWriter output;
	@Autowired
	protected SchoolDAO dao;
	protected String name="";

	public ConsoleMenuCommand(Scanner input, PrintWriter output) {
		super();
		this.input = input;
		this.output = output;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	protected int inputIntFromRange(int lowValue, int highValue) {
		if (lowValue > highValue) {
			throw new IllegalArgumentException("ERROR: highValue must be greater then lowValue .");
		}

		int result = 0;

		try {
			result = Integer.parseInt(input.nextLine());
		} catch (NumberFormatException e) {
			output.println("Invalid selection. Numbers only please.");
		}

		if (result < lowValue || result > highValue) {
			output.println("Value is outside of range - [" + lowValue + ":" + highValue + "].");
		} else {
			return result;
		}

		output.println("Once more");
		return inputIntFromRange(lowValue, highValue);
	}
}

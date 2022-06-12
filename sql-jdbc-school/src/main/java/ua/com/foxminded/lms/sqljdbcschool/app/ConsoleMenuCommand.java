package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.Scanner;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;

public abstract class ConsoleMenuCommand implements Command, ApplicationContextAware {
	protected Scanner input;
	protected PrintWriter output;
	protected SchoolDAO dao;
	protected String name="";
	private ApplicationContext context = null;

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
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
		this.dao = applicationContext.getBean("dao", SchoolDAO.class);
	};
	
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

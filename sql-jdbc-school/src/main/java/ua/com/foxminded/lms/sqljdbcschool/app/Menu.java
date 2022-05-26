package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
	private ArrayList<ConsoleMenuCommand> menuOptions = new ArrayList<ConsoleMenuCommand>();
	protected Scanner input;
	protected PrintWriter output;
	protected String name="";
	
	public Menu(Scanner input, PrintWriter output, String name) {
		super();
		this.input = input;
		this.output = output;
		this.name = name;
	}

	public void runCycle() {
			int exitChoice = menuOptions.size();
			int choice = -1;
			while (choice != exitChoice) {
				
				output.println();
				output.println(name);
				menuOptions.forEach(command -> output.println(menuOptions.indexOf(command) + ". " + command.getName()));
				output.println(menuOptions.size() + ". Exit");
				output.println("   Select option:");

				try {
					choice = Integer.parseInt(input.nextLine());

					if (choice < 0 || choice > menuOptions.size()) {
						output.println("Choice outside of range. Please choose again.");
					} else if (choice != exitChoice) {
						menuOptions.get(choice).run();
					}
				} catch (NumberFormatException e) {
					output.println("Invalid selection. Numbers only please.");
					choice = -1;
				}
			}
	}

	public void addMenuOption(ConsoleMenuCommand command) {
		menuOptions.add(command);
	}

	public void clearMenuOptions() {
		menuOptions.clear();
	}

}

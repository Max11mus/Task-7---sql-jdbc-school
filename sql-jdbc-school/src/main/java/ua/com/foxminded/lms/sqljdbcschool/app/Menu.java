package ua.com.foxminded.lms.sqljdbcschool.app;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu extends MenuItem {
	private ArrayList<MenuItem> commands;
	static private Scanner keyInput = new Scanner(System.in);

	public Menu(String greetings) {
		this();
		this.greetings = greetings;
	}

	public Menu() {
		super();
		commands = new ArrayList<MenuItem>();
		execute = () -> {
			System.out.println();
			commands.forEach((command) -> System.out.println(commands.indexOf(command) + ". " + command.greetings));
			System.out.println(commands.size() + ". Exit");
			System.out.println("   Select command:");
		};
	}

	public void runCommandThenExit() {

		try {
			int exitChoice = commands.size();
			int choice = -1;
			while (choice != exitChoice) {
				System.out.println();
				System.out.println(greetings);
				execute.run();

				try {
					choice = Integer.parseInt(keyInput.nextLine());

					if (choice < 0 || choice > commands.size()) {
						System.out.println("Choice outside of range. Please chose again.");
					} else if (choice != exitChoice) {
						if (commands.get(choice) instanceof Menu) {
							((Menu) commands.get(choice)).runCycle();
						} else if (commands.get(choice) instanceof MenuItem) {
							commands.get(choice).run();
						}
						choice = exitChoice;
					}

				} catch (NumberFormatException e) {
					System.out.println("Invalid selection. Numbers only please.");
					choice = -1;
				}
			}
		} finally {
		}

	}

	public void runCycle() {
		try {
			int exitChoice = commands.size();
			int choice = -1;
			while (choice != exitChoice) {
				System.out.println();
				System.out.println(greetings);
				execute.run();

				try {
					choice = Integer.parseInt(keyInput.nextLine());

					if (choice < 0 || choice > commands.size()) {
						System.out.println("Choice outside of range. Please chose again.");
					} else if (choice != exitChoice) {
						if (commands.get(choice) instanceof Menu) {
							((Menu) commands.get(choice)).runCycle();
						} else if (commands.get(choice) instanceof MenuItem) {
							commands.get(choice).run();
						}
					}

				} catch (NumberFormatException e) {
					System.out.println("Invalid selection. Numbers only please.");
					choice = -1;
				}
			}
		} finally {
		}
	}

	public void addCommand(MenuItem command) {
		commands.add(command);
	}

	public void clearCommands() {
		commands.clear();
	}
	
	public void closeInput() {
		keyInput.close();
	}

}

package ua.com.foxminded.lms.sqljdbcschool.app;

public class MenuItem{
	protected String greetings;
	protected Runnable execute = ( ) -> {}; 
	
	public MenuItem() {
		super();
		greetings="";
	}
	
	public MenuItem(String greetings, Runnable execute) {
		super();
		this.greetings = greetings;
		this.execute = execute;
	}
	
	public void run() {
		System.out.println();
		System.out.println(greetings);
		execute.run();
	} 
	
}

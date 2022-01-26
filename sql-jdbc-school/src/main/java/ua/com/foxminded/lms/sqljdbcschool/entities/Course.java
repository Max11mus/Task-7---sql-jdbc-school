package ua.com.foxminded.lms.sqljdbcschool.entities;

public class Course {
	private int id;
	private String courseName;
	private String coursedescription;

	public Course(int id, String courseName, String coursedescription) {
		this.id = id;
		this.courseName = courseName;
		this.coursedescription = coursedescription;
	}

	public Course() {
		id = 0;
		courseName = "";
		coursedescription = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCoursedescription() {
		return coursedescription;
	}

	public void setCoursedescription(String coursedescription) {
		this.coursedescription = coursedescription;
	}

}
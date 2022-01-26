package ua.com.foxminded.lms.sqljdbcschool.entities;

public class Group {
	private int id;
	private String groupName;

	public Group() {
		id = 0;
		groupName = "";
	}

	public Group(int id, String studentName) {
		super();
		this.id = id;
		this.groupName = studentName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStudentName() {
		return groupName;
	}

	public void setStudentName(String studentName) {
		this.groupName = studentName;
	}

}

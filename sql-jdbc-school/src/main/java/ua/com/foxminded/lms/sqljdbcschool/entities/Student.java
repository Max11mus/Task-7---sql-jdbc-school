package ua.com.foxminded.lms.sqljdbcschool.entities;

public class Student {
	private int id;
	private int groupId;
	private String studentFirstName;
	private String studentLastName;

	public Student() {
		id = 0;
		groupId = 0;
		studentFirstName = "";
		studentLastName = "";
	}

	public Student(int id, int groupId, String studentFirstName, String studentLastName) {
		super();
		this.id = id;
		this.groupId = groupId;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getStudentFirstName() {
		return studentFirstName;
	}

	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}

	public String getStudentLastName() {
		return studentLastName;
	}

	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}

}

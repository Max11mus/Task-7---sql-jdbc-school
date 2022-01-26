package ua.com.foxminded.lms.sqljdbcschool.dao;

public class GroupDAO {
	private int id;
	private String groupName;

	public GroupDAO() {
		id = 0;
		groupName = "";
	}

	public GroupDAO(int id, String studentName) {
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

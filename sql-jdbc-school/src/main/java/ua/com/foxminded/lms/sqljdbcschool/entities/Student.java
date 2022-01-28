package ua.com.foxminded.lms.sqljdbcschool.entities;

import java.util.Objects;
import java.util.UUID;

public class Student {
	private UUID id;
	private UUID groupId;
	private String studentFirstName;
	private String studentLastName;

	public Student() {
		groupId = UUID.randomUUID();
		id = UUID.randomUUID();
		studentFirstName = "";
		studentLastName = "";
	}

	public Student(UUID id, UUID groupId, String studentFirstName, String studentLastName) {
		super();
		this.id = id;
		this.groupId = groupId;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getGroupId() {
		return groupId;
	}

	public void setGroupId(UUID groupId) {
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

	@Override
	public int hashCode() {
		return Objects.hash(groupId, id, studentFirstName, studentLastName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		return Objects.equals(groupId, other.groupId) && Objects.equals(id, other.id)
				&& Objects.equals(studentFirstName, other.studentFirstName)
				&& Objects.equals(studentLastName, other.studentLastName);
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", groupId=" + groupId + ", studentFirstName=" + studentFirstName
				+ ", studentLastName=" + studentLastName + "]";
	}

}
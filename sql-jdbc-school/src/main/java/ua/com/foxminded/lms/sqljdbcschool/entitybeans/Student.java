package ua.com.foxminded.lms.sqljdbcschool.entitybeans;

import java.util.Objects;
import java.util.UUID;

public class Student {
	private UUID id;
	private UUID squadId;
	private String studentFirstName;
	private String studentLastName;

	public Student() {
		squadId = UUID.randomUUID();
		id = UUID.randomUUID();
		studentFirstName = "";
		studentLastName = "";
	}

	public Student(UUID id, UUID groupId, String studentFirstName, String studentLastName) {
		super();
		this.id = id;
		this.squadId = groupId;
		this.studentFirstName = studentFirstName;
		this.studentLastName = studentLastName;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getSquadId() {
		return squadId;
	}

	public void setSquadId(UUID groupId) {
		this.squadId = groupId;
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
		return Objects.hash(squadId, id, studentFirstName, studentLastName);
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
		return Objects.equals(squadId, other.squadId) && Objects.equals(id, other.id)
				&& Objects.equals(studentFirstName, other.studentFirstName)
				&& Objects.equals(studentLastName, other.studentLastName);
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", groupId=" + squadId + ", studentFirstName=" + studentFirstName
				+ ", studentLastName=" + studentLastName + "]";
	}

}
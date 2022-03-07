package ua.com.foxminded.lms.sqljdbcschool.entitybeans;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

public class Student implements Comparable<Student>{
	private UUID id;
	private UUID groupId;
	private String studentFirstName;
	private String studentLastName;

	public Student() {
		groupId = new UUID(0, 0);
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
		return Objects.hash(id);
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

	public void enrollTo(Group group) {
		if (group == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		this.groupId=group.getId();
	}

	public void dropOutFromAnyGroup() {
		this.groupId= new UUID(0, 0);
	}
	
	public void dropOutFrom(Group group) {
		if (group == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		if (this.groupId.equals(group.getId())) {
			this.groupId = new UUID(0, 0);
		}
	}

	static public void enrollTo(ArrayList<Student> students, Group group) {
		if (students == null || group == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		IntStream.iterate(0, i -> i + 1).limit(students.size())
				.forEach(i -> students.get(i).enrollTo(group));
	}
	
	static public void enrollTo(ArrayList<Student> students, ArrayList<Group> groups) {
		if (students == null || groups == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		IntStream.iterate(0, i -> i + 1).limit(groups.size())
		.forEach(i -> enrollTo(students, groups.get(i)));
	}
	
	static public void dropoutFrom(ArrayList<Student> students, Group group) {
		if (students == null || group == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		IntStream.iterate(0, i -> i + 1).limit(students.size())
				.forEach(i -> students.get(i).dropOutFrom(group));
	}
	
	static public void dropoutFrom(ArrayList<Student> students, ArrayList<Group> groups) {
		if (students == null || groups == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		IntStream.iterate(0, i -> i + 1).limit(groups.size())
				.forEach(i -> dropoutFrom(students, groups.get(i)));
	}
	
	@Override
	public int compareTo(Student o) {
		return id.compareTo(o.getId());
	}
	
}
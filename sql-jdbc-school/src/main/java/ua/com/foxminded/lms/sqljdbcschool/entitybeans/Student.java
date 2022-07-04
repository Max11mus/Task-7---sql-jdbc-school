package ua.com.foxminded.lms.sqljdbcschool.entitybeans;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="student")
public class Student implements Comparable<Student> {
	@Column(name="uuid")
	private String uuid;
	@Column(name="group_uuid")
	private String groupUuid;
	@Column(name="first_name")
	private String firstName;
	@Column(name="last_name")
	private String lastName;

	public Student() {
		uuid = UUID.randomUUID().toString();
		groupUuid = null;
		firstName = "";
		lastName = "";
	}

	public Student(String id, String groupId, String studentFirstName, String studentLastName) {
		super();
		this.uuid = id;
		this.groupUuid = groupId;
		this.firstName = studentFirstName;
		this.lastName = studentLastName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String id) {
		this.uuid = id;
	}

	public String getGroupUuid() {
		return groupUuid;
	}

	public void setGroupUuid(String groupId) {
		this.groupUuid = groupId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid);
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
		return Objects.equals(groupUuid, other.groupUuid) && Objects.equals(uuid, other.uuid)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName);
	}

	@Override
	public String toString() {
		return "Student [uuid=" + uuid + ", groupUuid=" + groupUuid + ", studentFirstName=" + firstName
				+ ", studentLastName=" + lastName + "]";
	}

	@Override
	public int compareTo(Student o) {
		return uuid.compareTo(o.getUuid().toString());
	}

}
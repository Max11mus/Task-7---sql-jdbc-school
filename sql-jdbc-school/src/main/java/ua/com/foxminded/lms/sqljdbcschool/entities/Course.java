package ua.com.foxminded.lms.sqljdbcschool.entities;

import java.util.Objects;
import java.util.UUID;

public class Course {
	private UUID id;
	private String courseName;
	private String courseDescription;

	public Course() {
		id = UUID.randomUUID();
		courseName = "";
		courseDescription = "";
	}
	
	public Course(UUID id, String courseName, String courseDescription) {
		this.id = id;
		this.courseName = courseName;
		this.courseDescription = courseDescription;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	@Override
	public int hashCode() {
		return Objects.hash(courseDescription, courseName, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		return Objects.equals(courseDescription, other.courseDescription)
				&& Objects.equals(courseName, other.courseName) && Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", courseName=" + courseName + ", courseDescription=" + courseDescription + "]";
	}

}
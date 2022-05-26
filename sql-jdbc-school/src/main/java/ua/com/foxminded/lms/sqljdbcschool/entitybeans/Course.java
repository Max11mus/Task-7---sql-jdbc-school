package ua.com.foxminded.lms.sqljdbcschool.entitybeans;

import java.util.Objects;
import java.util.UUID;

public class Course implements Comparable<Course> {
	private String uuid;
	private String courseName;
	private String courseDescription;
	public Course() {
		uuid = UUID.randomUUID().toString();
		courseName = "";
		courseDescription = "";
	}
	
	public Course(String id, String courseName, String courseDescription) {
		this.uuid = id;
		this.courseName = courseName;
		this.courseDescription = courseDescription;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String id) {
		this.uuid = id;
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
	public String toString() {
		return "Course [uuid=" + uuid + ", courseName=" + courseName + ", courseDescription=" + courseDescription + "]";
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
		Course other = (Course) obj;
		return Objects.equals(courseDescription, other.courseDescription)
				&& Objects.equals(courseName, other.courseName)
				&& Objects.equals(uuid, other.uuid); 
	}

	@Override
	public int compareTo(Course o) {
		return uuid.compareTo(o.getUuid().toString());
	}

}
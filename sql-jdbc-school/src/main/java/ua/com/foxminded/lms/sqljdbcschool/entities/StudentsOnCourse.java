package ua.com.foxminded.lms.sqljdbcschool.entities;

import java.util.Objects;
import java.util.UUID;

public class StudentsOnCourse {
	private UUID studentId;
	private UUID courseId;
	
	
	public StudentsOnCourse() {
		studentId = UUID.randomUUID();
		courseId = UUID.randomUUID();
	}

	public StudentsOnCourse(UUID studentId, UUID courseId) {
		super();
		this.studentId = studentId;
		this.courseId = courseId;
	}

	public UUID getStudentId() {
		return studentId;
	}

	public void setStudentId(UUID studentId) {
		this.studentId = studentId;
	}

	public UUID getCourseId() {
		return courseId;
	}

	public void setCourseId(UUID courseId) {
		this.courseId = courseId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(courseId, studentId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentsOnCourse other = (StudentsOnCourse) obj;
		return Objects.equals(courseId, other.courseId) && Objects.equals(studentId, other.studentId);
	}

	@Override
	public String toString() {
		return "StudentsOnCourse [studentId=" + studentId + ", courseId=" + courseId + "]";
	}

	
}

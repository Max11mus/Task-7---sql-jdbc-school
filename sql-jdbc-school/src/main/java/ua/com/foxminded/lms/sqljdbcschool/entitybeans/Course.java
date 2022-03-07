package ua.com.foxminded.lms.sqljdbcschool.entitybeans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

public class Course implements Comparable<Course> {
	private UUID id;
	private String courseName;
	private String courseDescription;
	private HashSet<Student> enrolledStudents;
	
	public Course() {
		id = UUID.randomUUID();
		courseName = "";
		courseDescription = "";
		enrolledStudents = new HashSet<Student>();
	}
	
	public Course(UUID id, String courseName, String courseDescription) {
		this.id = id;
		this.courseName = courseName;
		this.courseDescription = courseDescription;
		this.enrolledStudents = new HashSet<Student>();
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

	public HashSet<Student> getEnrolledStudents() {
		return enrolledStudents;
	}

	public void setEnrolledStudents(HashSet<Student> enrolledStudents) {
		this.enrolledStudents = enrolledStudents;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", courseName=" + courseName + ", courseDescription=" + courseDescription
				+ ", enrolledStudents=" + enrolledStudents + "]";
	}

	public void enroll(Student student) {
		if (student == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		enrolledStudents.add(student);
	}
	
	public void dropout(Student student) {
		if (student == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		enrolledStudents.remove(student);
	}
	
	public void enroll(ArrayList<Student> students) {
		if (students == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		enrolledStudents.addAll(students);
	}
	
	public void dropout(ArrayList<Student> students) {
		if (students == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		enrolledStudents.removeAll(students);
	}
	
	
	static public void enrollTo(ArrayList<Student> students, Course course) {
		if (students == null || course == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		course.getEnrolledStudents().addAll((students));
	}
	
	static public void enrollTo(ArrayList<Student> students, ArrayList<Course> courses) {
		if (students == null || courses == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		IntStream.iterate(0, i -> i + 1)
				.limit(courses.size())
				.forEach(i -> courses.get(i).getEnrolledStudents().addAll(students));
	}
	
	static public void dropoutFrom(ArrayList<Student> students, Course course) {
		if (students == null || course == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		course.getEnrolledStudents().removeAll((students));
	}
	
	static public void dropoutFrom(ArrayList<Student> students, ArrayList<Course> courses) {
		if (students == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}
		IntStream.iterate(0, i -> i + 1)
				.limit(courses.size())
				.forEach(i -> courses.get(i).getEnrolledStudents().removeAll(students));
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
		Course other = (Course) obj;
		return Objects.equals(courseDescription, other.courseDescription)
				&& Objects.equals(courseName, other.courseName)
				&& Objects.equals(enrolledStudents, other.enrolledStudents) && Objects.equals(id, other.id); 
	}

	@Override
	public int compareTo(Course o) {
		return id.compareTo(o.getId());
	}

}
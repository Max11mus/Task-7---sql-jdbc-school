package ua.com.foxminded.lms.sqljdbcschool.entitybeans;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="course")
public class Course implements Comparable<Course> {

	@Id
	@Column(name="uuid", length = 36, nullable = false)
    private String uuid;

	@Column(name = "course_name", length = 255, nullable = false)
    private String courseName;

	@Column(name = "course_description", length = 1024, nullable = false)
    private String courseDescription;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    public Course() {
        this.uuid = UUID.randomUUID().toString();
        this.courseName = "";
        this.courseDescription = "";
    }

    public Course(String id, String courseName, String courseDescription) {
        this.uuid = id;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
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
                && Objects.equals(courseName, other.courseName) && Objects.equals(uuid, other.uuid);
    }

    @Override
    public int compareTo(Course o) {
        return uuid.compareTo(o.getUuid().toString());
    }

}
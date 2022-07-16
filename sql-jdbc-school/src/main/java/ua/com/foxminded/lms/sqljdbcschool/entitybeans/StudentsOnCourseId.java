package ua.com.foxminded.lms.sqljdbcschool.entitybeans;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StudentsOnCourseId implements Serializable {   // Used only  in Hibernate DAO
    private static final long serialVersionUID = -1048542367752011817L;
    @Column(name = "student_uuid", nullable = false, length = 36)
    private String studentUuid;

    @Column(name = "course_uuid", nullable = false, length = 36)
    private String courseUuid;

    public String getStudentUuid() {
        return studentUuid;
    }

    public void setStudentUuid(String studentUuid) {
        this.studentUuid = studentUuid;
    }

    public String getCourseUuid() {
        return courseUuid;
    }

    public void setCourseUuid(String courseUuid) {
        this.courseUuid = courseUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentsOnCourseId entity = (StudentsOnCourseId) o;
        return Objects.equals(this.courseUuid, entity.courseUuid) &&
                Objects.equals(this.studentUuid, entity.studentUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseUuid, studentUuid);
    }

}
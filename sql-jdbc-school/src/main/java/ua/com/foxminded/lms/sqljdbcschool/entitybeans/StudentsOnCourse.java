package ua.com.foxminded.lms.sqljdbcschool.entitybeans;

import javax.persistence.*;

@Entity
@Table(name = "students_on_course")
public class StudentsOnCourse {         // Used only  in Hibernate DAO
    @EmbeddedId
    private StudentsOnCourseId id;

    @MapsId("studentUuid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_uuid", nullable = false)
    private Student student;

    @MapsId("courseUuid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_uuid", nullable = false)
    private Course course;

    public StudentsOnCourseId getId() {
        return id;
    }

    public void setId(StudentsOnCourseId id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

}
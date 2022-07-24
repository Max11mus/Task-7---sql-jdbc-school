package ua.com.foxminded.lms.sqljdbcschool.entitybeans;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "student")
public class Student implements Comparable<Student> {
    @Id
    @Column(name = "uuid", length = 36, nullable = false)
    private String uuid;

    @Access(AccessType.PROPERTY)
    @ManyToOne
    @JoinColumn(name = "group_uuid")
    private Group group;

    @Transient
    private String groupUuid;

    @Column(name = "first_name", length = 20, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 20, nullable = false)
    private String lastName;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "students_on_course",
            joinColumns = {@JoinColumn(name = "student_uuid")},
            inverseJoinColumns = {@JoinColumn(name = "course_uuid")})
    private Set<Course> courses=  new HashSet<>();

    public Student() {
        this.uuid = UUID.randomUUID().toString();
        this.group = null;
        this.groupUuid = null;
        this.firstName = "";
        this.lastName = "";
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
        if (this.group != null) {
            this.groupUuid = this.group.getUuid();
        } else {
            this.groupUuid = null;
        }
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
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
        return Objects.equals(uuid, other.uuid)
                && Objects.equals(groupUuid, other.groupUuid)
                && Objects.equals(firstName, other.firstName)
                && Objects.equals(lastName, other.lastName);
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
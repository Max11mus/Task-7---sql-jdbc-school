package ua.com.foxminded.lms.sqljdbcschool.entitybeans;

import javax.persistence.*;
import java.util.Objects;
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
    private Group group; // Used only  in Hibernate DAO

    @Transient
    private String groupUuid;

    @Column(name = "first_name", length = 20, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 20, nullable = false)
    private String lastName;

    public Student() {
        uuid = UUID.randomUUID().toString();
        group = null; // Used only  in Hibernate DAO
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
package ua.com.foxminded.lms.sqljdbcschool.dao;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

import java.util.HashMap;
import java.util.List;

public interface SchoolDAO {
    void insertGroups(List<Group> groups);

    void insertGroup(Group group);

    void insertStudents(List<Student> students);

    void insertStudent(Student student);

    void insertCourses(List<Course> courses);

    List<Student> getAllStudents();

    void deleteStudent(String studentUuid);

    HashMap<Group, Integer> findGroupsStudentCountLessOrEquals(int studentCount);

    List<Course> getAllCourses();

    List<Student> findStudentsByCourseID(String courseUuid);

    void addStudentToCourse(String studentId, String courseId);

    List<Course> findStudentCourses(String studentUuid);

    void dropoutStudentFromCourse(String studentUuid, String courseUuid);
}

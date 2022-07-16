package ua.com.foxminded.lms.sqljdbcschool.dao;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SchoolDAO {
    public void insertGroups(List<Group> groups);

    public void insertGroup(Group group);

    public void insertStudents(List<Student> students);

    public void insertStudent(Student student);

    public void insertCourses(List<Course> courses);

    public void insertCourse(Course course);

    public List<Student> getAllStudents();

    public void deleteStudent(String studentUuid);

    public Map<Group, Integer> findGroupsStudentCountLessOrEquals(int studentCount);

    public List<Course> getAllCourses();

    public List<Student> findStudentsByCourseID(String courseUuid);

    public void addStudentToCourse(String studentId, String courseId);

    public List<Course> findStudentCourses(String studentUuid);

    public void dropoutStudentFromCourse(String studentUuid, String courseUuid);

}
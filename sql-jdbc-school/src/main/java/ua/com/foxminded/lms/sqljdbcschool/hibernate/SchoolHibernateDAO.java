package ua.com.foxminded.lms.sqljdbcschool.hibernate;

import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

import java.util.HashMap;
import java.util.List;

public class SchoolHibernateDAO implements SchoolDAO {

    @Override
    public void insertGroups(List<Group> groups) {

    }

    @Override
    public void insertGroup(Group group) {

    }

    @Override
    public void insertStudents(List<Student> students) {

    }

    @Override
    public void insertStudent(Student student) {

    }

    @Override
    public void insertCourses(List<Course> courses) {

    }

    @Override
    public List<Student> getAllStudents() {
        return null;
    }

    @Override
    public void deleteStudent(String studentUuid) {

    }

    @Override
    public HashMap<Group, Integer> findGroupsStudentCountLessOrEquals(int studentCount) {
        return null;
    }

    @Override
    public List<Course> getAllCourses() {
        return null;
    }

    @Override
    public List<Student> findStudentsByCourseID(String courseUuid) {
        return null;
    }

    @Override
    public void addStudentToCourse(String studentId, String courseId) {

    }

    @Override
    public List<Course> findStudentCourses(String studentUuid) {
        return null;
    }

    @Override
    public void dropoutStudentFromCourse(String studentUuid, String courseUuid) {

    }
}

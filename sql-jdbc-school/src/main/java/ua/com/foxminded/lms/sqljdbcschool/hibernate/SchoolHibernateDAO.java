package ua.com.foxminded.lms.sqljdbcschool.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SchoolHibernateDAO implements SchoolDAO {
    @Autowired
    HibernateSessionFactory sessionFactory;

    @Override
    public void insertGroups(List<Group> groups) {
        groups.parallelStream().forEach(this::insertGroup);
    }

    @Override
    public void insertGroup(Group group) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.save(group);

            transaction.commit();

        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void insertStudents(List<Student> students) {
        students.parallelStream().forEach(this::insertStudent);
    }

    @Override
    public void insertStudent(Student student) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.save(student);

            transaction.commit();

        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void insertCourses(List<Course> courses) {
        courses.parallelStream().forEach(this::insertCourse);
    }

    @Override
    public void insertCourse(Course course) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.save(course);

            transaction.commit();

        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Student> getAllStudents() {
        Session session = null;
        Transaction transaction = null;
        List<Student> students = null;

        try {
            session = sessionFactory.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            students = session.createQuery("FROM Student", Student.class).getResultList();

            transaction.commit();

        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return students;
    }

    @Override
    public void deleteStudent(String studentUuid) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.createQuery("DELETE FROM StudentsOnCourse AS entity  WHERE entity.id.studentUuid =:id")
                    .setParameter("id", studentUuid)
                    .executeUpdate();

            session.createQuery("DELETE FROM Student entity  WHERE entity.uuid =:id")
                    .setParameter("id", studentUuid)
                    .executeUpdate();

            transaction.commit();

        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Map<Group, Integer> findGroupsStudentCountLessOrEquals(int studentCount) {
        Session session = null;
        Transaction transaction = null;
        Map<Group, Integer> groupStudentCount = null;

        try {
            session = sessionFactory.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            List<Object[]> results = session.createQuery(
                    "SELECT g.uuid, g.groupName, count(*) " +
                            " FROM Group g" +
                            " INNER JOIN Student s " +
                            " ON g.uuid = s.group.uuid" +
                            " GROUP BY g.uuid " +
                            " HAVING count(*) <= :studentCount")
                    .setParameter("studentCount", (long) studentCount)
                    .getResultList();

            groupStudentCount = results
                    .parallelStream().collect(Collectors.toMap(
                            object -> new Group((String) object[0], (String) object[1]),
                            object -> ((Long) object[2]).intValue()));

            transaction.commit();

        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return groupStudentCount;
    }

    @Override
    public List<Student> findStudentsByCourseID(String courseUuid) {
        Session session = null;
        Transaction transaction = null;
        List<Student> students = null;

        try {
            session = sessionFactory.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            List<Object[]> results = session.createQuery(
                            "SELECT sc.id.courseUuid, s.uuid, s.firstName, s.lastName, s.group.uuid " +
                                    " FROM StudentsOnCourse sc" +
                                    " INNER JOIN Student s " +
                                    " ON sc.id.studentUuid = s.uuid" +
                                    " where sc.id.courseUuid = :courseid")
                    .setParameter("courseid", courseUuid)
                    .getResultList();

            students = results
                    .parallelStream()
                    .map(object -> new Student(
                            (String) object[1],
                            (object.length < 5) ? null : (String) object[4],
                            (String) object[2],
                            (String) object[3]))
                    .collect(Collectors.toList());

            transaction.commit();

        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return students;
    }

    @Override
    public void addStudentToCourse(String studentId, String courseId) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);

            if (course != null && student != null) {
                StudentsOnCourse studentsOnCourse = new StudentsOnCourse();
                studentsOnCourse.setStudent(student);
                studentsOnCourse.setCourse(course);

                StudentsOnCourseId studentsOnCourseId = new StudentsOnCourseId();
                studentsOnCourseId.setCourseUuid(courseId);
                studentsOnCourseId.setStudentUuid(studentId);
                studentsOnCourse.setId(studentsOnCourseId);

                session.saveOrUpdate(studentsOnCourse);
            }

            transaction.commit();

        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    @Override
    public List<Course> findStudentCourses(String studentUuid) {
        Session session = null;
        Transaction transaction = null;
        List<Course> courses = null;

        try {
            session = sessionFactory.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            List<Object[]> results = session.createQuery(
                            "SELECT c, sc.id.studentUuid " +
                                    " FROM Course c " +
                                    " RIGHT JOIN StudentsOnCourse sc " +
                                    " ON c.uuid = sc.id.courseUuid" +
                                    " WHERE sc.id.studentUuid =:studentid")
                    .setParameter("studentid", studentUuid)
                    .getResultList();

            courses = results
                    .parallelStream()
                    .map(object -> (Course) object[0])
                    .collect(Collectors.toList());

            transaction.commit();

        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return courses;
    }

    @Override
    public List<Course> getAllCourses() {
        Session session = null;
        Transaction transaction = null;
        List<Course> courses = null;

        try {
            session = sessionFactory.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            courses = session.createQuery("FROM Course", Course.class).getResultList();

            transaction.commit();

        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return courses;
    }

    @Override
    public void dropoutStudentFromCourse(String studentUuid, String courseUuid) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Student student = session.get(Student.class, studentUuid);
            Course course = session.get(Course.class, courseUuid);

            if (course != null && student != null) {
                StudentsOnCourseId studentsOnCourseId = new StudentsOnCourseId();
                studentsOnCourseId.setCourseUuid(courseUuid);
                studentsOnCourseId.setStudentUuid(studentUuid);

                StudentsOnCourse studentsOnCourse = session.get(StudentsOnCourse.class, studentsOnCourseId);

                session.delete(studentsOnCourse);
            }

            transaction.commit();

        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }


    }

}

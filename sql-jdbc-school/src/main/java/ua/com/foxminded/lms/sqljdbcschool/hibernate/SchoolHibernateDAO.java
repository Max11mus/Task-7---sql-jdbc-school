package ua.com.foxminded.lms.sqljdbcschool.hibernate;

import org.springframework.stereotype.Component;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.utils.EntitiesGenerator;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class SchoolHibernateDAO implements SchoolDAO {
    private static final EntityManagerFactory entityManagerFactory
            = Persistence.createEntityManagerFactory("ua.com.foxminded.lms.sql_school");

    @Override
    public void insertGroups(List<Group> groups) {
        groups.forEach(this::insertGroup);
    }

    @Override
    public void insertGroup(Group group) {
        EntityManager entityManager = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.persist(group);

            entityManager.getTransaction().commit();

        } catch (Exception e) {
           throw new RuntimeException(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public void insertStudents(List<Student> students) {
        students.forEach(this::insertStudent);
    }

    @Override
    public void insertStudent(Student student) {
        EntityManager entityManager = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.persist(student);

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            try {
                throw new RuntimeException(e);
            } finally {
                if (entityManager != null) {
                    entityManager.close();
                }
            }
        }
    }

    @Override
    public void insertCourses(List<Course> courses) {
        courses.forEach(this::insertCourse);
    }

    @Override
    public void insertCourse(Course course) {
        EntityManager entityManager = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.persist(course);

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Student> getAllStudents() {
        EntityManager entityManager = null;
        List<Student> students = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            students = entityManager
                    .createQuery("SELECT entity FROM Student entity", Student.class)
                    .getResultList();

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return students;
    }

    @Override
    public void deleteStudent(String studentUuid) {
        EntityManager entityManager = null;
        Student student = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            student = entityManager.find(Student.class, studentUuid);
            if (student != null) {
                entityManager.remove(student);
            }

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public Map<Group, Integer> findGroupsStudentCountLessOrEquals(int studentCount) {
        EntityManager entityManager = null;
        Map<Group, Integer> groupStudentCount = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            List<Object[]> results = entityManager.createQuery(
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

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return groupStudentCount;
    }

    @Override
    public List<Student> findStudentsByCourseID(String courseUuid) {
        EntityManager entityManager = null;
        List<Student> students = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Course course = entityManager.find(Course.class, courseUuid);
            if (course != null) {
                students = new ArrayList<>(course.getStudents());
            }
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return students;
    }

    @Override
    public void addStudentToCourse(String studentId, String courseId) {
        EntityManager entityManager = null;
        Student student = null;
        Course course =null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            student = entityManager.find(Student.class, studentId);
            course = entityManager.find(Course.class, courseId);

            if (student != null && course != null) {
                course.getStudents().add(student);
                student.getCourses().add(course);
                entityManager.merge(student);
            }

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Course> findStudentCourses(String studentUuid) {
        EntityManager entityManager = null;
        List<Course> courses = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Student student = entityManager.find(Student.class, studentUuid);
            if (student != null) {
                courses = new ArrayList<>(student.getCourses());
            }
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return courses;
    }

    @Override
    public List<Course> getAllCourses() {
        EntityManager entityManager = null;
        List<Course> courses = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            courses = entityManager
                    .createQuery("SELECT entity FROM Course entity", Course.class)
                    .getResultList();

            entityManager.getTransaction().commit();

        } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (entityManager != null) {
                    entityManager.close();
                }
            }

            return courses;
        }

    @Override
    public void dropoutStudentFromCourse(String studentUuid, String courseUuid) {
        EntityManager entityManager = null;
        Student student = null;
        Course course =null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            student = entityManager.find(Student.class, studentUuid);
            course = entityManager.find(Course.class, courseUuid);

            if (student != null && course != null) {
                course.getStudents().remove(student);
                student.getCourses().remove(course);
                entityManager.merge(student);
            }

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

}

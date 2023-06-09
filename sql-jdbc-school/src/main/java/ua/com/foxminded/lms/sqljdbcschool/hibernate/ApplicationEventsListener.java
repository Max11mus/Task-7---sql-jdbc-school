package ua.com.foxminded.lms.sqljdbcschool.hibernate;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;
import ua.com.foxminded.lms.sqljdbcschool.jdbc.SchoolDBInitializer;
import ua.com.foxminded.lms.sqljdbcschool.jdbc.SchoolJdbcDAO;
import ua.com.foxminded.lms.sqljdbcschool.utils.DBConnectionPool;
import ua.com.foxminded.lms.sqljdbcschool.utils.EntitiesGenerator;
import ua.com.foxminded.lms.sqljdbcschool.utils.FileLoader;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public class ApplicationEventsListener implements ApplicationListener<ApplicationContextEvent> {
    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            init((ContextRefreshedEvent) event);
        }
    }

    private void init(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();

        SchoolDAO dao = context.getBean(SchoolDAO.class);

        EntitiesGenerator entitiesGenerator;
        try {
            entitiesGenerator = new EntitiesGenerator();
            List<Group> groups = entitiesGenerator.getRandomGroups();
            List<Student> students = entitiesGenerator.getRandomStudents();
            List<Course> courses = entitiesGenerator.getRandomCourses();
            entitiesGenerator.randomEnrollStudentsToGroups(students, groups);
            ConcurrentHashMap<String, List<Student>> enrolledStudents = entitiesGenerator
                    .randomEnrollStudentsToCourses(students, courses);

            dao.insertGroups(groups);
            dao.insertStudents(students);
            dao.insertCourses(courses);
            enrolledStudents.forEach((courseUuid, studentList) -> studentList.parallelStream()
                    .forEach(student -> dao.addStudentToCourse(student.getUuid(), courseUuid)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

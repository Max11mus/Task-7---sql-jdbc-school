package ua.com.foxminded.lms.sqljdbcschool.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Course;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Group;
import ua.com.foxminded.lms.sqljdbcschool.entitybeans.Student;

public class EntitiesGenerator {
private	DataGenerator dataGenerator;

public EntitiesGenerator() throws IOException {
	super();
	dataGenerator = new DataGenerator();
}

public EntitiesGenerator(DataGenerator dataGenerator) {
	super();
	this.dataGenerator = dataGenerator;
}

public List<Group> getRandomGroups() {
	System.out.println();
	System.out.println("Create 10 groups ");
	ArrayList<String> groupNames = dataGenerator.getGroupNames(10);
	ArrayList<Group> groups = new ArrayList<Group>();
	for (Iterator<String> iterator = groupNames.iterator(); iterator.hasNext();) {
		String groupName = iterator.next();
		Group group = new Group();
		group.setGroupName(groupName);
		groups.add(group);
	}
	return groups;
}

public List<Course> getRandomCourses() throws IOException{
	System.out.println();
	System.out.println("Create 10 courses ");
	ArrayList<String> courseNames = dataGenerator.getCourseNames(10);
	ArrayList<Course> courses = new ArrayList<Course>();
	for (Iterator<String> iterator = courseNames.iterator(); iterator.hasNext();) {
		String courseName = iterator.next();
		Course course = new Course();
		course.setCourseName(courseName);
		course.setCourseDescription(courseName);
		courses.add(course);
	}
	return courses;
}

public List<Student> getRandomStudents() throws IOException{
System.out.println();
System.out.println("Create 200 students");
	ArrayList<String> studentsNames = dataGenerator.getStudentNames(200);
	ArrayList<String> studentSurNames = dataGenerator.getStudentSurNames(200);
	ArrayList<Student> students = new ArrayList<Student>();
	for (int i = 0; i < studentsNames.size(); i++) {
		Student student = new Student();
		student.setFirstName(studentsNames.get(i));
		student.setLastName(studentSurNames.get(i));
		students.add(student);
	}
	return students;
}

public void randomEnrollStudentsToGroups(List<Student> students, List<Group> groups ){
	System.out.println();
	System.out.println("Randomly assign 100 student to groups");
	int randomIndex = 0;
	Random randomGenerator = new Random();
	ArrayList<Student> studentsWithGroups = new ArrayList<Student>();
	randomGenerator.ints(0, students.size()).distinct().limit(100)
			.forEach(i -> studentsWithGroups.add(students.get(i)));
	for (Student student : studentsWithGroups) {
		randomIndex = randomGenerator.ints(0, groups.size()).findFirst().getAsInt();
		student.setGroupUuid(groups.get(randomIndex).getUuid());
	}
}

public ConcurrentHashMap<String, List<Student>> randomEnrollStudentsToCourses(List<Student> students,
		List<Course> courses) {
	System.out.println();
	System.out.println("Randomly assign from 1 to 3 courses for each student");
	int cousresPerStudent = 0;
	Random randomGenerator = new Random();
	ConcurrentHashMap<String, List<Student>> enrolledStudents = new ConcurrentHashMap<String, List<Student>>();
	courses.parallelStream().forEach(course -> enrolledStudents.put(course.getUuid(), new ArrayList<Student>()));
	for (Student student : students) {
		cousresPerStudent = randomGenerator.ints(1, 4).findFirst().getAsInt();
		randomGenerator.ints(0, courses.size()).distinct().limit(cousresPerStudent)
				.forEach(i -> 
				enrolledStudents.get(
						courses.get(i).getUuid())
						.add(student));
	}
	return enrolledStudents;
}

}

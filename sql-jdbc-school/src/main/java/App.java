import java.util.HashSet;

import ua.com.foxminded.lms.sqljdbcschool.utils.DataGenerator;

public class App {

	public static void main(String[] args) {
		DataGenerator dataGenerator = new DataGenerator();
		HashSet<String> groupNames = dataGenerator.getGroupNames(100); 
		groupNames.stream().forEach(s -> System.out.println(s));
		
		HashSet<String> groupNames = dataGenerator.getGroupNames(100);
	}

}

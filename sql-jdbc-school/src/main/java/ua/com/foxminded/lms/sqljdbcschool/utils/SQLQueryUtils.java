package ua.com.foxminded.lms.sqljdbcschool.utils;

public class SQLQueryUtils {
	public String convert(String input) { // from camelCase to SQL like name - example:
											// studentFirstName -> student_first_name
		String[] splited = input.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])");
		String result = String.join("_", splited).toLowerCase();
		return result;
	}
}

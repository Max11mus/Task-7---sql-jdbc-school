package ua.com.foxminded.lms.sqljdbcschool.utils;

public class CheckForNull {
	public static void check(Object... objects) {
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] == null) {
				throw new IllegalArgumentException("ERROR: Null Pointer Arguments.");
			}
		}
	}
}

package ua.com.foxminded.lms.sqljdbcschool.utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class DataGenerator {
	public HashSet<String> getSquadNames(int quantity) {
		HashSet<String> names = new HashSet<String>();
		final String englishUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final String englishLower = englishUpper.toLowerCase();
		final String digits = "0123456789";
		final String englishLetters = englishUpper + englishLower;

		while (names.size() < quantity) {
			String name = Character.toString(getOneRandomFrom(englishLetters))
					+ Character.toString(getOneRandomFrom(englishLetters)) + "-"
					+ Character.toString(getOneRandomFrom(digits)) + Character.toString(getOneRandomFrom(digits));
			names.add(name);
		}
		return names;
	}

	public HashSet<String> getCourseNames(int quantity) throws IOException {
		HashSet<String> names = new HashSet<String>();

		URL namesUrl = ClassLoader.getSystemResource("courses.txt");
		FileLoader fileLoader = new FileLoader();
		final ArrayList<String> courseNamesPool = new ArrayList<String>(
				fileLoader.loadLines(namesUrl).stream().collect(Collectors.toList()));

		while (names.size() < quantity) {
			int randomIndex = (int) Math.floor(Math.random() * courseNamesPool.size());
			names.add(courseNamesPool.get(randomIndex));
		}
		return names;
	}

	public HashSet<String> getStudentNames(int quantity) throws IOException {
		HashSet<String> names = new HashSet<String>();

		URL namesUrl = ClassLoader.getSystemResource("names.txt");
		FileLoader fileLoader = new FileLoader();
		final ArrayList<String> namesPool = new ArrayList<String>(
				fileLoader.loadLines(namesUrl).stream().collect(Collectors.toList()));

		while (names.size() < quantity) {
			int randomIndex = (int) Math.floor(Math.random() * namesPool.size());
			names.add(namesPool.get(randomIndex));
		}
		return names;
	}

	public HashSet<String> getStudentSurNames(int quantity) throws IOException {
		HashSet<String> surNames = new HashSet<String>();

		URL namesUrl = ClassLoader.getSystemResource("surnames.txt");
		FileLoader fileLoader = new FileLoader();
		final ArrayList<String> surNamesPool = new ArrayList<String>(
				fileLoader.loadLines(namesUrl).stream().collect(Collectors.toList()));

		while (surNames.size() < quantity) {
			int randomIndex = (int) Math.floor(Math.random() * surNamesPool.size());
			surNames.add(surNamesPool.get(randomIndex));
		}
		return surNames;
	}

	private char getOneRandomFrom(String input) {
		if (input == null) {
			throw new IllegalArgumentException("ERROR: Null Pointer Argument.");
		}

		if (input.length() == 0) {
			throw new IllegalArgumentException("ERROR: Arguments are EMPTY.");
		}

		if (input.length() == 1) {
			return input.charAt(0);
		}

		int randomIndex = (int) Math.floor(Math.random() * input.length());
		return input.charAt(randomIndex);
	}

}

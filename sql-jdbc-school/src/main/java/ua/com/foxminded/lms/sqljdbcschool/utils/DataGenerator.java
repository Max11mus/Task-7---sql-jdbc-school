package ua.com.foxminded.lms.sqljdbcschool.utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataGenerator {
	private static ArrayList<String> courseNamesPool;
	private static ArrayList<String> namesPool;
	private static ArrayList<String> surnamesPool;
	private Random randomGenerator = new Random();

	private BiFunction<Integer, ArrayList<String>, ArrayList<String>> getRandomNames = 
			(quantity, pool) -> 
			(ArrayList<String>) randomGenerator
					.ints(0, pool.size())
					.limit(quantity)
					.mapToObj(i -> pool.get(i))
					.collect(Collectors.toList());
			
	private BiFunction<Integer, ArrayList<String>, ArrayList<String>> getUniqeRandomNames = 
			(quantity, pool) -> 
			(ArrayList<String>) randomGenerator
					.ints(0, pool.size())
					.distinct()
					.limit(quantity)
					.mapToObj(i -> pool.get(i))
					.collect(Collectors.toList());
			
			
	public DataGenerator() throws IOException {
		courseNamesPool = loadFromPlainTextFile("courses.txt");
		namesPool = loadFromPlainTextFile("names.txt");
		surnamesPool = loadFromPlainTextFile("surnames.txt");
	}

	public ArrayList<String> getCourseNames(int quantity) throws IOException {
		return getUniqeRandomNames.apply(quantity, courseNamesPool);
	}

	public ArrayList<String> getStudentNames(int quantity) throws IOException {
		return getRandomNames.apply(quantity, namesPool);
	}

	public ArrayList<String> getStudentSurNames(int quantity) throws IOException {
		return getRandomNames.apply(quantity, surnamesPool);
	}

	public ArrayList<String> getGroupNames(int quantity) {

		/*
		 * According to the requirement of the Task7 - Groups names must be randomly
		 * generated. The name should contain 2 characters, hyphen, 2 numbers
		 */

		final String englishUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final String englishLower = englishUpper.toLowerCase();
		final String digits = "0123456789";
		final String englishLetters = englishUpper + englishLower;

		Supplier<String> getRandomLetter = () -> Character.toString(
				englishLetters.charAt(randomGenerator.ints(0, englishLetters.length()).findFirst().getAsInt()));

		Supplier<String> getRandomDigit = () -> Character
				.toString(digits.charAt(randomGenerator.ints(0, digits.length()).findFirst().getAsInt()));

		Supplier<String> getRandomGroupName = () -> getRandomLetter.get() + getRandomLetter.get() + "-"
				+ getRandomDigit.get() + getRandomDigit.get();

		return (ArrayList<String>) Stream.generate(getRandomGroupName).distinct().limit(quantity)
				.collect(Collectors.toList());

	}
	
	private ArrayList<String> loadFromPlainTextFile(String fileName) throws IOException {
		URL namesUrl = ClassLoader.getSystemResource(fileName);
		FileLoader fileLoader = new FileLoader();
		return new ArrayList<String>(fileLoader.loadTextLines(namesUrl).stream().collect(Collectors.toList()));
	}

}

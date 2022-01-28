package ua.com.foxminded.lms.sqljdbcschool.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileLoader {
	public List<String> loadLines(URL url) throws IOException {
		List<String> lines;
		lines = null;

		try {
			Path path;
			path = Paths.get(url.toURI());
			lines = Files.readAllLines(path);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public InputStream load(URL url) throws IOException {
		InputStream stream = null;
		try {
			Path path;
			path = Paths.get(url.toURI());
			stream = Files.newInputStream(path, StandardOpenOption.READ);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return stream;
	}

}

package ua.com.foxminded.lms.sqljdbcschool.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileLoader {
	public List<String> loadTextLines(URL url) throws IOException {
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

	public InputStream load(URL url) {

		InputStream stream = null;
		try {
			Path path=null;

			if (url.toString().substring(0,4).equals("jar:")) {
				Map<String, String> env = new HashMap<>();
				String[] pathSplited = url.toString().split("!");
				FileSystem fs = FileSystems.newFileSystem(URI.create(pathSplited[0]), env);
				path = fs.getPath(pathSplited[1]);
			}
			
			if (path == null) {
				path = Paths.get(url.toURI());
			}

			stream = Files.newInputStream(path, StandardOpenOption.READ);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
}

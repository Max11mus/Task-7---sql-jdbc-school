package ua.com.foxminded.lms.sqljdbcschool.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {

	public List<String> loadTextLines(URL url) {
		List<String> lines = new ArrayList<String>();
		URLConnection connection;
		
		try {
			connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public InputStream loadProperties(URL url) {
		InputStream stream = null;
		URLConnection connection;
		
		try {
			connection = url.openConnection();
			stream = connection.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

}

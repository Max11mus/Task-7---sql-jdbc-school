package ua.com.foxminded.lms.sqljdbcschool.app;

import java.io.File;
import java.io.IOException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class SchoolDBApp {
	private static int PORT = 7777;

	public static void main(String[] args) throws LifecycleException {
		String appBase = ".";
		Tomcat tomcat = new Tomcat();
		tomcat.setBaseDir(createTempDir());
		tomcat.setPort(PORT);
		tomcat.getHost().setAppBase(appBase);
		tomcat.addWebapp("", ".");
		
		tomcat.start();
		tomcat.getConnector();
		tomcat.getServer().await();
	}
	
	private static String createTempDir() {
		try {
			File tempDir = File.createTempFile("tomcat.", "." + PORT);
			tempDir.delete();
			tempDir.mkdir();
			tempDir.deleteOnExit();
			return tempDir.getAbsolutePath();
		} catch (IOException ex) {
			throw new RuntimeException(
					"Unable to create tempDir. java.io.tmpdir is set to " + System.getProperty("java.io.tmpdir"), ex);
		}
	}

}
package test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatDtdDataSet;

public class ExportDBSchema {

	public static void main(String[] args) throws SQLException, DatabaseUnitException, FileNotFoundException, IOException {
		Connection jdbcConnection = DriverManager.getConnection("jdbc:h2:~/test",
				"sa", "sa");
	        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
	        
	        // write DTD file
	        FlatDtdDataSet.write(connection.createDataSet(), new FileOutputStream("schema.sql"));

	}

}

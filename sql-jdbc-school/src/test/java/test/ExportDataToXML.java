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
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

public class ExportDataToXML {

	public static void main(String[] args)
			throws ClassNotFoundException, SQLException, DatabaseUnitException, FileNotFoundException, IOException {
		 // database connection
			Connection jdbcConnection = DriverManager
					.getConnection("jdbc:postgresql://maxcloud.sytes.net:5432/school_db", "schooluser", "1199");
			IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

        // full database export
        IDataSet fullDataSet = connection.createDataSet();
        FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));
        
        
	}

}

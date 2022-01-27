import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.com.foxminded.lms.sqljdbcschool.dao.DataBaseInitializer;

class SchoolDataBaseTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://maxcloud.sytes.net:5432/school_db",
				"schooluser", "1199")) {

			DataBaseInitializer schoolDataBase = new DataBaseInitializer(connection);
			schoolDataBase.init();

			connection.close();

		} catch (SQLException e) {
			System.out.println("Connection failure.");
			e.printStackTrace();
		}
	}

	@Test
	void testInit() {
		fail("Not yet implemented");
	}

}

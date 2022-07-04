package ControllersTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ua.com.foxminded.lms.sqljdbcschool.controllers.SpringWebControllersConfig;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;
import ua.com.foxminded.lms.sqljdbcschool.utils.DBConnectionPool;

@Configuration
@Import({ SpringWebControllersConfig.class })
public class TestConfig {
	@MockBean
	DBConnectionPool dBConnectionPool;

	@MockBean
	SchoolDAO schoolDAO;

}

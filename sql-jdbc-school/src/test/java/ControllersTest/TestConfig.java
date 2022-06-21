package ControllersTest;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ua.com.foxminded.lms.sqljdbcschool.controllers.SpringWebControllersConfig;
import ua.com.foxminded.lms.sqljdbcschool.dao.SchoolDAO;

@Configuration
@Import({SpringWebControllersConfig.class})
public class TestConfig {
	@Bean
	public SchoolDAO schoolDAO() {
		return Mockito.mock(SchoolDAO.class);
	}
	
	

}

package ua.com.foxminded.lms.sqljdbcschool.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {

	@GetMapping("/")
	public String homePage() {
	    return "home_tl";
	  }

}

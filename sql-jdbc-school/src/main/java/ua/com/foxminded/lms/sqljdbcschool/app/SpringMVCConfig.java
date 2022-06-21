package ua.com.foxminded.lms.sqljdbcschool.app;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import ua.com.foxminded.lms.sqljdbcschool.controllers.SpringWebControllersConfig;
import ua.com.foxminded.lms.sqljdbcschool.dao.SpringDaoConfig;

@Configuration
@Import({SpringDaoConfig.class, SpringWebControllersConfig.class})
@EnableWebMvc

public class SpringMVCConfig implements WebApplicationInitializer  {

	 @Bean
	    @Description("Thymeleaf template resolver serving HTML 5")
	    public ClassLoaderTemplateResolver templateResolver() {
	        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

	        templateResolver.setPrefix("/webapp/WEB-INF/templates/");
	        templateResolver.setCacheable(false);
	        templateResolver.setSuffix(".html");
	        templateResolver.setTemplateMode("HTML5");
	        templateResolver.setCharacterEncoding("UTF-8");

	        return templateResolver;
	    }

	    @Bean
	    @Description("Thymeleaf template engine with Spring integration")
	    public SpringTemplateEngine templateEngine() {

	        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	        templateEngine.setTemplateResolver(templateResolver());

	        return templateEngine;
	    }

	    @Bean
	    @Description("Thymeleaf view resolver")
	    public ViewResolver viewResolver() {
	        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();

	        viewResolver.setTemplateEngine(templateEngine());
	        viewResolver.setCharacterEncoding("UTF-8");

	        return viewResolver;
	    }

	    @Override
		public void onStartup(ServletContext container) {
			AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
			rootContext.register(SpringMVCConfig.class);

			container.addListener(new ContextLoaderListener(rootContext));

			AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();

			ServletRegistration.Dynamic dispatcher = container
					.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
			dispatcher.setLoadOnStartup(1);
			dispatcher.addMapping("/");
		}
}
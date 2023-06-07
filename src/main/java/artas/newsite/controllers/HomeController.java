package artas.newsite.controllers;

import artas.newsite.config.Config;
import artas.newsite.entities.WeekDay;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Random;

@Controller
public class HomeController implements WebMvcConfigurer {
    private final ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
    private final Log logger = LogFactory.getLog(getClass());
    private final int value = randomNumber();
    private final WeekDay weekDay = context.getBean(WeekDay.class);

    @GetMapping("/home")
    public String helloWorldController(Model model, Authentication authentication) {
        logger.info("Передача.. " + weekDay.getNameWeekDay() + " " + value);
        String username = authentication.getName();
        model.addAttribute("username", username);
        model.addAttribute("day", weekDay.getNameWeekDay());
        model.addAttribute("number", value);
        return "home";
    }

    private int randomNumber() {
        Random random = new Random();
        return random.nextInt(1000);
    }
}

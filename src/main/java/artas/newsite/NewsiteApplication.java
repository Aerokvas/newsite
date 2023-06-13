package artas.newsite;

import artas.newsite.config.Config;
import artas.newsite.entities.*;
import artas.newsite.repositories.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static java.lang.System.out;

//@EnableWebMvc
@SpringBootApplication
public class NewsiteApplication implements CommandLineRunner {
    private final PersonRepository personRepository;

    public NewsiteApplication(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(NewsiteApplication.class, args);
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        WeekDay weekDay = context.getBean(WeekDay.class);
        out.println("Сегодня " + weekDay.getNameWeekDay());
    }

    @Override
    public void run(String... args) {
        try {
            for (PersonEntity person : personRepository.findAll()) {
                System.out.println(person.toString());
            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }
}

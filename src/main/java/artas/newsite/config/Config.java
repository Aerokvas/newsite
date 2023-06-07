package artas.newsite.config;

import artas.newsite.entities.WeekDay;
import artas.newsite.entities.WeekDayInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class Config {
    @Bean
    public WeekDay getDay() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        return switch (dayOfWeek) {
            case MONDAY -> new WeekDayInfo("понедельник",
                    DateTimeFormatter.ofPattern("HH:mm"));
            case TUESDAY -> new WeekDayInfo("вторник",
                    DateTimeFormatter.ofPattern("HH:mm"));
            case WEDNESDAY -> new WeekDayInfo("среда, мои чуваки",
                    DateTimeFormatter.ofPattern("HH:mm"));
            case THURSDAY -> new WeekDayInfo("четверг",
                    DateTimeFormatter.ofPattern("HH:mm"));
            case FRIDAY -> new WeekDayInfo("пятница развратница",
                    DateTimeFormatter.ofPattern("HH:mm"));
            case SATURDAY -> new WeekDayInfo("суббота",
                    DateTimeFormatter.ofPattern("HH:mm"));
            default -> new WeekDayInfo("воскресенье",
                    DateTimeFormatter.ofPattern("HH:mm"));
        };
    }
}


package artas.newsite.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeekDayInfo implements WeekDay {
    private String name;
    private DateTimeFormatter formatter;

    @Override
    public String getNameWeekDay() {
        LocalTime time = LocalTime.now();
        return name + " " + time.format(formatter);
    }
}


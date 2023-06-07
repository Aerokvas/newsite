package artas.newsite;

import artas.newsite.config.Config;
import artas.newsite.entities.*;
import artas.newsite.interactionBank.TransferTask;
import artas.newsite.repositories.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static java.lang.System.out;

//@EnableWebMvc
@SpringBootApplication
public class NewsiteApplication implements CommandLineRunner {
    //private static final BigDecimal amount = new BigDecimal(1000);
    private final PersonRepository user;

    public NewsiteApplication(PersonRepository user) {
        this.user = user;
    }

    public static void main(String[] args) {
        SpringApplication.run(NewsiteApplication.class, args);
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        WeekDay weekDay = context.getBean(WeekDay.class);
/*

        out.println("Передача..\n");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        BankAccountEntity account1 = new BankAccountEntity(1, new BigDecimal(1000));
        BankAccountEntity account2 = new BankAccountEntity(2, new BigDecimal(0));
        BankAccountEntity account3 = new BankAccountEntity(3, new BigDecimal(0));
        BankAccountEntity account4 = new BankAccountEntity(4, new BigDecimal(0));

        out.println("На " + account1.getNameNumber() + " " + account1.getAmount() + ";\n"
                + "На " + account2.getNameNumber() + " " + account2.getAmount() + ";\n"
                + "На " + account3.getNameNumber() + " " + account3.getAmount() + ";\n"
                + "На " + account4.getNameNumber() + " " + account4.getAmount() + ";\n" );

        Semaphore semaphore = new Semaphore(1);
        executor.execute(new TransferTask(account1, account2, amount, semaphore));
        executor.execute(new TransferTask(account2, account3, amount, semaphore));
        executor.execute(new TransferTask(account3, account4, amount, semaphore));

        executor.shutdown();
        //ждем конца
        while (!executor.isTerminated()) {
        }

        out.println("Передача закончена\n");
        out.println("На " + account1.getNameNumber() + " " + account1.getAmount() + ";\n"
                + "На " + account2.getNameNumber() + " " + account2.getAmount() + ";\n"
                + "На " + account3.getNameNumber() + " " + account3.getAmount() + ";\n"
                + "На " + account4.getNameNumber() + " " + account4.getAmount() + ";\n" );


*/
        out.println("Сегодня " + weekDay.getNameWeekDay());

    }
    @Override
    public void run(String... args) {
        try {
            out.println(generateAccountNumber());
            for (PersonEntity person : user.findAll()) {
                System.out.println(person.toString());
            }
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }
    private String generateAccountNumber() {
        int departmentCode = 123;
        int accountCode = 0;

        int maxAccountCode = 13453;

        if (maxAccountCode >= 99999) {
            return "-1";
        }
        accountCode = maxAccountCode + 1;

        String formattedAccountCode = String.format("%05d", accountCode);

        return String.valueOf(departmentCode) + formattedAccountCode;
    }
}

package artas.newsite;

import artas.newsite.config.Config;
import artas.newsite.entities.BankAccountEntity;
import artas.newsite.entities.WeekDay;
import artas.newsite.objects.TransferTask;
import artas.newsite.service.BankAccountService;
import artas.newsite.service.PersonService;
import artas.newsite.service.TransferInformationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static java.lang.System.out;

@SpringBootApplication
public class NewsiteApplication implements CommandLineRunner {

    private final TransferInformationService transferInformationService;
    private final BankAccountService bankAccountService;
    private final PersonService personService;
    private final Log logger = LogFactory.getLog(getClass());

    public NewsiteApplication(TransferInformationService transferInformationService, BankAccountService bankAccountService, PersonService personService) {
        this.transferInformationService = transferInformationService;
        this.bankAccountService = bankAccountService;
        this.personService = personService;
    }

    public static void main(String[] args) {
        SpringApplication.run(NewsiteApplication.class, args);
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        WeekDay weekDay = context.getBean(WeekDay.class);
        out.println("Сегодня " + weekDay.getNameWeekDay());
    }

    @Override
    public void run(String... args) throws Exception {
        //TODO сделать JSON вывод от кого кому и сколько
        //bankAccountService.createNAccounts(1, "artas@mail.ru", BigDecimal.valueOf(5000000));
        List<BankAccountEntity> allAccounts = bankAccountService.getAllAccounts();

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Semaphore semaphore = new Semaphore(1);

        for (int i = 0; i < allAccounts.size() - 1; i++) {
            BankAccountEntity fromAccount = allAccounts.get(i);
            BankAccountEntity toAccount = allAccounts.get(i + 1);
            BigDecimal transferAmount = generateRandomAmount(fromAccount.getAmount());

            Runnable transferTask = new TransferTask(fromAccount, toAccount, transferAmount, transferInformationService, semaphore);

            executorService.execute(transferTask);
        }
        executorService.shutdown();
    }

    private BigDecimal generateRandomAmount(BigDecimal value) {
        Random random = new Random();
        BigDecimal minAmount = BigDecimal.valueOf(100000);

        BigDecimal randomValue = value.subtract(minAmount)
                .multiply(BigDecimal.valueOf(random.nextDouble()))
                .add(minAmount);

        return randomValue.setScale(2, RoundingMode.HALF_UP);
    }

}

package artas.newsite;

import artas.newsite.config.Config;
import artas.newsite.entities.BankAccountEntity;
import artas.newsite.entities.TransferInformationEntity;
import artas.newsite.entities.WeekDay;
import artas.newsite.repositories.BankAccountRepository;
import artas.newsite.repositories.TransferInformationRepository;
import artas.newsite.service.TransferInformationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.lang.System.out;

@SpringBootApplication
public class NewsiteApplication implements CommandLineRunner {

    private final TransferInformationRepository transferInformationRepository;
    private final TransferInformationService transferInformationService;
    private final BankAccountRepository bankAccountRepository;
    private final Log logger = LogFactory.getLog(getClass());

    public NewsiteApplication(TransferInformationRepository transferInformationRepository, TransferInformationService transferInformationService, BankAccountRepository bankAccountRepository) {
        this.transferInformationRepository = transferInformationRepository;
        this.transferInformationService = transferInformationService;
        this.bankAccountRepository = bankAccountRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(NewsiteApplication.class, args);
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        WeekDay weekDay = context.getBean(WeekDay.class);
        out.println("Сегодня " + weekDay.getNameWeekDay());
    }

    @Override
    public void run(String... args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<BankAccountEntity> allAccounts = bankAccountRepository.findAll();

        List<BankAccountEntity> emptyAccounts = allAccounts.stream()
                .filter(account -> account.getAmount().compareTo(BigDecimal.ZERO) == 0)
                .sorted(Comparator.comparingInt(BankAccountEntity::getId))
                .collect(Collectors.toList());

        logger.info("Пустые счета: " + emptyAccounts);

        List<BankAccountEntity> fullAccounts = allAccounts.stream()
                .filter(account -> account.getAmount().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparingInt(BankAccountEntity::getId))
                .collect(Collectors.toList());

        logger.info("Полные счета: " + fullAccounts);

        int size = Math.min(emptyAccounts.size(), fullAccounts.size());
        CountDownLatch latch = new CountDownLatch(size);

        for (int i = 0; i < size; i++) {
            int index = i;

            executorService.execute(() -> {
                logger.info("\033[1;33m Поток " + Thread.currentThread().getName() + "\033[0m начался");
                BankAccountEntity fromAccount = bankAccountRepository.getBankAccountEntityByNameNumber(fullAccounts.get(index).getNameNumber());
                BankAccountEntity toAccount = bankAccountRepository.getBankAccountEntityByNameNumber(emptyAccounts.get(index).getNameNumber());

                BigDecimal minAmount = BigDecimal.ZERO;
                BigDecimal maxAmount = fromAccount.getAmount();

                Random random = new Random();

                BigDecimal amount = minAmount.add(BigDecimal.valueOf(random.nextDouble()).multiply(maxAmount));

                transferInformationService.transferMoney(fromAccount, toAccount, amount);

                logger.info("\033[1;31m Поток № " + Thread.currentThread().getName() + "\033[0m //// Перевод выполнен: от " + fromAccount.getNameNumber()
                        + "; кому " + toAccount.getNameNumber()
                        + "; сумма " + amount);

                bankAccountRepository.save(fromAccount);
                bankAccountRepository.save(toAccount);
                transferInformationRepository.save(new TransferInformationEntity(fromAccount.getNameNumber(), toAccount.getNameNumber(), amount));

                logger.info("\033[1;34m Поток " + Thread.currentThread().getName() + "\033[0m закончился");

                latch.countDown();
            });
        }
        latch.await();
        executorService.shutdown();
    }
}

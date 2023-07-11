package artas.newsite;

import artas.newsite.config.Config;
import artas.newsite.entities.RoleEntity;
import artas.newsite.entities.WeekDay;
import artas.newsite.repositories.RoleRepository;
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

import static java.lang.System.out;

@SpringBootApplication
public class NewsiteApplication implements CommandLineRunner {

    private final TransferInformationService transferInformationService;
    private final BankAccountService bankAccountService;
    private final PersonService personService;
    private final RoleRepository roleRepository;
    private final Log logger = LogFactory.getLog(getClass());

    public NewsiteApplication(TransferInformationService transferInformationService, BankAccountService bankAccountService, PersonService personService, RoleRepository roleRepository) {
        this.transferInformationService = transferInformationService;
        this.bankAccountService = bankAccountService;
        this.personService = personService;
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(NewsiteApplication.class, args);
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        WeekDay weekDay = context.getBean(WeekDay.class);
        out.println("Сегодня " + weekDay.getNameWeekDay());
    }

    private void initializeData() {
        RoleEntity userRole = new RoleEntity();
        userRole.setName("ROLE_USER");

        RoleEntity adminRole = new RoleEntity();
        adminRole.setName("ROLE_ADMIN");

        roleRepository.save(userRole);
        roleRepository.save(adminRole);
    }

    @Override
    public void run(String... args) throws Exception {
        //initializeData();
        //bankAccountService.createNAccounts(5, "artas@mail.ru", BigDecimal.valueOf(5000000));
        /*List<BankAccountEntity> emptyAccounts = bankAccountService.getEmptyAccounts();

        logger.info("Пустые счета: " + emptyAccounts);

        List<BankAccountEntity> fullAccounts = bankAccountService.getNonEmptyAccounts();

        logger.info("Полные счета: " + fullAccounts);

        int size = Math.min(emptyAccounts.size(), fullAccounts.size());

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Semaphore semaphore = new Semaphore(1);
        AtomicBoolean isTransfersMade = new AtomicBoolean(false);

        do {
            isTransfersMade.set(false);

            for (int i = 0; i < size; i++) {
                BankAccountEntity fromAccount = bankAccountService.getBankAccountByNameNumber(fullAccounts.get(i).getNameNumber());
                BankAccountEntity toAccount = bankAccountService.getBankAccountByNameNumber(emptyAccounts.get(i).getNameNumber());
                BigDecimal transferAmount = BigDecimal.valueOf(1000000);

                if (fromAccount.getAmount().compareTo(transferAmount) >= 0) {
                    executorService.execute(() -> {
                        try {
                            semaphore.acquire();
                            logger.info("\033[1;33m Поток " + Thread.currentThread().getName() + "\033[0m начался");

                            transferInformationService.transferMoney(fromAccount, toAccount, transferAmount);

                            logger.info("\033[1;31m Поток № " + Thread.currentThread().getName() + "\033[0m //// Перевод выполнен: от " + fromAccount.getNameNumber()
                                    + "; кому " + toAccount.getNameNumber()
                                    + "; сумма " + transferAmount);

                            bankAccountService.saveBankAccount(fromAccount);
                            bankAccountService.saveBankAccount(toAccount);
                            transferInformationService.saveTransfer(new TransferInformationEntity(fromAccount.getNameNumber(), toAccount.getNameNumber(), transferAmount));

                            logger.info("\033[1;32m Итоговый счет " + toAccount.getNameNumber() + " = " + toAccount.getAmount() + "\033[0m");
                            logger.info("\033[1;34m Поток " + Thread.currentThread().getName() + "\033[0m закончился");

                            isTransfersMade.set(true);
                        } catch (Exception e) {
                            logger.info(e.getMessage());
                        } finally {
                            semaphore.release();
                        }
                    });
                }
            }

            executorService.shutdown();
            executorService.awaitTermination(5000L, TimeUnit.MILLISECONDS);
            executorService = Executors.newFixedThreadPool(5);
        } while (isTransfersMade.get());

        executorService.shutdown();*/
    }
}

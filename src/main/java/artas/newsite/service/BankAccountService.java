package artas.newsite.service;

import artas.newsite.entities.BankAccountEntity;
import artas.newsite.entities.PersonEntity;
import artas.newsite.repositories.BankAccountRepository;
import artas.newsite.repositories.PersonRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.lang.System.out;

@Service
public class BankAccountService {
    private final PersonService personService;
    private final PersonRepository personRepository;
    private final BankAccountRepository accountRepository;
    private final Log logger = LogFactory.getLog(getClass());


    public BankAccountService(PersonRepository personRepository, BankAccountRepository accountRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.accountRepository = accountRepository;
        this.personService = personService;
    }

    public List<BankAccountEntity> getBankAccountsByUsername(String username) {
        Optional<PersonEntity> userFromDB = personRepository.findByUsername(username);
        logger.info("Вывод всех счетов: " + accountRepository.getBankAccountEntitiesByPersonId(userFromDB.get()));
        return accountRepository.getBankAccountEntitiesByPersonId(userFromDB.get());
    }

    public BankAccountEntity getBankAccountByNameNumber(String nameNumber) {
        logger.info("Вывод счета по его имени - " + accountRepository.getBankAccountEntityByNameNumber(nameNumber));
        return accountRepository.getBankAccountEntityByNameNumber(nameNumber);
    }

    public boolean createBankAccount(String username) {
        try {
            Optional<PersonEntity> userFromDB = personRepository.findByUsername(username);

            if (userFromDB.isPresent()) {
                BankAccountEntity bankAccount = new BankAccountEntity();

                if (!generateAccountNumber(username).equals("-1")) {
                    bankAccount.setNameNumber(generateAccountNumber(username));
                    bankAccount.setAmount(BigDecimal.valueOf(1000));
                    bankAccount.setPersonId(userFromDB.get());

                    logger.info("До сохранения - " + bankAccount);

                    accountRepository.save(bankAccount);

                    logger.info("После сохранения - " + bankAccount);

                    personRepository.save(userFromDB.get());
                    return true;
                }
            }
        } catch (Exception e) {
            logger.info("SaveAccount - " + e.getMessage(), e);
        }
        return false;
    }

    public void getProfile(String username, Model model) {
        List<BankAccountEntity> accounts = getBankAccountsByUsername(username);

        if (!accounts.isEmpty())
            model.addAttribute("accounts", accounts);
        else
            model.addAttribute("errorMessage", "У вас нет счета. Для оформления счета нажмите кнопку <<Открыть новый счет>> ниже.");
    }

    private String generateAccountNumber(String username) {
        int departmentCode = 12;
        int userCode = 0;
        int accountCode = 0;
        int maxAccountCode = 0;

        if (personRepository.findByUsername(username).isPresent()) {
            userCode = personRepository.findByUsername(username).get().getId();
            maxAccountCode = accountRepository.getAccountsCountByPersonId(userCode);
        }

        if (maxAccountCode >= 99999) {
            return "-1";
        }
        accountCode = maxAccountCode + 1;

        String formattedUserCode = String.format("%04d", userCode);
        String formattedAccountCode = String.format("%02d", accountCode);

        return String.valueOf(departmentCode) + formattedUserCode + formattedAccountCode;
    }

}


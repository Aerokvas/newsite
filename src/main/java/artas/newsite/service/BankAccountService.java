package artas.newsite.service;

import artas.newsite.entities.BankAccountEntity;
import artas.newsite.entities.PersonEntity;
import artas.newsite.repositories.BankAccountRepository;
import artas.newsite.repositories.PersonRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {
    private final PersonRepository personRepository;
    private final BankAccountRepository accountRepository;
    private final Log logger = LogFactory.getLog(getClass());


    public BankAccountService(PersonRepository personRepository, BankAccountRepository accountRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.accountRepository = accountRepository;
    }

    public List<BankAccountEntity> getBankAccountsByUsername(String username) {
        Optional<PersonEntity> userFromDB = personRepository.findByUsername(username);
        logger.info("Вывод всех счетов: " + accountRepository.getBankAccountEntitiesByPersonId(userFromDB.get()));
        return accountRepository.getBankAccountEntitiesByPersonId(userFromDB.get());
    }

    public void saveBankAccount(BankAccountEntity bankAccountEntity) {
        accountRepository.save(bankAccountEntity);
    }

    public BankAccountEntity getBankAccountByNameNumber(String nameNumber) {
        return accountRepository.getBankAccountEntityByNameNumber(nameNumber);
    }

    public List<BankAccountEntity> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<BankAccountEntity> getEmptyAccounts() {
        return accountRepository.findByAmount(BigDecimal.ZERO, Sort.by("id"));
    }

    public List<BankAccountEntity> getNonEmptyAccounts() {
        return accountRepository.findByAmountGreaterThan(BigDecimal.ZERO, Sort.by("id"));
    }

    public void getProfile(String username, Model model) {
        List<BankAccountEntity> accounts = getBankAccountsByUsername(username);

        if (!accounts.isEmpty())
            model.addAttribute("accounts", accounts);
        else
            model.addAttribute("errorMessage", "У вас нет счета. Для оформления счета нажмите кнопку <<Открыть новый счет>> ниже.");
    }

    @Transactional
    public void createNAccounts(int count, String username, BigDecimal starterAmount) {
        Optional<PersonEntity> user = personRepository.findByUsername(username);

        for (int i = 0; i < count; i++) {
            if (user.isPresent()) {
                BankAccountEntity bankAccount = new BankAccountEntity();

                if (!generateAccountNumber(username).equals("-1")) {
                    saveAccountWithCustomAccountNumber(username, user, bankAccount, starterAmount);
                }
            }
        }
    }


    public boolean createBankAccount(String username) {
        try {
            Optional<PersonEntity> userFromDB = personRepository.findByUsername(username);

            if (userFromDB.isPresent()) {
                BankAccountEntity bankAccount = new BankAccountEntity();

                if (!generateAccountNumber(username).equals("-1")) {
                    saveAccountWithCustomAccountNumber(username, userFromDB, bankAccount, BigDecimal.valueOf(1000));
                    return true;
                }
            }
        } catch (Exception e) {
            logger.info("SaveAccount - " + e.getMessage(), e);
        }
        return false;
    }

    private void saveAccountWithCustomAccountNumber(String username, Optional<PersonEntity> userFromDB, BankAccountEntity bankAccount, BigDecimal starterAmount) {
        bankAccount.setNameNumber(generateAccountNumber(username));
        bankAccount.setAmount(starterAmount);
        bankAccount.setPersonId(userFromDB.get());

        logger.info("До сохранения - " + bankAccount);

        accountRepository.save(bankAccount);

        logger.info("После сохранения - " + bankAccount);

        personRepository.save(userFromDB.get());
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
        String formattedAccountCode = String.format("%04d", accountCode);

        return String.valueOf(departmentCode) + formattedUserCode + formattedAccountCode;
    }

}


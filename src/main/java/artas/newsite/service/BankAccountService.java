package artas.newsite.service;

import artas.newsite.entities.BankAccountEntity;
import artas.newsite.entities.PersonEntity;
import artas.newsite.entities.TransferInformationEntity;
import artas.newsite.repositories.BankAccountRepository;
import artas.newsite.repositories.PersonRepository;
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

    public BankAccountService(PersonRepository personRepository, BankAccountRepository accountRepository, PersonService personService) {
        this.personRepository = personRepository;
        this.accountRepository = accountRepository;
        this.personService = personService;
    }

    public List<BankAccountEntity> getBankAccountsByUsername(String username) {
        Optional<PersonEntity> userFromDB = personRepository.findByUsername(username);
        out.println(accountRepository.getBankAccountEntitiesByPersonId(userFromDB.get()));
        return accountRepository.getBankAccountEntitiesByPersonId(userFromDB.get());
    }

    public BankAccountEntity getBankAccountByNameNumber(String nameNumber) {
        out.println(accountRepository.getBankAccountEntityByNameNumber(nameNumber));
        return accountRepository.getBankAccountEntityByNameNumber(nameNumber);
    }

    public boolean createBankAccount(String username, Model model) {
        try {
            Optional<PersonEntity> userFromDB = personRepository.findByUsername(username);

            if (userFromDB.isPresent()) {
                BankAccountEntity bankAccount = new BankAccountEntity();

                if (!generateAccountNumber(username).equals("-1")) {
                    bankAccount.setNameNumber(generateAccountNumber(username));
                    bankAccount.setAmount(BigDecimal.valueOf(1000));
                    bankAccount.setPersonId(userFromDB.get());

                    userFromDB.get().setMaxAccountCount(userFromDB.get().getMaxAccountCount() + 1);
                    out.println("До сохранения - " + bankAccount);
                    accountRepository.save(bankAccount);
                    out.println("После сохранения - " + bankAccount);
                    personRepository.save(userFromDB.get());
                    return true;
                }
            }
        } catch (Exception e) {
            out.println("SaveAccount - " + e.getMessage());
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
            maxAccountCode = personService.findMaxAccountCountById(userCode);
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


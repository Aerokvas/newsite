package artas.newsite.controllers;

import artas.newsite.entities.BankAccountEntity;
import artas.newsite.entities.TransferInformationEntity;
import artas.newsite.repositories.TransferInformationRepository;
import artas.newsite.service.BankAccountService;
import artas.newsite.service.TransferInformationService;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import static java.lang.System.out;

@Controller
public class AccountProfileController implements WebMvcConfigurer {
    private final BankAccountService bankAccountService;
    private final TransferInformationService transferInformationService;
    private final TransferInformationRepository transferInformationRepository;
    private final Log logger = LogFactory.getLog(getClass());
    private BankAccountEntity fromAccount;
    private BankAccountEntity toAccount;
    private BigDecimal amount;

    public AccountProfileController(BankAccountService bankAccountService, TransferInformationService transferInformationService, TransferInformationRepository transferInformationRepository) {
        this.bankAccountService = bankAccountService;
        this.transferInformationService = transferInformationService;
        this.transferInformationRepository = transferInformationRepository;
    }

    @GetMapping("/profile_account")
    public String userProfile(Principal person, Model model) {
        try {
            List<BankAccountEntity> accounts = bankAccountService.getBankAccountsByUsername(person.getName());

            if (!accounts.isEmpty())
                model.addAttribute("accounts", accounts);
            else
                model.addAttribute("error", "У вас ничего нет");
        } catch (Exception e){
            model.addAttribute("error", "У вас ничего нет");
            out.println(e.getMessage());
        }

        return "profile_account";
    }

    @GetMapping("/profile_account/new_account")
    public String showTransferForm() {
        return "new_account";
    }

    @PostMapping("/profile_account/new_account")
    public String createAccount(Principal principal, Model model) {
        String username = principal.getName();
        boolean isAccountCreated = bankAccountService.createBankAccount(username);

        if (isAccountCreated) {
            return "redirect:/profile_account";
        } else {
            model.addAttribute("errorMessage", "Ошибка при создании аккаунта. Попробуйте снова.");
            return "new_account";
        }
    }

    @GetMapping("/profile_account/transfer")
    public String showTransferForm(Principal person, Model model) {
        logger.info("Имя пользователя " + person.getName());

        List<BankAccountEntity> accounts = bankAccountService.getBankAccountsByUsername(person.getName());
        model.addAttribute("accounts", accounts);
        model.addAttribute("transferInformation", new TransferInformationEntity());

        logger.info("Информация о переводе 1 " + model.getAttribute("transferInformation"));
        return "transfer";
    }

    @PostMapping("/profile_account/transfer")
    public String processTransfer(@ModelAttribute("transferInformation") @Valid TransferInformationEntity transferInformation,
                                  Principal person, BindingResult bindingResult, Model model) {
        logger.info("Начало метода " + transferInformation);
        try {
            fromAccount = bankAccountService.getBankAccountByNameNumber(transferInformation.getFromAccountNumber());
            transferInformation.setBankAccountByFromaccount(fromAccount);

            toAccount = bankAccountService.getBankAccountByNameNumber(transferInformation.getToAccountNumber());
            transferInformation.setBankAccountByToaccount(toAccount);

            amount = transferInformation.getAmount();

            logger.info("После объявления аккаунтов " + transferInformation);

            if (bindingResult.hasErrors()) {
                List<BankAccountEntity> accounts = bankAccountService.getBankAccountsByUsername(person.getName());
                model.addAttribute("accounts", accounts);
                return "transfer";
            }

            if (fromAccount == null || toAccount == null) {
                model.addAttribute("errorMessage", "Неверный ID аккаунта");
                List<BankAccountEntity> accounts = bankAccountService.getBankAccountsByUsername(person.getName());
                model.addAttribute("accounts", accounts);
                return "transfer";
            }

            if (fromAccount.getNameNumber().equals(toAccount.getNameNumber())) {
                model.addAttribute("errorMessage", "Одинаковые аккаунты отправителя и получателя");
                List<BankAccountEntity> accounts = bankAccountService.getBankAccountsByUsername(person.getName());
                model.addAttribute("accounts", accounts);
                return "transfer";
            }

            if (fromAccount.getAmount().compareTo(amount) < 0) {
                model.addAttribute("errorMessage", "Недостаточно средств для перевода");
                List<BankAccountEntity> accounts = bankAccountService.getBankAccountsByUsername(person.getName());
                model.addAttribute("accounts", accounts);
                return "transfer";
            }
        } catch (Exception e) {
            logger.info("Перед попыткой перевода" + e.getMessage());
        }

        try {
            logger.info("Попытка перевода " + transferInformation);
            transferInformationService.transferMoney(fromAccount, toAccount, amount);
            transferInformationRepository.save(transferInformation);
            model.addAttribute("successMessage", "Перевод успешно выполнен");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Произошла ошибка при выполнении перевода: " + e.getMessage());
        }

        List<BankAccountEntity> accounts = bankAccountService.getBankAccountsByUsername(person.getName());
        model.addAttribute("accounts", accounts);
        return "transfer";
    }
}

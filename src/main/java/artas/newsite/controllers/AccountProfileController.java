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

import java.security.Principal;
import java.util.List;

import static java.lang.System.out;

@Controller
public class AccountProfileController implements WebMvcConfigurer {
    private final BankAccountService bankAccountService;
    private final TransferInformationService transferInformationService;
    private final Log logger = LogFactory.getLog(getClass());

    public AccountProfileController(BankAccountService bankAccountService, TransferInformationService transferInformationService, TransferInformationRepository transferInformationRepository) {
        this.bankAccountService = bankAccountService;
        this.transferInformationService = transferInformationService;
    }

    @GetMapping("/profile_account")
    public String userProfile(Principal person, Model model) {
        try {
            bankAccountService.getProfile(person.getName(), model);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "У вас ничего нет");
            out.println(e.getMessage());
        }

        return "profile_account";
    }

    @GetMapping("/profile_account/new_account")
    public String showNewAccountPage() {
        return "new_account";
    }

    @PostMapping("/profile_account/new_account")
    public String createAccount(Principal person, Model model) {
        if (bankAccountService.createBankAccount(person.getName(), model)) {
            return "redirect:/profile_account";
        } else {
            model.addAttribute("errorMessage", "Произошла ошибка при создании счета. Повторите попытку и если проблема не решилась, то обратитесь в тех. поддержку.");
            return "new_account";
        }
    }

    @GetMapping("/profile_account/transfer")
    public String showNewAccountPage(Principal person, Model model) {
        List<BankAccountEntity> accounts = bankAccountService.getBankAccountsByUsername(person.getName());

        model.addAttribute("transferInformation", new TransferInformationEntity());
        model.addAttribute("accounts", accounts);

        return "transfer";
    }

    @PostMapping("/profile_account/transfer")
    public String processTransfer(@ModelAttribute("transferInformation") @Valid TransferInformationEntity transferInformation,
                                  Principal person, Model model) {
        logger.info("Информация о переводе 1 " + transferInformation);
        try {
            transferInformationService.processTransfer(transferInformation, model);
            bankAccountService.getProfile(person.getName(), model);
            model.addAttribute("successMessage", "Перевод успешно выполнен");
        } catch (Exception e) {
            model.addAttribute("errorMessage", model.getAttribute("errorMessage"));
            logger.info("Ошибка при переводе " + e.getMessage());
        }
        return "transfer";
    }
}

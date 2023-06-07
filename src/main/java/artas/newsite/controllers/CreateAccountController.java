package artas.newsite.controllers;

import artas.newsite.entities.BankAccountEntity;
import artas.newsite.entities.PersonEntity;
import artas.newsite.entities.TransferInformationEntity;
import artas.newsite.service.BankAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

import static java.lang.System.out;

@Controller
public class CreateAccountController {
    private final BankAccountService bankAccountService;

    public CreateAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
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
}

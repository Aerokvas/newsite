package artas.newsite.controllers;

import artas.newsite.entities.BankAccountEntity;
import artas.newsite.entities.PersonEntity;
import artas.newsite.service.BankAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.Principal;
import java.util.List;

import static java.lang.System.out;

@Controller
public class AccountProfileController implements WebMvcConfigurer {
    private final BankAccountService bankAccountService;

    public AccountProfileController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
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
}

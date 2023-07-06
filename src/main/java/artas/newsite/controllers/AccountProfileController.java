package artas.newsite.controllers;

import artas.newsite.entities.BankAccountEntity;
import artas.newsite.entities.TransferInformationEntity;
import artas.newsite.service.BankAccountService;
import artas.newsite.service.TransferInformationService;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static java.lang.System.out;

@Controller
public class AccountProfileController {
    private final BankAccountService bankAccountService;
    private final TransferInformationService transferInformationService;
    private final Log logger = LogFactory.getLog(getClass());

    public AccountProfileController(BankAccountService bankAccountService, TransferInformationService transferInformationService) {
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
        if (bankAccountService.createBankAccount(person.getName())) {
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
                                  @RequestParam("fromAccount") String fromAccountNumber,
                                  Principal person, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bankAccountService.getProfile(person.getName(), model);
            return "transfer";
        }

        BankAccountEntity fromAccount = bankAccountService.getBankAccountByNameNumber(fromAccountNumber);
        transferInformation.setFromAccount(fromAccount);

        logger.info("Результат отправки: " + transferInformation);

        if (transferInformationService.processTransfer(transferInformation, model)) {
            bankAccountService.getProfile(person.getName(), model);
            model.addAttribute("successMessage", "Перевод успешно выполнен");
        } else {
            bankAccountService.getProfile(person.getName(), model);
            model.addAttribute("errorMessage", model.getAttribute("errorMessage"));
        }
        return "transfer";
    }

    @GetMapping("profile_account/transfer_history/{nameNumber}")
    public String showTransferHistory(@PathVariable("nameNumber") String nameNumber, Model model) {
        List<TransferInformationEntity> history = transferInformationService.getAllTransactionWhereIsANameNumber(nameNumber);
        model.addAttribute("history", history);

        return "transfer_history";
    }

    @GetMapping("profile_account/all_transfer_history")
    public String showTransferPage(Model model,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransferInformationEntity> transferPage = transferInformationService.getAllPagination(pageable);

        model.addAttribute("transferPage", transferPage);

        return "all_transfer_history";
    }
}
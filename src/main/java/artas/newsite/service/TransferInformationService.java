package artas.newsite.service;

import artas.newsite.entities.BankAccountEntity;
import artas.newsite.entities.TransferInformationEntity;
import artas.newsite.repositories.TransferInformationRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;

import static java.lang.System.out;

@Service
public class TransferInformationService {
    private final BankAccountService bankAccountService;
    private final TransferInformationRepository transferInformationRepository;

    public TransferInformationService(BankAccountService bankAccountService, TransferInformationRepository transferInformationRepository) {
        this.bankAccountService = bankAccountService;
        this.transferInformationRepository = transferInformationRepository;
    }

    public synchronized void transferMoney(BankAccountEntity fromAccount, BankAccountEntity toAccount, BigDecimal amount) {
        try {
            if (fromAccount.getAmount().compareTo(amount) >= 0) {
                fromAccount.takeValue(amount);
                toAccount.putValue(amount);
            }
        } catch (Exception e) {
            out.println("Ошибка в transferMoney " + e.getMessage());
        }
    }

    public void processTransfer(TransferInformationEntity transferInformation, Model model) {
        BankAccountEntity fromAccount = bankAccountService.getBankAccountByNameNumber(transferInformation.getFromAccountNumber());
        transferInformation.setFromAccountNumber(fromAccount.getNameNumber());

        BankAccountEntity toAccount = bankAccountService.getBankAccountByNameNumber(transferInformation.getToAccountNumber());
        out.println("Прием " + toAccount);

        if (toAccount == null)
            model.addAttribute("errorMessage", "Неверный номер получателя.");
        else
            transferInformation.setToAccountNumber(toAccount.getNameNumber());

        BigDecimal amount = transferInformation.getAmount();

        if (fromAccount.getNameNumber().equals(toAccount.getNameNumber())) {
            model.addAttribute("errorMessage", "Одинаковые аккаунты отправителя и получателя");
        } else
            return;


        if (toAccount.getNameNumber().length() != 8) {
            model.addAttribute("errorMessage", "Счет отправителя должен быть равен 8 символам");
        } else
            return;


        if (fromAccount.getAmount().compareTo(amount) < 0) {
            model.addAttribute("errorMessage", "Недостаточно средств для перевода");
            return;
        }

        try {
            transferMoney(fromAccount, toAccount, amount);
            transferInformationRepository.save(transferInformation);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Произошла ошибка при выполнении перевода: " + e.getMessage());
        }
    }
}

package artas.newsite.service;

import artas.newsite.entities.BankAccountEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.lang.System.out;

@Service
public class TransferInformationService {
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
}

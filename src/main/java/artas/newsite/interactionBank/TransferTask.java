package artas.newsite.interactionBank;

import artas.newsite.entities.BankAccountEntity;

import java.math.BigDecimal;
import java.util.concurrent.Semaphore;

import static java.lang.System.out;
import static java.lang.Thread.sleep;

public class TransferTask implements Runnable {
    private BankAccountEntity fromAccount;
    private BankAccountEntity toAccount;
    private BigDecimal amount;
    private Semaphore semaphore;

    public TransferTask(BankAccountEntity fromAccount, BankAccountEntity toAccount, BigDecimal amount, Semaphore semaphore) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        boolean transferCompleted = false;

        try {
            if (!transferCompleted) {
                semaphore.acquire();

                while (!transferCompleted) {
                    if (fromAccount.getAmount().compareTo(amount) >= 0) {
                        fromAccount.takeValue(amount);
                        toAccount.putValue(amount);

                        out.println("\u001B[33mСумма перевода: " + amount + " денег\u001B[0m");
                        out.println("Баланс " + fromAccount.getNameNumber() + " аккаунта: " + fromAccount.getAmount() + " денег");
                        out.println("Баланс " + toAccount.getNameNumber() + " аккаунта: " + toAccount.getAmount() + " денег\n");
                    } else {
                        out.println("Недостаточно средств для перевода в " + amount + " денег. На счете отправителя - " + fromAccount.getAmount() + " денег\n");
                        transferCompleted = true;
                        sleep(200);
                    }
                }
                out.println("\u001B[32mИтоговый баланс " + toAccount.getNameNumber() + ": " + toAccount.getAmount() + " денег\u001B[0m\n");
                semaphore.release();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
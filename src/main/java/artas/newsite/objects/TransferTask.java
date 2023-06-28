package artas.newsite.objects;

import artas.newsite.entities.BankAccountEntity;
import artas.newsite.service.TransferInformationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.concurrent.Semaphore;


public class TransferTask implements Runnable {
    private BankAccountEntity fromAccount;
    private BankAccountEntity toAccount;
    private BigDecimal transferAmount;
    private final TransferInformationService transferInformationService;
    private final Semaphore semaphore;
    private final Log logger = LogFactory.getLog(getClass());

    public TransferTask(BankAccountEntity fromAccount, BankAccountEntity toAccount, BigDecimal transferAmount, TransferInformationService transferInformationService, Semaphore semaphore) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.transferAmount = transferAmount;
        this.transferInformationService = transferInformationService;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            logger.info("\033[1;33m Поток " + Thread.currentThread().getName() + "\033[0m начался");

            transferInformationService.transferMoney(fromAccount, toAccount, transferAmount);

            logger.info("\033[1;31m Поток № " + Thread.currentThread().getName() + "\033[0m //// Перевод выполнен: от "
                    + fromAccount.getNameNumber() + "; кому " + toAccount.getNameNumber() + "; сумма " + transferAmount);

            logger.info("\033[1;32m Итоговый счет " + toAccount.getNameNumber() + " = " + toAccount.getAmount()
                    + "\033[0m");
            logger.info("\033[1;34m Поток " + Thread.currentThread().getName() + "\033[0m закончился");
        } catch (Exception e) {
            logger.info(e.getMessage());
        } finally {
            semaphore.release();
        }
    }
}

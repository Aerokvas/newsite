package artas.newsite.service;

import artas.newsite.entities.BankAccountEntity;
import artas.newsite.entities.TransferInformationEntity;
import artas.newsite.repositories.TransferInformationRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferInformationService {
    private final BankAccountService bankAccountService;
    private final TransferInformationRepository transferInformationRepository;
    private final Log logger = LogFactory.getLog(getClass());


    public TransferInformationService(BankAccountService bankAccountService, TransferInformationRepository transferInformationRepository) {
        this.bankAccountService = bankAccountService;
        this.transferInformationRepository = transferInformationRepository;
    }

    public synchronized void transferMoney(BankAccountEntity fromAccount, BankAccountEntity toAccount, BigDecimal amount) {
        try {
            if (fromAccount.getAmount().compareTo(amount) >= 0) {
                logger.info("Информация перевода: от " + fromAccount
                        + ", кому " + toAccount
                        + ", сумма " + amount);
                fromAccount.takeValue(amount);
                toAccount.putValue(amount);

                bankAccountService.saveBankAccount(fromAccount);
                bankAccountService.saveBankAccount(toAccount);
                saveTransfer(new TransferInformationEntity(
                        fromAccount.getNameNumber(),
                        toAccount.getNameNumber(),
                        amount));
            }
        } catch (Exception e) {
            logger.info("Ошибка в transferMoney " + e.getMessage());
        }
    }

    public boolean processTransfer(TransferInformationEntity transferInformation, Model model) {
        BankAccountEntity fromAccount = bankAccountService.getBankAccountByNameNumber(transferInformation.getFromAccountNumber());
        BankAccountEntity toAccount = bankAccountService.getBankAccountByNameNumber(transferInformation.getToAccountNumber());

        logger.info("Прием 1 - " + fromAccount + "; 2 - " + toAccount);

        if (toAccount != null) {
            BigDecimal amount = transferInformation.getAmount();

            logger.info("Прошел проверку на получателя " + toAccount.getNameNumber());

            if (!(fromAccount.getNameNumber().equals(toAccount.getNameNumber()))) {
                logger.info("Прошел проверку на разные аккаунты " + fromAccount.getNameNumber() + "; " + toAccount.getNameNumber());

                if (!(fromAccount.getAmount().compareTo(amount) < 0)) {
                    logger.info("Прошел проверку на средства " + fromAccount.getAmount());

                    try {
                        logger.info("Перед сохранением - " + transferInformation);
                        transferMoney(fromAccount, toAccount, amount);

                        return true;
                    } catch (Exception e) {
                        logger.info("Произошла ошибка " + e.getMessage());
                        model.addAttribute("errorMessage", "Произошла ошибка при выполнении перевода: " + e.getMessage());
                        return false;
                    }
                } else {
                    model.addAttribute("errorMessage", "Недостаточно средств для перевода");
                    return false;
                }
            } else {
                model.addAttribute("errorMessage", "Одинаковые аккаунты отправителя и получателя");
                return false;
            }
        } else {
            model.addAttribute("errorMessage", "Неверный номер получателя.");
            return false;
        }
    }

    public boolean processTransferREST(TransferInformationEntity transferInformation) {
        BankAccountEntity fromAccount = bankAccountService.getBankAccountByNameNumber(transferInformation.getFromAccountNumber());
        BankAccountEntity toAccount = bankAccountService.getBankAccountByNameNumber(transferInformation.getToAccountNumber());

        logger.info("Прием 1 - " + fromAccount + "; 2 - " + toAccount);

        if (toAccount != null) {
            BigDecimal amount = transferInformation.getAmount();

            logger.info("Прошел проверку на получателя " + toAccount.getNameNumber());

            if (!(fromAccount.getNameNumber().equals(toAccount.getNameNumber()))) {
                logger.info("Прошел проверку на разные аккаунты " + fromAccount.getNameNumber() + "; " + toAccount.getNameNumber());

                if (!(fromAccount.getAmount().compareTo(amount) < 0)) {
                    logger.info("Прошел проверку на средства " + fromAccount.getAmount());

                    try {
                        logger.info("Перед сохранением - " + transferInformation);
                        transferMoney(fromAccount, toAccount, amount);

                        return true;
                    } catch (Exception e) {
                        logger.info("Произошла ошибка " + e.getMessage());
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public List<TransferInformationEntity> getAllTransactionWhereIsANameNumber(String nameNumber) {
        return transferInformationRepository.findByFromAccountNumberOrToAccountNumber(nameNumber, nameNumber);
    }

    public List<TransferInformationEntity> findAllInfo() {
        return transferInformationRepository.findAll();
    }

    public Page<TransferInformationEntity> getAllPagination(Pageable pageable) {
        return transferInformationRepository.findAll(pageable);
    }

    public void saveTransfer(TransferInformationEntity transferInformation) {
        transferInformationRepository.save(transferInformation);
    }
}

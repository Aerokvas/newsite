package artas.newsite.controllers;

import artas.newsite.entities.TransferInformationEntity;
import artas.newsite.service.BankAccountService;
import artas.newsite.service.TransferInformationService;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class TransferRestController {
    private final TransferInformationService transferInformationService;
    private final BankAccountService bankAccountService;
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    public TransferRestController(TransferInformationService transferInformationService, BankAccountService bankAccountService) {
        this.transferInformationService = transferInformationService;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/profile_account/api/transfer")
    public List<TransferInformationEntity> getAllTransfers() {
        return transferInformationService.findAllInfo();
    }

    @PostMapping("/profile_account/api/create_transfer")
    public ResponseEntity<TransferInformationEntity> processTransfer(@RequestBody @Valid TransferInformationEntity transferInformation, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        logger.info("Информация о переводе (post) " + transferInformation);

        if (transferInformationService.processTransferREST(transferInformation)) {
            return ResponseEntity.ok(transferInformation);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}

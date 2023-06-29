package artas.newsite.controllers;

import artas.newsite.entities.TransferInformationEntity;
import artas.newsite.service.TransferInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransferRestController {
    private final TransferInformationService transferInformationService;

    @Autowired
    public TransferRestController(TransferInformationService transferInformationService) {
        this.transferInformationService = transferInformationService;
    }

    @GetMapping("/profile_account/api/transfer")
    public List<TransferInformationEntity> getAllTransfers() {
        return transferInformationService.findAllInfo();
    }
}

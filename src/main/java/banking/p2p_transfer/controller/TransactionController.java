package banking.p2p_transfer.controller;

import banking.p2p_transfer.dto.TransactionDTO;
import banking.p2p_transfer.dto.UserResponseDTO;
import banking.p2p_transfer.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PutMapping("/transactions")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        log.info("Начало перевода");
        Long idTransaction = transactionService.operateTransaction(transactionDTO);

        log.info("Конец перевода, создана транзакция: {}", idTransaction);
        return ResponseEntity.ok(idTransaction);
    }
}

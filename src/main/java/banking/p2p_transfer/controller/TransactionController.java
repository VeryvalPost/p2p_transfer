package banking.p2p_transfer.controller;

import banking.p2p_transfer.dto.TransactionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @PutMapping("/transactions")
    public ResponseEntity<?> createTransaction(@AuthenticationPrincipal Authentication authentication, @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok().build();
    }
}

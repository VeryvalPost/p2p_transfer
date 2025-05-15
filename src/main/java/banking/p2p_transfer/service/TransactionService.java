package banking.p2p_transfer.service;

import banking.p2p_transfer.dto.TransactionDTO;
import org.springframework.security.core.Authentication;

public interface TransactionService {
    Long operateTransaction(TransactionDTO transactionDTO);
}

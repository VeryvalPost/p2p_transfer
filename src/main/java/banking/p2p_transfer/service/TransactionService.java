package banking.p2p_transfer.service;

import banking.p2p_transfer.dto.TransactionRequestDTO;

public interface TransactionService {
    Long operateTransaction(TransactionRequestDTO transactionRequestDTO);
}

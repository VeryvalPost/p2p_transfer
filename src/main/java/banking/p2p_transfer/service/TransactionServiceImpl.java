package banking.p2p_transfer.service;

import banking.p2p_transfer.dto.TransactionDTO;
import banking.p2p_transfer.exception.UserNotFoundException;
import banking.p2p_transfer.model.Account;
import banking.p2p_transfer.model.Transaction;
import banking.p2p_transfer.repository.AccountRepository;
import banking.p2p_transfer.repository.TransactionRepository;
import banking.p2p_transfer.repository.UserRepository;
import banking.p2p_transfer.util.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(JwtUtils jwtUtils, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.jwtUtils = jwtUtils;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Retryable(retryFor = OptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100))
    @Transactional(rollbackOn = Exception.class)
    public Long operateTransaction(Authentication authentication, TransactionDTO transactionDTO) {
        Long fromUserId = jwtUtils.getUserIdFromAuthentication(authentication);
        Long toUserId = transactionDTO.getToUserId();
        BigDecimal amount = transactionDTO.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма транзакции должна быть положительной");
        }


        Account accountFrom = accountRepository.findAccountByUser(fromUserId)
                .orElseThrow(() -> new UserNotFoundException("Аккаунт отправителя не найден для пользователя с ID: " + fromUserId));
        Account accountTo = accountRepository.findAccountByUser(toUserId)
                .orElseThrow(() -> new UserNotFoundException("Аккаунт получателя не найден для пользователя с ID: " + toUserId));

        if (accountFrom.getId().equals(accountTo.getId())) {
            throw new IllegalStateException("Нельзя отправить на свой же аккаунт");
        }

        BigDecimal currentBalanceFrom = accountFrom.getBalance();
        BigDecimal newBalanceFrom = currentBalanceFrom.subtract(amount);

        if (newBalanceFrom.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Недостаточно средств на счете отправителя");
        }

        accountFrom.setBalance(newBalanceFrom);
        accountTo.setBalance(accountTo.getBalance().add(amount));

        try {
            accountRepository.saveAll(List.of(accountFrom, accountTo));
        } catch (OptimisticLockingFailureException ex) {
            log.warn("Конфликт версий при выполнении транзакции. Попытка повторной обработки.");
        }

        Transaction newTransaction = new Transaction();

        newTransaction.setAmount(amount);
        newTransaction.setFromUserId(fromUserId);
        newTransaction.setToUserId(toUserId);
        newTransaction.setDate(LocalDate.now());

        transactionRepository.save(newTransaction);
        log.info("Транзакция выполнена: от пользователя {} к пользователю {}, сумма: {}", fromUserId, toUserId, amount);

        return newTransaction.getId();
    }



}
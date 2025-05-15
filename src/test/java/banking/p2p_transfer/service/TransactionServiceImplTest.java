package banking.p2p_transfer.service;

import banking.p2p_transfer.dto.TransactionDTO;
import banking.p2p_transfer.exception.UserNotFoundException;
import banking.p2p_transfer.model.Account;
import banking.p2p_transfer.model.Transaction;
import banking.p2p_transfer.repository.AccountRepository;
import banking.p2p_transfer.repository.TransactionRepository;
import banking.p2p_transfer.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account accountFrom;
    private Account accountTo;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        // Настройка тестовых данных
        accountFrom = new Account();
        accountFrom.setId(1L);
        accountFrom.setBalance(new BigDecimal("1000.00"));
        accountFrom.setVersion(1L);

        accountTo = new Account();
        accountTo.setId(2L);
        accountTo.setBalance(new BigDecimal("500.00"));
        accountTo.setVersion(1L);

        transactionDTO = new TransactionDTO();
        transactionDTO.setToUserId(2L); // Исправлено: только toUserId, так как fromUserId берется из JWT
        transactionDTO.setAmount(new BigDecimal("100.00"));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(jwtUtils.getUserIdFromAuthentication(authentication)).thenReturn(1L);
    }

    @Test
    void testConcurrentTransfers() throws InterruptedException {
        // Настройка моков
        when(accountRepository.findAccountByUser(1L)).thenAnswer(invocation -> {
            Account copy = new Account();
            copy.setId(accountFrom.getId());
            copy.setBalance(accountFrom.getBalance());
            copy.setVersion(accountFrom.getVersion());
            return Optional.of(copy);
        });
        when(accountRepository.findAccountByUser(2L)).thenAnswer(invocation -> {
            Account copy = new Account();
            copy.setId(accountTo.getId());
            copy.setBalance(accountTo.getBalance());
            copy.setVersion(accountTo.getVersion());
            return Optional.of(copy);
        });

        when(accountRepository.saveAll(any())).thenAnswer(invocation -> {
            List<Account> accounts = invocation.getArgument(0);
            // Эмулируем сохранение в БД - только увеличиваем версию
            accounts.forEach(acc -> acc.setVersion(acc.getVersion() + 1));
            return accounts;
        });

        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            transaction.setId(1L);
            transaction.setTimestamp(LocalDateTime.now());
            return transaction;
        });

        // Запуск двух конкурентных трансферов
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Runnable transfer = () -> transactionService.operateTransaction(transactionDTO);

        executor.submit(transfer);
        executor.submit(transfer);

        executor.shutdown();
        assertThat(executor.awaitTermination(5, TimeUnit.SECONDS)).isTrue();

        // Проверка вызовов
        verify(transactionRepository, times(2)).save(any(Transaction.class));

        // Проверка итоговых сохраненных балансов
        ArgumentCaptor<List<Account>> accountsCaptor = ArgumentCaptor.forClass(List.class);
        verify(accountRepository, times(2)).saveAll(accountsCaptor.capture());

        List<List<Account>> allSavedAccounts = accountsCaptor.getAllValues();
        // Берем последнее сохраненное состояние
        List<Account> lastSaved = allSavedAccounts.get(allSavedAccounts.size() - 1);

        Account savedFrom = lastSaved.stream().filter(a -> a.getId().equals(1L)).findFirst().get();
        Account savedTo = lastSaved.stream().filter(a -> a.getId().equals(2L)).findFirst().get();

        assertThat(savedFrom.getBalance()).isEqualTo(new BigDecimal("800.00"));
        assertThat(savedTo.getBalance()).isEqualTo(new BigDecimal("700.00"));
    }

    @Test
    void testConcurrentTransfersWithConflict() throws InterruptedException {
        // Настройка моков
        when(accountRepository.findAccountByUser(1L)).thenAnswer(invocation -> {
            Account copy = new Account();
            copy.setId(accountFrom.getId());
            copy.setBalance(accountFrom.getBalance());
            copy.setVersion(accountFrom.getVersion());
            return Optional.of(copy);
        });
        when(accountRepository.findAccountByUser(2L)).thenAnswer(invocation -> {
            Account copy = new Account();
            copy.setId(accountTo.getId());
            copy.setBalance(accountTo.getBalance());
            copy.setVersion(accountTo.getVersion());
            return Optional.of(copy);
        });

        // Первый вызов - конфликт, второй и третий - успех (так как 2 потока)
        when(accountRepository.saveAll(any()))
                .thenThrow(new OptimisticLockingFailureException("Конфликт версий"))
                .thenAnswer(invocation -> {
                    List<Account> accounts = invocation.getArgument(0);
                    accounts.forEach(acc -> acc.setVersion(acc.getVersion() + 1));
                    return accounts;
                })
                .thenAnswer(invocation -> {
                    List<Account> accounts = invocation.getArgument(0);
                    accounts.forEach(acc -> acc.setVersion(acc.getVersion() + 1));
                    return accounts;
                });

        // Настройка сохранения транзакций
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            transaction.setId(1L);
            transaction.setTimestamp(LocalDateTime.now());
            return transaction;
        });

        // Запуск двух конкурентных трансферов
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Runnable transfer = () -> {
            try {
                transactionService.operateTransaction(transactionDTO);
            } catch (Exception e) {
                // Логируем исключения, чтобы увидеть, если что-то пошло не так
                System.err.println("Exception in transfer: " + e.getMessage());
            }
        };

        executor.submit(transfer);
        executor.submit(transfer);

        executor.shutdown();
        assertThat(executor.awaitTermination(5, TimeUnit.SECONDS)).isTrue();

        // Проверка вызовов
        verify(transactionRepository, times(2)).save(any(Transaction.class));
        verify(accountRepository, times(3)).saveAll(any()); // 1 неудачный + 2 успешных

        // Проверка итоговых сохраненных балансов
        ArgumentCaptor<List<Account>> accountsCaptor = ArgumentCaptor.forClass(List.class);
        verify(accountRepository, times(3)).saveAll(accountsCaptor.capture());

        // Берем последние два успешных сохранения
        List<Account> lastSaved1 = accountsCaptor.getAllValues().get(1);
        List<Account> lastSaved2 = accountsCaptor.getAllValues().get(2);

        // Проверяем балансы в обоих успешных сохранениях
        Account savedFrom1 = lastSaved1.stream().filter(a -> a.getId().equals(1L)).findFirst().get();
        Account savedTo1 = lastSaved1.stream().filter(a -> a.getId().equals(2L)).findFirst().get();

        Account savedFrom2 = lastSaved2.stream().filter(a -> a.getId().equals(1L)).findFirst().get();
        Account savedTo2 = lastSaved2.stream().filter(a -> a.getId().equals(2L)).findFirst().get();

        assertThat(savedFrom1.getBalance()).isEqualTo(new BigDecimal("900.00"));
        assertThat(savedTo1.getBalance()).isEqualTo(new BigDecimal("600.00"));
        assertThat(savedFrom2.getBalance()).isEqualTo(new BigDecimal("800.00"));
        assertThat(savedTo2.getBalance()).isEqualTo(new BigDecimal("700.00"));
    }
}

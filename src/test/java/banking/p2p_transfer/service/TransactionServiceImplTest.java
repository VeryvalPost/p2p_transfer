package banking.p2p_transfer.service;

import banking.p2p_transfer.dto.TransactionRequestDTO;
import banking.p2p_transfer.model.Account;
import banking.p2p_transfer.model.Transaction;
import banking.p2p_transfer.repository.AccountRepository;
import banking.p2p_transfer.repository.TransactionRepository;
import banking.p2p_transfer.service.implementation.TransactionServiceImpl;
import banking.p2p_transfer.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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

    @BeforeEach
    void setup() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testOperateTransaction_success() {

        Long fromUserId = 1L;
        Long toUserId = 2L;
        BigDecimal amount = new BigDecimal("100.00");

        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setToUserId(toUserId);
        dto.setAmount(amount);

        Account accountFrom = new Account();
        accountFrom.setId(10L);
        accountFrom.setBalance(new BigDecimal("200.00"));

        Account accountTo = new Account();
        accountTo.setId(20L);
        accountTo.setBalance(new BigDecimal("50.00"));

        when(jwtUtils.getUserIdFromAuthentication(authentication)).thenReturn(fromUserId);
        when(accountRepository.findAccountByUser(fromUserId)).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findAccountByUser(toUserId)).thenReturn(Optional.of(accountTo));



        Long transactionId = transactionService.operateTransaction(dto);


        assertNotNull(transactionId);

        assertEquals(new BigDecimal("100.00"), new BigDecimal("200.00").subtract(accountFrom.getBalance()));
        assertEquals(new BigDecimal("100.00"), accountTo.getBalance().subtract(new BigDecimal("50.00")));
        verify(accountRepository, times(1)).saveAll(anyList());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testOperateTransaction_invalidAmount() {

        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setToUserId(2L);
        dto.setAmount(BigDecimal.ZERO);

        when(jwtUtils.getUserIdFromAuthentication(authentication)).thenReturn(1L);


        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.operateTransaction(dto);
        });
        assertEquals("Сумма транзакции должна быть положительной", exception.getMessage());
    }
}
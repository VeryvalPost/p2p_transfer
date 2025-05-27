package banking.p2p_transfer.service.implementation;

import banking.p2p_transfer.repository.AccountRepository;
import banking.p2p_transfer.service.SalaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class SalaryServiceImpl implements SalaryService {
    private final AccountRepository accountRepository;
    private static final BigDecimal MAX_SALARY = new BigDecimal("2.07");
    private static final BigDecimal INCREASE_RATE = new BigDecimal("0.1");

    public SalaryServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void increaseBalance(){
        log.info("Начало начисления процентов по счетам");

        try {
            int updatedCount = accountRepository.updateBalances(
                    INCREASE_RATE,
                    MAX_SALARY
            );

            log.info("Начисление процентов завершено. Обновлено счетов: {}", updatedCount);

        } catch (Exception e) {
            log.error("Ошибка при начислении процентов: {}", e.getMessage());
            throw e;
        }
    }
}

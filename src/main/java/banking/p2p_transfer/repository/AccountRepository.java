package banking.p2p_transfer.repository;

import banking.p2p_transfer.model.Account;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Cacheable(value = "accountMeta", key = "#userId")
    @Query("SELECT a FROM Account a WHERE a.user.id = :userId")
    Optional<Account> findAccountByUser(Long userId);

    @Modifying
    @Query(value = "UPDATE account  SET balance = LEAST(balance + (balance * :increaseRate), initial_balance * :maxSalary)", nativeQuery = true)
    int updateBalances(BigDecimal increaseRate, BigDecimal maxSalary);

}

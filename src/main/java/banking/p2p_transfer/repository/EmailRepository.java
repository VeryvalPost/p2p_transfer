package banking.p2p_transfer.repository;

import banking.p2p_transfer.model.Email;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    @Cacheable(value = "emailExists", key = "#email")
    boolean existsByEmail(String email);

    @Cacheable(value = "userIdByEmail", key = "#email")
    @Query(value = "SELECT users_id FROM email_data  WHERE email = :email", nativeQuery = true)
    Optional<Long> findUserIdByEmail(String email);

    @Cacheable(value = "emailById", key = "#id")
    Optional<Email> findById(Long id);

}

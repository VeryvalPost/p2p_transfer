package banking.p2p_transfer.repository;

import banking.p2p_transfer.model.Phone;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    @Cacheable(value = "phoneExists", key = "#phone")
    boolean existsByPhone(String phone);

    @Cacheable(value = "phoneById", key = "#phoneId")
    @Query(value = "SELECT users_id FROM phone_data WHERE phone = :phone", nativeQuery = true)
    Optional<Long> findUserIdByPhone(String phone);
}

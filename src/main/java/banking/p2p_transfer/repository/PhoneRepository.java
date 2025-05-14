package banking.p2p_transfer.repository;

import banking.p2p_transfer.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    boolean existsByPhone(String phone);

    @Query(value = "SELECT users_id FROM phone_data WHERE phone = :phone", nativeQuery = true)
    Optional<Long> findUserIdByPhone(String phone);
}

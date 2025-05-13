package banking.p2p_transfer.repository;

import banking.p2p_transfer.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    boolean existsByPhone(String phone);
}

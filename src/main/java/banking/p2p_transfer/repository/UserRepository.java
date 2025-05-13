package banking.p2p_transfer.repository;

import banking.p2p_transfer.model.Email;
import banking.p2p_transfer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String username);
    Optional<User> findById(Long id);
    List<Email> findEmailsById(Long id);
}

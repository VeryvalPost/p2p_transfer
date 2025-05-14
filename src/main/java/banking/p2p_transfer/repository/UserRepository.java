package banking.p2p_transfer.repository;

import banking.p2p_transfer.model.User;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String username);
    Optional<User> findById(Long id);

    List<User> findByNameContains(@Size(max = 500) String name, Pageable pageable);

    List<User> findByDateOfBirthAfter(LocalDate dateOfBirthAfter, Pageable pageable);
}

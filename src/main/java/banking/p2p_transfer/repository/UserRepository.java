package banking.p2p_transfer.repository;

import banking.p2p_transfer.model.User;
import jakarta.validation.constraints.Size;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Cacheable(value = "userByName", key = "#username")
    Optional<User> findByName(String username);
    @Cacheable(value = "userById", key = "#id")
    Optional<User> findById(Long id);

    @Cacheable(value = "usersByNameContains", key = "#name + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    List<User> findByNameContains(@Size(max = 500) String name, Pageable pageable);
    @Cacheable(value = "usersByDateOfBirthAfter", key = "#dateOfBirthAfter.toString() + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    List<User> findByDateOfBirthAfter(LocalDate dateOfBirthAfter, Pageable pageable);
}

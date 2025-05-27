package banking.p2p_transfer.service.implementation;

import banking.p2p_transfer.dto.SearchRequestDTO;
import banking.p2p_transfer.dto.UserResponseDTO;
import banking.p2p_transfer.exception.UserNotFoundException;
import banking.p2p_transfer.model.User;
import banking.p2p_transfer.repository.EmailRepository;
import banking.p2p_transfer.repository.PhoneRepository;
import banking.p2p_transfer.repository.UserRepository;
import banking.p2p_transfer.service.UserSearchService;
import banking.p2p_transfer.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSearchServiceImpl implements UserSearchService {

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchUser(SearchRequestDTO searchRequest) {
        log.debug("Поиск пользователей с параметрами: {}", searchRequest);

        try {
            return searchByPriority(searchRequest)
                    .stream()
                    .map(userMapper::toDto)
                    .toList();
        } catch (Exception e) {
            log.error("Ошибка при поиске пользователей: {}", e.getMessage());
            throw e;
        }
    }

    private List<User> searchByPriority(SearchRequestDTO searchRequest) {
        PageRequest pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());

        // Поиск по точным параметрам
        if (searchRequest.getPhone() != null) {
            return searchByPhone(searchRequest.getPhone());
        }
        if (searchRequest.getEmail() != null) {
            return searchByEmail(searchRequest.getEmail());
        }

        // Поиск по нечётким параметрам
        if (searchRequest.getName() != null) {
            return userRepository.findByNameContains(searchRequest.getName(), pageable);
        }
        if (searchRequest.getDateOfBirth() != null) {
            return userRepository.findByDateOfBirthAfter(searchRequest.getDateOfBirth(), pageable);
        }

        return Collections.emptyList();
    }

    private List<User> searchByPhone(String phone) {
        return findUserByIdentifier(
                () -> phoneRepository.findUserIdByPhone(phone),
                "Пользователь с номером телефона %s не найден".formatted(phone)
        );
    }

    private List<User> searchByEmail(String email) {
        return findUserByIdentifier(
                () -> emailRepository.findUserIdByEmail(email),
                "Пользователь с email %s не найден".formatted(email)
        );
    }

    private List<User> findUserByIdentifier(Supplier<Optional<Long>> idSupplier, String errorMessage) {
        Long userId = idSupplier.get()
                .orElseThrow(() -> new UserNotFoundException(errorMessage));

        User user = userRepository.findByIdWithPhonesAndEmails(userId)
                .orElseThrow(() -> new UserNotFoundException(errorMessage));

        return Collections.singletonList(user);
    }
}
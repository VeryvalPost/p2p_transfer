package banking.p2p_transfer.service;

import banking.p2p_transfer.dto.SearchRequestDTO;
import banking.p2p_transfer.dto.UserResponseDTO;
import banking.p2p_transfer.exception.UserNotFoundException;
import banking.p2p_transfer.model.User;
import banking.p2p_transfer.repository.EmailRepository;
import banking.p2p_transfer.repository.PhoneRepository;
import banking.p2p_transfer.repository.UserRepository;
import banking.p2p_transfer.util.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserSearchServiceImpl implements UserSearchService {

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;
    private final UserMapper userMapper;

    public UserSearchServiceImpl(UserRepository userRepository, EmailRepository emailRepository, PhoneRepository phoneRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
        this.phoneRepository = phoneRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserResponseDTO> searchUser(SearchRequestDTO searchRequest) {
        log.info("Начало поиска пользователей: page={}, size={}, phone={}, email={}, name={}, dateOfBirth={}",
                searchRequest.getPage(),
                searchRequest.getSize(),
                searchRequest.getPhone(),
                searchRequest.getEmail(),
                searchRequest.getName(),
                searchRequest.getDateOfBirth());
        try {
            PageRequest pageable = PageRequest.of(
                    searchRequest.getPage(),
                    searchRequest.getSize());

            if (searchRequest.getPhone() != null) {
                Long userId = phoneRepository.findUserIdByPhone(searchRequest.getPhone())
                        .orElseThrow(() -> {
                            log.error("Пользователь с номером телефона {} не найден", searchRequest.getPhone());
                            return new UserNotFoundException("Пользователь с таким номером телефона не найден");
                        });
                User user = userRepository.findByIdWithPhonesAndEmails(userId)
                        .orElseThrow(() -> {
                            log.error("Пользователь с номером телефона {} не найден", searchRequest.getPhone());
                            return new UserNotFoundException("Пользователь с таким номером телефона не найден");
                        });
                UserResponseDTO userResponseDTO = userMapper.toDto(user);
                log.info("Найден пользователь по телефону");
                return List.of(userResponseDTO);
            } else if (searchRequest.getEmail() != null) {
                Long userId = emailRepository.findUserIdByEmail(searchRequest.getEmail())
                        .orElseThrow(() -> {
                            log.error("Пользователь с email {} не найден", searchRequest.getEmail());
                            return new UserNotFoundException("Пользователь с таким email не найден");
                        });
                User user = userRepository.findByIdWithPhonesAndEmails(userId)
                        .orElseThrow(() -> {
                            log.error("Пользователь с email {} не найден", searchRequest.getEmail());
                            return new UserNotFoundException("Пользователь с таким email не найден");
                        });
                UserResponseDTO userResponseDTO = userMapper.toDto(user);
                log.info("Найден пользователь по email");
                return List.of(userResponseDTO);
            } else if (searchRequest.getName() != null) {
                List<User> list = userRepository.findByNameContains(searchRequest.getName(), pageable);
                List<UserResponseDTO> userResponseDTOList = list.stream().map(userMapper::toDto).toList();
                log.info("Найдено {} пользователей по имени", userResponseDTOList.size());
                return userResponseDTOList;
            } else if (searchRequest.getDateOfBirth() != null) {
                List<User> list = userRepository.findByDateOfBirthAfter(searchRequest.getDateOfBirth(), pageable);
                list.forEach(user -> {
                    user.getPhones().size();
                    user.getEmails().size();
                });
                List<UserResponseDTO> userResponseDTOList = list.stream()
                        .map(userMapper::toDto)
                        .toList();
                log.info("Найдено {} пользователей по дате рождения", userResponseDTOList.size());
                return userResponseDTOList;
            }
            log.info("По запросу ничего не найдено");
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("Ошибка при поиске пользователей: {}", e.getMessage());
            throw e;
        } finally {
            log.info("Завершение поиска пользователей");
        }
    }
}
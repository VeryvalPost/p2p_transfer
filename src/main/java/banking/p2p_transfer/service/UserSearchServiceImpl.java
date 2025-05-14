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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        log.info("Поиск пользователей: page={}, size={}, phone={}, email={}, name={}",
                searchRequest.getPage(),
                searchRequest.getSize(),
                searchRequest.getPhone(),
                searchRequest.getEmail(),
                searchRequest.getName());

        PageRequest pageable = PageRequest.of(
                searchRequest.getPage(),
                searchRequest.getSize());

        if (searchRequest.getPhone() != null) {
            Long userId = phoneRepository.findUserIdByPhone(searchRequest.getPhone())
                    .orElseThrow(() -> new UserNotFoundException("Пользователь с таким номером телефона не найден"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("Пользователь с таким номером телефона не найден"));
            UserResponseDTO userResponseDTO = userMapper.toDto(user);
            return List.of(userResponseDTO);
        } else if (searchRequest.getEmail() != null) {
            Long userId = emailRepository.findUserIdByEmail(searchRequest.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("Пользователь с таким email не найден"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("Пользователь с таким email не найден"));
            UserResponseDTO userResponseDTO = userMapper.toDto(user);
            return List.of(userResponseDTO);
        } else if (searchRequest.getName() != null) {
            List<User> list = userRepository.findByNameContains(searchRequest.getName(), pageable);
            List<UserResponseDTO> userResponseDTOList = list.stream().map(userMapper::toDto).toList();
            return userResponseDTOList;
        }  else if (searchRequest.getDateOfBirth() != null) {
            List<User> list = userRepository.findByDateOfBirthAfter(searchRequest.getDateOfBirth(), pageable);
            List<UserResponseDTO> userResponseDTOList = list.stream().map(userMapper::toDto).toList();
            return userResponseDTOList;
        }

        return new ArrayList<>();
    }
}
package banking.p2p_transfer.service;


import banking.p2p_transfer.dto.SearchRequestDTO;
import banking.p2p_transfer.exception.UserNotFoundException;
import banking.p2p_transfer.model.User;
import banking.p2p_transfer.repository.EmailRepository;
import banking.p2p_transfer.repository.PhoneRepository;
import banking.p2p_transfer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserSearchServiceImpl implements UserSearchService {

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;

    public UserSearchServiceImpl(UserRepository userRepository, EmailRepository emailRepository, PhoneRepository phoneRepository) {
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
        this.phoneRepository = phoneRepository;
    }

    @Override
    public List<User> searchUser(SearchRequestDTO searchRequest) {

        if (searchRequest.getPhone() != null) {
            Long userId = phoneRepository.findUserIdByPhone(searchRequest.getPhone()).orElseThrow(()->
                    new UserNotFoundException("Пользователь с таким номером телефона не найден"));
            User user = userRepository.findById(userId).orElseThrow(() ->
                    new UserNotFoundException("Пользователь с таким номером телефона не найден"));
            return List.of(user);

        } else if (searchRequest.getEmail() != null) {
            Long userId = emailRepository.findUserIdByEmail(searchRequest.getEmail()).orElseThrow(()->
                    new UserNotFoundException("Пользователь с таким номером телефона не найден"));
            User user = userRepository.findById(userId).orElseThrow(() ->
                    new UserNotFoundException("Пользователь с таким номером телефона не найден"));
            return List.of(user);

        } else if (searchRequest.getName() != null) {
            List<User> list = userRepository.findByNameContaining(searchRequest.getName());
            return list;
        }

        return new ArrayList<>();
    }
}

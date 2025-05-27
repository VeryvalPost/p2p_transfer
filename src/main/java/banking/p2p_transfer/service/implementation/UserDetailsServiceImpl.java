package banking.p2p_transfer.service.implementation;

import banking.p2p_transfer.model.User;
import banking.p2p_transfer.repository.UserRepository;
import lombok.RequiredArgsConstructor;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Не смог найти пользователя: " + username));

        return UserDetailsImpl.build(user);
    }


}
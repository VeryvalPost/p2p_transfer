package banking.p2p_transfer.service.implementation;

import banking.p2p_transfer.dto.JwtRequestDTO;
import banking.p2p_transfer.dto.JwtResponseDTO;
import banking.p2p_transfer.exception.UserNotFoundException;
import banking.p2p_transfer.repository.EmailRepository;
import banking.p2p_transfer.repository.UserRepository;
import banking.p2p_transfer.service.AuthService;
import banking.p2p_transfer.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;

    @Override
    public JwtResponseDTO authenticate(JwtRequestDTO request) {
        log.info("Попытка аутентификации пользователя с email: {}", request.getEmail());

        Long userId = emailRepository.findUserIdByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("Пользователь с email {} не найден", request.getEmail());
                    return new UserNotFoundException("Пользователь с таким email не найден");
                });

        String userName = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Пользователь с ID {} не найден", userId);
                    return new UserNotFoundException("Пользователь не найден");
                })
                .getName();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            log.info("Пользователь {} успешно аутентифицирован", userName);

            return new JwtResponseDTO(jwt, userDetails.getId(), roles);
        } catch (Exception e) {
            log.error("Ошибка при аутентификации пользователя {}: {}", userName, e.getMessage());
            throw e;
        }
    }
}
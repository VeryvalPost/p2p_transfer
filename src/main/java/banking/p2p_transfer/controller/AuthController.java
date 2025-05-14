package banking.p2p_transfer.controller;

import banking.p2p_transfer.dto.JwtRequest;
import banking.p2p_transfer.dto.JwtResponse;
import banking.p2p_transfer.exception.UserNotFoundException;
import banking.p2p_transfer.repository.EmailRepository;
import banking.p2p_transfer.repository.UserRepository;
import banking.p2p_transfer.service.UserDetailsImpl;
import banking.p2p_transfer.util.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository,  EmailRepository emailRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
    }

    //Аутентификацию предусмотрел по основному email и паролю.
    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(@RequestBody JwtRequest jwtRequest) {
        Long userId = emailRepository.findUserIdByEmail(jwtRequest.getEmail()).orElseThrow(()-> new UserNotFoundException("Пользователь с таким email не найден"));
        String userName = userRepository.findById(userId).get().getName();
        System.out.println("User email from request: " + userName);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, jwtRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);


        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                roles));
    }
}
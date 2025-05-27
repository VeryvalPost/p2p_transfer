package banking.p2p_transfer.controller;

import banking.p2p_transfer.dto.JwtRequestDTO;
import banking.p2p_transfer.dto.JwtResponseDTO;
import banking.p2p_transfer.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "API для аутентификации пользователей")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Аутентификация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/auth/login")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@RequestBody JwtRequestDTO request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
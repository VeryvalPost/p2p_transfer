package banking.p2p_transfer.controller;

import banking.p2p_transfer.dto.EmailDTO;
import banking.p2p_transfer.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users/{userId}/emails")
@Tag(name = "Email", description = "API для управления email пользователя")
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "Добавление email пользователя", description = "Добавляет новый email адрес в профиль пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Email успешно создан",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Email уже существует"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<Long> createEmail(
            @Parameter(description = "ID пользователя", required = true) @PathVariable long userId,
            @Valid @RequestBody EmailDTO emailDTO) {
        log.info("Запрос на создание email для пользователя с ID: {}", userId);
        Long emailId = emailService.createEmailForUser(userId, emailDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(emailId);
    }

    @Operation(summary = "Обновление email пользователя", description = "Обновляет существующий email адрес пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email успешно обновлен",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь или email не найден"),
            @ApiResponse(responseCode = "409", description = "Email уже существует"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{emailId}")
    public ResponseEntity<String> updateEmail(
            @Parameter(description = "ID пользователя", required = true) @PathVariable long userId,
            @Parameter(description = "ID email", required = true) @PathVariable Long emailId,
            @Valid @RequestBody EmailDTO emailDTO) {
        log.info("Запрос на обновление email с ID: {} для пользователя с ID: {}", emailId, userId);
        String updatedEmail = emailService.updateEmailForUser(userId, emailId, emailDTO);
        return ResponseEntity.ok(updatedEmail);
    }

    @Operation(summary = "Удаление email пользователя", description = "Удаляет email адрес из профиля пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Email успешно удален"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Пользователь или email не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{emailId}")
    public ResponseEntity<Void> deleteEmail(
            @Parameter(description = "ID пользователя", required = true) @PathVariable long userId,
            @Parameter(description = "ID email", required = true) @PathVariable Long emailId) {
        log.info("Запрос на удаление email с ID: {} для пользователя с ID: {}", emailId, userId);
        emailService.deleteEmailForUser(userId, emailId);
        return ResponseEntity.noContent().build();
    }
}
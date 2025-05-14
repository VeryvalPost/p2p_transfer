package banking.p2p_transfer.controller;

import banking.p2p_transfer.dto.EmailDTO;
import banking.p2p_transfer.dto.PhoneDTO;
import banking.p2p_transfer.dto.SearchRequestDTO;
import banking.p2p_transfer.dto.UserResponseDTO;
import banking.p2p_transfer.service.EmailService;
import banking.p2p_transfer.service.PhoneService;
import banking.p2p_transfer.service.UserSearchService;
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

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Контактная информация пользователя", description = "API для управления контактами пользователей")
public class ModifyUserController {

    private final EmailService emailService;
    private final PhoneService phoneService;
    private final UserSearchService userService;

    @Operation(summary = "Добавление email пользователя", description = "Добавляет новый email адрес в профиль пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Email успешно создан",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Email уже существует"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/{userId}/emails")
    public ResponseEntity<Long> createEmail(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long userId,
            @Valid @RequestBody EmailDTO emailDTO) {
        log.info("Запрос на создание email для пользователя с ID: {}", userId);
        Long emailId = emailService.createEmailForUser(userId, emailDTO);
        log.info("Email успешно создан с ID: {} для пользователя с ID: {}", emailId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(emailId);
    }

    @Operation(summary = "Добавление телефона пользователя", description = "Добавляет новый номер телефона в профиль пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Телефон успешно создан",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Телефон уже существует"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/{userId}/phones")
    public ResponseEntity<Long> createPhone(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long userId,
            @Valid @RequestBody PhoneDTO phoneDTO) {
        log.info("Запрос на создание телефона для пользователя с ID: {}", userId);
        Long phoneId = phoneService.createPhoneForUser(userId, phoneDTO);
        log.info("Телефон успешно создан с ID: {} для пользователя с ID: {}", phoneId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(phoneId);
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
    @PutMapping("/{userId}/emails/{emailId}")
    public ResponseEntity<String> updateEmail(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long userId,
            @Parameter(description = "ID email", required = true) @PathVariable Long emailId,
            @Valid @RequestBody EmailDTO emailDTO) {
        log.info("Запрос на обновление email с ID: {} для пользователя с ID: {}", emailId, userId);
        String updatedEmail = emailService.updateEmailForUser(userId, emailId, emailDTO);
        log.info("Email с ID: {} успешно обновлен для пользователя с ID: {}", emailId, userId);
        return ResponseEntity.ok(updatedEmail);
    }

    @Operation(summary = "Обновление телефона пользователя", description = "Обновляет существующий номер телефона пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Телефон успешно обновлен",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь или телефон не найден"),
            @ApiResponse(responseCode = "409", description = "Телефон уже существует"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{userId}/phones/{phoneId}")
    public ResponseEntity<String> updatePhone(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long userId,
            @Parameter(description = "ID телефона", required = true) @PathVariable Long phoneId,
            @Valid @RequestBody PhoneDTO phoneDTO) {
        log.info("Запрос на обновление телефона с ID: {} для пользователя с ID: {}", phoneId, userId);
        String updatedPhone = phoneService.updatePhoneForUser(userId, phoneId, phoneDTO);
        log.info("Телефон с ID: {} успешно обновлен для пользователя с ID: {}", phoneId, userId);
        return ResponseEntity.ok(updatedPhone);
    }

    @Operation(summary = "Удаление email пользователя", description = "Удаляет email адрес из профиля пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Email успешно удален"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Пользователь или email не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{userId}/emails/{emailId}")
    public ResponseEntity<Void> deleteEmail(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long userId,
            @Parameter(description = "ID email", required = true) @PathVariable Long emailId) {
        log.info("Запрос на удаление email с ID: {} для пользователя с ID: {}", emailId, userId);
        emailService.deleteEmailForUser(userId, emailId);
        log.info("Email с ID: {} успешно удален для пользователя с ID: {}", emailId, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удаление телефона пользователя", description = "Удаляет номер телефона из профиля пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Телефон успешно удален"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Пользователь или телефон не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{userId}/phones/{phoneId}")
    public ResponseEntity<Void> deletePhone(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long userId,
            @Parameter(description = "ID телефона", required = true) @PathVariable Long phoneId) {
        log.info("Запрос на удаление телефона с ID: {} для пользователя с ID: {}", phoneId, userId);
        phoneService.deletePhoneForUser(userId, phoneId);
        log.info("Телефон с ID: {} успешно удален для пользователя с ID: {}", phoneId, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Поиск пользователей", description = "Поиск пользователей с фильтрацией по различным полям")
    @ApiResponse(responseCode = "200", description = "Успешный поиск",
            content = @Content(schema = @Schema(implementation = List.class)))
    @PostMapping("/findUser")
    public ResponseEntity<?> findUser(
            @Parameter(description = "Запрос на поиск пользователя", required = true)
            @RequestBody SearchRequestDTO searchRequestDTO) {
        log.info("Запрос на поиск пользователя");
        List<UserResponseDTO> findUser = userService.searchUser(searchRequestDTO);

        log.info("Пользователи с подходящими под фильтр данными: {}", findUser);
        return ResponseEntity.ok(findUser);
    }
}
package banking.p2p_transfer.controller;

import banking.p2p_transfer.dto.PhoneDTO;
import banking.p2p_transfer.service.PhoneService;
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
@RequestMapping(value = "/api/v1/users/{userId}/phones")
@Tag(name = "Телефон", description = "API для управления телефоном пользователя")
public class PhoneController {

    private final PhoneService phoneService;

    @Operation(summary = "Добавление телефона пользователя", description = "Добавляет новый номер телефона в профиль пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Телефон успешно создан",
                    content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "409", description = "Телефон уже существует"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<Long> createPhone(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long userId,
            @Valid @RequestBody PhoneDTO phoneDTO) {
        log.info("Запрос на создание телефона для пользователя с ID: {}", userId);
        Long phoneId = phoneService.createPhoneForUser(userId, phoneDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(phoneId);
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
    @PutMapping("/{phoneId}")
    public ResponseEntity<String> updatePhone(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long userId,
            @Parameter(description = "ID телефона", required = true) @PathVariable Long phoneId,
            @Valid @RequestBody PhoneDTO phoneDTO) {
        log.info("Запрос на обновление телефона с ID: {} для пользователя с ID: {}", phoneId, userId);
        String updatedPhone = phoneService.updatePhoneForUser(userId, phoneId, phoneDTO);
        return ResponseEntity.ok(updatedPhone);
    }

    @Operation(summary = "Удаление телефона пользователя", description = "Удаляет номер телефона из профиля пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Телефон успешно удален"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос"),
            @ApiResponse(responseCode = "404", description = "Пользователь или телефон не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{phoneId}")
    public ResponseEntity<Void> deletePhone(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long userId,
            @Parameter(description = "ID телефона", required = true) @PathVariable Long phoneId) {
        log.info("Запрос на удаление телефона с ID: {} для пользователя с ID: {}", phoneId, userId);
        phoneService.deletePhoneForUser(userId, phoneId);
        return ResponseEntity.noContent().build();
    }
}
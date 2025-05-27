package banking.p2p_transfer.controller;

import banking.p2p_transfer.dto.TransactionRequestDTO;
import banking.p2p_transfer.dto.TransactionResponseDTO;
import banking.p2p_transfer.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
@Tag(name = "Транзакции", description = "API для управления денежными переводами")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Создание транзакции", description = "Создает новую транзакцию между пользователями")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Транзакция успешно создана"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "422", description = "Недостаточно средств"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<Long> createTransaction(@Valid @RequestBody TransactionRequestDTO request) {
        log.info("Получен запрос на создание транзакции: {}", request);
        Long response = transactionService.operateTransaction(request);
        log.info("Транзакция успешно создана: {}", response);
        return ResponseEntity.ok(response);
    }
}
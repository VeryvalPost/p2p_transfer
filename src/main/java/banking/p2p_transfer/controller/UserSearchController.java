package banking.p2p_transfer.controller;

import banking.p2p_transfer.dto.SearchRequestDTO;
import banking.p2p_transfer.dto.UserResponseDTO;
import banking.p2p_transfer.service.UserSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users")
@Tag(name = "Поиск пользователей", description = "API для поиска пользователей")
public class UserSearchController {

    private final UserSearchService userSearchService;

    @Operation(summary = "Поиск пользователей", description = "Поиск пользователей с фильтрацией по различным полям")
    @ApiResponse(responseCode = "200", description = "Успешный поиск",
            content = @Content(schema = @Schema(implementation = List.class)))
    @PostMapping("/findUser")
    public ResponseEntity<List<UserResponseDTO>> findUser(
            @Parameter(description = "Запрос на поиск пользователя", required = true)
            @RequestBody SearchRequestDTO searchRequestDTO) {
        log.info("Запрос на поиск пользователя");
        List<UserResponseDTO> users = userSearchService.searchUser(searchRequestDTO);
        return ResponseEntity.ok(users);
    }
}
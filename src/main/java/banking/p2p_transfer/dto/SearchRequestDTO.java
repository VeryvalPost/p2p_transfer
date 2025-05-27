package banking.p2p_transfer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class SearchRequestDTO{
    @Schema(description = "Дата рождения (ищутся пользователи с датой рождения после указанной)", example = "1990-01-01")
    private LocalDate dateOfBirth;

    @Schema(description = "Телефон (точное совпадение)", example = "71234567890")
    private String phone;

    @Schema(description = "Имя (поиск по началу имени)", example = "Пользователь")
    private String name;

    @Schema(description = "Email (точное совпадение)", example = "email@email.ru")
    private String email;

    @Schema(description = "Номер страницы (начиная с 0)", example = "0")
    private Integer page = 0;

    @Schema(description = "Размер страницы", example = "10")
    private Integer size = 10;
}

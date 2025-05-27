package banking.p2p_transfer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailDTO  {
    private Long id;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Неверный формат email")
    private String email;
}
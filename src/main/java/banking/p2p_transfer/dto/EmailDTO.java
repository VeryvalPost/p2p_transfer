package banking.p2p_transfer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailDTO {
    private Long id;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
}
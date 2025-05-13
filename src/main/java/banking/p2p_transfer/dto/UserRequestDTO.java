package banking.p2p_transfer.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserRequestDTO {
    private String name;
    private LocalDate dateOfBirth;
    private String password;
    private BigDecimal balance;
    private List<String> phones;
    private List<String> emails;
}
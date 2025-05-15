package banking.p2p_transfer.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {

    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private LocalDateTime timestamp;
    private BigDecimal amount;

}

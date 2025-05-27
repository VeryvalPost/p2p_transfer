package banking.p2p_transfer.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionRequestDTO {

    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private LocalDateTime timestamp;
    private BigDecimal amount;

}

package banking.p2p_transfer.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponseDTO {
    private Long id;
    private BigDecimal amount;
    private String status;
    private LocalDateTime timestamp;
    private Long fromUserId;
    private Long toUserId;
}
package banking.p2p_transfer.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionDTO {

    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private LocalDate timestamp;
    private BigDecimal amount;

}

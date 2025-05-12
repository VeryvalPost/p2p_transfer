package banking.p2p_transfer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {
    private Long id;
    private BigDecimal balance;
}
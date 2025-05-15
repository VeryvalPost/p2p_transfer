package banking.p2p_transfer.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AccountDTO implements Serializable {
    private Long id;
    private BigDecimal balance;
}
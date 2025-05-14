package banking.p2p_transfer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {

    @Id
    private Long id;
    @Column
    private Long fromUserId;
    @Column
    private Long toUserId;
    @Column
    private BigDecimal amount;
    @Column
    private LocalDate date;
}

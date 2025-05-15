package banking.p2p_transfer.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long fromUserId;
    @Column
    private Long toUserId;
    @Column
    private BigDecimal amount;
    @Column
    private LocalDate timestamp;
}

package banking.p2p_transfer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Не может быть null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Баланс не может быть меньше 0")
    private BigDecimal balance;

    @OneToOne
    @JoinColumn(name = "users_id", nullable = false, unique = true)
    private User user;
}
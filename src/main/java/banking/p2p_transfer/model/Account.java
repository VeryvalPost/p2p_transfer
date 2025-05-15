package banking.p2p_transfer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "account")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 2)
    @NotNull(message = "Не может быть null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Баланс не может быть меньше 0")
    private BigDecimal balance;

    @OneToOne
    @JoinColumn(name = "users_id", nullable = false, unique = true)
    @JsonBackReference
    private User user;

    @Column(name = "initial_balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal initialBalance;

    @Version
    private Long version;
}
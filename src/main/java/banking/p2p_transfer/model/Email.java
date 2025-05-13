package banking.p2p_transfer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "email_data")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;
}

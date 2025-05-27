package banking.p2p_transfer.dto;

import lombok.Getter;

@Getter
public class JwtRequestDTO {
    private final String email;
    private final String password;

    public JwtRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

}

package banking.p2p_transfer.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class JwtRequest {
    private final String email;
    private final String password;

    public JwtRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}

package banking.p2p_transfer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class JwtResponseDTO {
    @Getter
    private final List<String> roles;
    private String token;
    private String type = "Bearer";
    @Setter
    @Getter
    private Long id;


    public JwtResponseDTO(String accessToken, Long id, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

}

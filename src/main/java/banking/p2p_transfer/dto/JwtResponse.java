package banking.p2p_transfer.dto;

import java.util.List;

public class JwtResponse {
    private final List<String> roles;
    private String token;
    private String type = "Bearer";
    private Long id;


    public JwtResponse(String accessToken, Long id, List<String> roles) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getRoles() {
        return roles;
    }
}

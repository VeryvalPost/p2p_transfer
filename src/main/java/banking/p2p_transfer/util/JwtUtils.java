package banking.p2p_transfer.util;

import banking.p2p_transfer.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${security.token}")
    private String jwtSecret;

    @Value("${security.expiration}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        log.debug("Generating JWT for user ID: {}", userPrincipal.getId());

        try {
            String token = Jwts.builder()
                    .claim("USER_ID", userPrincipal.getId())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .signWith(key(), SignatureAlgorithm.HS256)
                    .compact();
            log.debug("Generated JWT: {}", token);
            return token;
        } catch (Exception e) {
            log.error("Error generating JWT: {}", e.getMessage(), e);
            throw e;
        }
    }

    private SecretKey key() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.error("Error decoding JWT secret: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Long getUserIdFromJwtToken(String token) {
        log.debug("Parsing JWT: {}", token);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Long userId = claims.get("USER_ID", Long.class);
            log.debug("Extracted user ID: {}", userId);
            return userId;
        } catch (Exception e) {
            log.error("Error parsing JWT: {}", e.getMessage(), e);
            throw e;
        }
    }

    public boolean validateJwtToken(String authToken) {
        log.debug("Validating JWT: {}", authToken);
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(authToken);
            log.debug("JWT is valid");
            return true;
        } catch (JwtException e) {
            log.error("JWT validation error: {}", e.getMessage(), e);
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT token is empty or null: {}", e.getMessage(), e);
            return false;
        }
    }
}
package banking.p2p_transfer.util;

import banking.p2p_transfer.service.implementation.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test

    void testGenerateAndValidateToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "testuser", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String token = jwtUtils.generateJwtToken(auth);
        assertNotNull(token);


        boolean isValid = jwtUtils.validateJwtToken(token);
        assertTrue(isValid, "Токен должен быть действительным");

        Long userId = jwtUtils.getUserIdFromJwtToken(token);
        assertEquals(1L, userId, "User ID должен совпадать с ID пользователя в токене");
    }
}
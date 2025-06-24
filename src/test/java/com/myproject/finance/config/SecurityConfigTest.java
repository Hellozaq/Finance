package com.myproject.finance.config;

import com.myproject.finance.security.JwtFilter;
import com.myproject.finance.security.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SecurityConfigTest {

    @Test
    void testPasswordEncoderBean() {
        JwtFilter mockFilter = mock(JwtFilter.class);
        CustomUserDetailsService mockUserService = mock(CustomUserDetailsService.class);

        SecurityConfig config = new SecurityConfig(mockFilter, mockUserService);

        PasswordEncoder encoder = config.passwordEncoder();

        assertNotNull(encoder);
        assertTrue(encoder.matches("password123", encoder.encode("password123")));
    }
}

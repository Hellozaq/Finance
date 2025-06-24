package com.myproject.finance.security;

import com.myproject.finance.model.User;
import com.myproject.finance.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        UserRepository repo = mock(UserRepository.class);
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("pass");

        when(repo.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        CustomUserDetailsService service = new CustomUserDetailsService(repo);

        assertEquals("test@example.com", service.loadUserByUsername("test@example.com").getUsername());
    }

    @Test
    void loadUserByUsername_shouldThrowIfNotFound() {
        UserRepository repo = mock(UserRepository.class);
        when(repo.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        CustomUserDetailsService service = new CustomUserDetailsService(repo);
        assertThrows(RuntimeException.class, () -> service.loadUserByUsername("notfound@example.com"));
    }
}
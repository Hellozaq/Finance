package com.myproject.finance.service;

import com.myproject.finance.model.User;
import com.myproject.finance.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser_success() {
        User user = new User("test", "test@example.com", "password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User saved = userService.saveUser(user);

        assertEquals(user.getEmail(), saved.getEmail());
    }

    @Test
    void testSaveUser_duplicateEmail_throwsException() {
        User user = new User("test", "test@example.com", "password");

        // Simulate that user with email already exists
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(new User()));

        // Now we expect saveUser to throw RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.saveUser(user));

        assertEquals("Email already in use", exception.getMessage());
    }

}
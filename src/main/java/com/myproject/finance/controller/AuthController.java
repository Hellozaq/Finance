package com.myproject.finance.controller;

import com.myproject.finance.dto.ApiResponse;
import com.myproject.finance.model.User;
import com.myproject.finance.security.JwtUtil;
import com.myproject.finance.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import com.myproject.finance.dto.RegisterRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User saved = userService.saveUser(user);
        return ResponseEntity.ok(new ApiResponse<>(200, "User registered successfully", saved));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(new ApiResponse<>(200, "Login successful", token));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body(new ApiResponse<>(401, "Invalid credentials", null));
        }
    }
    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handle(Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("⚠️ Exception: " + ex.getMessage());
        }
    }
}
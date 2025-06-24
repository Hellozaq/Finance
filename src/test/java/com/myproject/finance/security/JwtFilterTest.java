package com.myproject.finance.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtFilterTest {

    private JwtFilter jwtFilter;
    private JwtUtil jwtUtil;
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        userDetailsService = mock(CustomUserDetailsService.class);
        jwtFilter = new JwtFilter(jwtUtil, userDetailsService);
    }

    @Test
    void testDoFilterInternal_withValidToken_setsAuthentication() throws ServletException, IOException {
        String email = "user@example.com";
        String token = "Bearer valid.jwt.token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", token);
        request.setRequestURI("/test");

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        when(jwtUtil.extractUsername("valid.jwt.token")).thenReturn(email);
        when(jwtUtil.validateToken("valid.jwt.token")).thenReturn(true);

        var userDetails = mock(org.springframework.security.core.userdetails.UserDetails.class);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
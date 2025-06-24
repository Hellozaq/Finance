package com.myproject.finance.controller;

import com.myproject.finance.dto.ApiResponse;
import com.myproject.finance.exception.GlobalExceptionHandlerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DummyExceptionControllerTest {

    private GlobalExceptionHandlerTest handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandlerTest();
    }

    @Test
    void testRuntimeExceptionEndpoint() {
        RuntimeException ex = new RuntimeException("Dummy runtime error");
        ResponseEntity<ApiResponse<Object>> response = handler.handleRuntime(ex);

        assertEquals(500, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Dummy runtime error", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testValidationExceptionEndpoint() {
        // Mock BindingResult
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("dto", "username", "must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // Create MethodArgumentNotValidException with mocked BindingResult
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ApiResponse<Object>> response = handler.handleValidation(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("must not be blank", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
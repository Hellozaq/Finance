package com.myproject.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.finance.dto.TransactionDTO;
import com.myproject.finance.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        // Register a new user with a unique email
        String email = "testuser" + System.currentTimeMillis() + "@example.com";
        String registerRequest = objectMapper.writeValueAsString(Map.of(
                "username", "testuser",
                "email", email,
                "password", "password"
        ));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andExpect(status().isOk());

        // Login to retrieve JWT token
        String loginRequest = objectMapper.writeValueAsString(Map.of(
                "email", email,
                "password", "password"
        ));

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Extract JWT token from JSON response
        jwtToken = objectMapper.readTree(response).get("data").asText();
    }

    @Test
    void testAddTransaction() throws Exception {
        TransactionDTO dto = new TransactionDTO(
                1L,
                200.0,
                "Salary",
                "Income",
                LocalDate.now()
        );

        mockMvc.perform(post("/transactions")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transaction added successfully"));
    }

    @Test
    void testGetAllTransactions() throws Exception {
        mockMvc.perform(get("/transactions")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testCalculateBalance() throws Exception {
        mockMvc.perform(get("/transactions/balance")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Balance calculated successfully"));
    }
    @Test
    void testUpdateTransaction() throws Exception {
        // First add a transaction
        TransactionDTO addDto = new TransactionDTO(
                1L,
                200.0,
                "Groceries",
                "Expense",
                LocalDate.now()
        );

        String addResponse = mockMvc.perform(post("/transactions")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long transactionId = objectMapper.readTree(addResponse).get("data").get("id").asLong();

        // Now update the transaction
        TransactionDTO updateDto = new TransactionDTO(
                transactionId,
                150.0,
                "Updated Groceries",
                "Expense",
                LocalDate.now()
        );

        mockMvc.perform(put("/transactions")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transaction updated successfully"));
    }

    @Test
    void testDeleteTransaction() throws Exception {
        // First add a transaction
        TransactionDTO dto = new TransactionDTO(
                1L,
                300.0,
                "Travel",
                "Expense",
                LocalDate.now()
        );

        String response = mockMvc.perform(post("/transactions")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long transactionId = objectMapper.readTree(response).get("data").get("id").asLong();

        // Now delete the transaction by sending ID in JSON body
        String deletePayload = objectMapper.writeValueAsString(Map.of("id", transactionId));

        mockMvc.perform(delete("/transactions")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deletePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transaction deleted successfully"));
    }
}

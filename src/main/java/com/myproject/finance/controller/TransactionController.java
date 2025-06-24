package com.myproject.finance.controller;

import com.myproject.finance.dto.ApiResponse;
import com.myproject.finance.dto.DeleteRequestDTO;
import com.myproject.finance.dto.TransactionDTO;
import com.myproject.finance.dto.TransactionResponseDTO;
import com.myproject.finance.model.Transaction;
import com.myproject.finance.model.User;
import com.myproject.finance.repository.UserRepository;
import com.myproject.finance.service.TransactionService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    public TransactionController(TransactionService transactionService, UserRepository userRepository) {
        this.transactionService = transactionService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid TransactionDTO dto, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "User not found", null));
        }

        Transaction tx = transactionService.createTransaction(dto, user);
        if (tx != null && tx.getId() != null) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Transaction added successfully", tx));
        } else {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Failed to add transaction", null));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "User not found", null));
        }

        List<TransactionResponseDTO> transactions = transactionService.getAllTransactions(user);
        if (transactions.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(200, "No transactions found", transactions));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(200, "Transactions fetched successfully", transactions));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody @Valid TransactionDTO dto, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "User not found", null));
        }

        try {
            Transaction updated = transactionService.updateTransaction(dto.id(), dto, user);
            return ResponseEntity.ok(new ApiResponse<>(200, "Transaction updated successfully", updated));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(400).body(new ApiResponse<>(400, ex.getMessage(), null));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody @Valid DeleteRequestDTO request, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "User not found", null));
        }

        try {
            transactionService.deleteTransaction(request.id(), user);
            return ResponseEntity.ok(new ApiResponse<>(200, "Transaction deleted successfully", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(400).body(new ApiResponse<>(400, ex.getMessage(), null));
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "User not found", null));
        }

        double balance = transactionService.getBalance(user);
        return ResponseEntity.ok(new ApiResponse<>(200, "Balance calculated successfully", balance));
    }
}

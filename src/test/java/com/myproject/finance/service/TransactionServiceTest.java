package com.myproject.finance.service;

import com.myproject.finance.dto.TransactionDTO;
import com.myproject.finance.model.Transaction;
import com.myproject.finance.model.User;
import com.myproject.finance.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class   TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testCreateTransaction_success() {
        User user = new User();
        TransactionDTO dto = new TransactionDTO(null, 100.0, "Grocery", "Expense", LocalDate.now());
        Transaction saved = new Transaction(100.0, "Grocery", "Expense", LocalDate.now(), user);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(saved);

        Transaction result = transactionService.createTransaction(dto, user);

        assertEquals(100.0, result.getAmount());
        assertEquals("Grocery", result.getDescription());
    }

    @Test
    void testGetBalance_mixedTransactions() {
        User user = new User();
        List<Transaction> transactions = Arrays.asList(
            new Transaction(100.0, "Salary", "Income", LocalDate.now(), user),
            new Transaction(40.0, "Food", "Expense", LocalDate.now(), user)
        );

        when(transactionRepository.findByUser(user)).thenReturn(transactions);

        double balance = transactionService.getBalance(user);
        assertEquals(60.0, balance);
    }

    @Test
    void testUpdateTransaction_success() {
        User user = new User();
        user.setId(1L);
        Transaction existing = new Transaction(100.0, "Old", "Expense", LocalDate.now(), user);
        existing.setId(10L);

        TransactionDTO dto = new TransactionDTO(null, 200.0, "Updated", "Income", LocalDate.now());

        when(transactionRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        Transaction updated = transactionService.updateTransaction(10L, dto, user);

        assertEquals(200.0, updated.getAmount());
        assertEquals("Updated", updated.getDescription());
    }

    @Test
    void testUpdateTransaction_unauthorized() {
        User owner = new User(); owner.setId(1L);
        User other = new User(); other.setId(2L);

        Transaction existing = new Transaction(100.0, "Old", "Expense", LocalDate.now(), owner);
        existing.setId(11L);

        TransactionDTO dto = new TransactionDTO(null, 200.0, "Updated", "Income", LocalDate.now());

        when(transactionRepository.findById(11L)).thenReturn(Optional.of(existing));

        Exception ex = assertThrows(RuntimeException.class, () -> {
            transactionService.updateTransaction(11L, dto, other);
        });

        assertEquals("Unauthorized", ex.getMessage());
    }

    @Test
    void testDeleteTransaction_notFound() {
        User user = new User();
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () -> {
            transactionService.deleteTransaction(99L, user);
        });

        assertEquals("Transaction not found", ex.getMessage());
    }
}
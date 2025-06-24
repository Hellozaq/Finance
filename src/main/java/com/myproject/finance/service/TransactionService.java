package com.myproject.finance.service;

import com.myproject.finance.dto.TransactionDTO;
import com.myproject.finance.dto.TransactionResponseDTO;
import com.myproject.finance.model.Transaction;
import com.myproject.finance.model.User;
import com.myproject.finance.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(TransactionDTO dto, User user) {
        Transaction transaction = new Transaction(
            dto.amount(), dto.description(), dto.category(), dto.date(), user);
        return transactionRepository.save(transaction);
    }

    public List<TransactionResponseDTO> getAllTransactions(User user) {
        return transactionRepository.findByUser(user).stream()
                .map(tx -> new TransactionResponseDTO(
                        tx.getId(),
                        tx.getAmount(),
                        tx.getDescription(),
                        tx.getCategory(),
                        tx.getDate(),
                        tx.getUser().getUsername()
                ))
                .toList();
    }

    public Transaction updateTransaction(Long id, TransactionDTO dto, User user) {
        Transaction tx = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (!tx.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        tx.setAmount(dto.amount());
        tx.setDescription(dto.description());
        tx.setCategory(dto.category());
        tx.setDate(dto.date());
        return transactionRepository.save(tx);
    }

    public void deleteTransaction(Long id, User user) {
        Transaction tx = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (!tx.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        transactionRepository.delete(tx);
    }

    public double getBalance(User user) {
        return transactionRepository.findByUser(user).stream()
            .mapToDouble(tx -> "income".equalsIgnoreCase(tx.getCategory()) ? tx.getAmount() : -tx.getAmount())
            .sum();
    }
}
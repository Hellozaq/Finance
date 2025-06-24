package com.myproject.finance.repository;

import com.myproject.finance.model.Transaction;
import com.myproject.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
}
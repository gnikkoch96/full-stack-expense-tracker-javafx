package com.example.expense_tracker_server.repository;

import com.example.expense_tracker_server.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByUserId(int userId);
}

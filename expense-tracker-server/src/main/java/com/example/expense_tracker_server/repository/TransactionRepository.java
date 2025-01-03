package com.example.expense_tracker_server.repository;

import com.example.expense_tracker_server.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByUserIdOrderByTransactionDateDesc(int userId);
    List<Transaction> findAllByUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(
            int userId,
            LocalDate startDate,
            LocalDate endDate
    );
}

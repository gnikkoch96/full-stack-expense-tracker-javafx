package com.example.expense_tracker_server.repository;

import com.example.expense_tracker_server.entity.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionCategoryRepository extends JpaRepository<TransactionCategory, Integer> {
        List<TransactionCategory> findAllByUserId(int userId);
}

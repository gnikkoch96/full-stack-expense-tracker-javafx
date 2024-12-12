package com.example.expense_tracker_server.controller;


import com.example.expense_tracker_server.entity.Transaction;
import com.example.expense_tracker_server.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private static final Logger logger = Logger.getLogger(TransactionController.class.getName());

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction){
        logger.info("Create Transaction: \n" +
                transaction.getTransactionCategory().getId() + "\n" +
                transaction.getUser().getId() + "\n" +
                transaction.getTransactionName() + "\n" +
                transaction.getTransactionAmount() + "\n" +
                transaction.getTransactionDate() + "\n" +
                transaction.getTransactionType()
        );


        transactionService.createTransaction(
                transaction.getTransactionCategory().getId(),
                transaction.getUser().getId(),
                transaction.getTransactionName(),
                transaction.getTransactionAmount(),
                transaction.getTransactionDate(),
                transaction.getTransactionType()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}














package com.example.expense_tracker_server.controller;


import com.example.expense_tracker_server.entity.Transaction;
import com.example.expense_tracker_server.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable int id){
        logger.info("Getting Transaction with Id: " + id);

        Optional<Transaction> transaction = transactionService.getTransactionById(id);

        if(transaction.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(transaction.get());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByUserId(@PathVariable int userId){
        logger.info("Getting All Transaction with User Id: " + userId);

        List<Transaction> transactionList = transactionService.getAllTransactionsByUserId(userId);

        if(transactionList.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.status(HttpStatus.OK).body(transactionList);
    }
}














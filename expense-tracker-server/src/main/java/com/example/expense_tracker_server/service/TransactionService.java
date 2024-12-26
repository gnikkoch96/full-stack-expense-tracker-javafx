package com.example.expense_tracker_server.service;

import com.example.expense_tracker_server.entity.Transaction;
import com.example.expense_tracker_server.entity.TransactionCategory;
import com.example.expense_tracker_server.entity.User;
import com.example.expense_tracker_server.repository.TransactionCategoryRepository;
import com.example.expense_tracker_server.repository.TransactionRepository;
import com.example.expense_tracker_server.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class TransactionService {
    private static final Logger logger = Logger.getLogger(TransactionService.class.getName());
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    // createit a
    public Transaction createTransaction(Transaction transaction){
        logger.info("Creating Transaction");

        // find category (optional)
        TransactionCategory category = null;
        if(transaction.getTransactionCategory() != null){
            category = transactionCategoryRepository.findById(transaction.getTransactionCategory().getId())
                    .orElse(null);
        }

        // find user
        User user = userRepository.findById(transaction.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("User Not Found")
        );

        // create new transaction
        Transaction newTransaction = new Transaction();
        newTransaction.setUser(user);
        newTransaction.setTransactionName(transaction.getTransactionName());
        newTransaction.setTransactionAmount(transaction.getTransactionAmount());
        newTransaction.setTransactionDate(transaction.getTransactionDate());
        newTransaction.setTransactionType(transaction.getTransactionType());
        newTransaction.setTransactionCategory(category);

        return transactionRepository.save(newTransaction);
    }

    // Note: it returns the list in descending order by default
    public List<Transaction> getAllTransactionsByUserId(int userId){
        logger.info("Getting all Transaction for User: " + userId);
        return transactionRepository.findAllByUserIdOrderByTransactionDateDesc(userId);
    }

    public Optional<Transaction> getTransactionById(int id){
        logger.info("Getting all Transaction for User: " + id);
        return transactionRepository.findById(id);
    }
}














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

import java.math.BigDecimal;
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

    // create
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

    // read
    // Note: it returns the list in descending order by default
    // todo might change this to recent transactions where it will only get the top 10 recent transactions
    public List<Transaction> getAllTransactionsByUserId(int userId){
        logger.info("Getting all Transaction for User: " + userId);
        return transactionRepository.findAllByUserIdOrderByTransactionDateDesc(userId);
    }

    public List<Transaction> getAllTransactionsByUserIdAndYear(int userId, int year){
        logger.info("Getting all Transaction for User: " + userId );

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return transactionRepository.findAllByUserIdAndTransactionDateBetweenOrderByTransactionDateDesc(
                userId,
                startDate,
                endDate
        );
    }

    public Optional<Transaction> getTransactionById(int id){
        logger.info("Getting all Transaction for User: " + id);
        return transactionRepository.findById(id);
    }

    // update
    public Transaction updateTransactionById(Transaction transaction){
        // can't update transaction if it doesn't exist
        if(transaction == null) return null;

        logger.info("Updating Transaction with ID: " + transaction.getId());

        Transaction updatedTransaction = transaction;
        updatedTransaction.setTransactionName(transaction.getTransactionName());
        updatedTransaction.setTransactionAmount(transaction.getTransactionAmount());
        updatedTransaction.setTransactionDate(transaction.getTransactionDate());
        updatedTransaction.setTransactionType(transaction.getTransactionType());

        // attempt to find transaction category if it doesn't exist just give it a null transaction category
        Optional<TransactionCategory> transactionCategory = transactionCategoryRepository.findById(transaction.getTransactionCategory().getId());
        updatedTransaction.setTransactionCategory(transactionCategory.orElse(null));

        return transactionRepository.save(updatedTransaction);
    }

    // delete
    public void deleteTransactionById(int transactionId){
        logger.info("Deleting Transaction with ID: " + transactionId);

        Optional<Transaction> transaction = transactionRepository.findById(transactionId);

        if(transaction.isEmpty()) return;

        transactionRepository.delete(transaction.get());
    }
}














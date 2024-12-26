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

    // create
    public Transaction createTransaction(int categoryId, int userId, String transactionName,
                                         double transactionAmount, LocalDate transactionDate, String transactionType){
        logger.info("Creating Transaction");

        // find category
        Optional<TransactionCategory> transactionCategoryOptional = transactionCategoryRepository.findById(categoryId);

        // find user
        Optional<User> userOptional = userRepository.findById(userId);

        logger.info("User is null");

        // Note: we don't do this with the transactino category because it can be empty
        if(userOptional.isEmpty()) return null;

        logger.info("User is not null");

        Transaction transaction = new Transaction();

        if(transactionCategoryOptional.isEmpty()){
            transaction.setTransactionCategory(null);
        }else{
            transaction.setTransactionCategory(transactionCategoryOptional.get());
        }

        logger.info(userOptional.get().getName());
        transaction.setUser(userOptional.get());
        transaction.setTransactionName(transactionName);
        transaction.setTransactionAmount(transactionAmount);
        transaction.setTransactionDate(transactionDate);
        transaction.setTransactionType(transactionType);

        logger.info(String.valueOf(transaction.getUser().getId()));
        return transactionRepository.save(transaction);
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














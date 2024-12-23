package com.example.expense_tracker_server.service;

import com.example.expense_tracker_server.entity.TransactionCategory;
import com.example.expense_tracker_server.entity.User;
import com.example.expense_tracker_server.repository.TransactionCategoryRepository;
import com.example.expense_tracker_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class TransactionCategoryService {
    private static final Logger logger = Logger.getLogger(TransactionCategoryService.class.getName());

    @Autowired
    private TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    // create
    public TransactionCategory createTransactionCategory(int userId, String categoryName, String categoryColor){
        logger.info("Creating Transaction Category");

        // find user by id
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) return null;

        TransactionCategory transactionCategory = new TransactionCategory();
        transactionCategory.setUser(user.get());
        transactionCategory.setCategoryName(categoryName);
        transactionCategory.setCategoryColor(categoryColor);

        return transactionCategoryRepository.save(transactionCategory);
    }

    // read
    public Optional<TransactionCategory> getTransactionCategoryById(int id){
        logger.info("Getting Transaction Category: " + id);
        return transactionCategoryRepository.findById(id);
    }

    public List<TransactionCategory> getAllTransactionCategoriesByUserId(int userId){
        logger.info("Getting all Transaction Categories for User: " + userId);
        return transactionCategoryRepository.findAllByUserId(userId);
    }

    // update
    public TransactionCategory updateTransactionCategoryById(int transactionCategoryId, String newCategoryName,
                                                             String newCategoryColor){
        logger.info("Updating TransactionCategory with Id: " + transactionCategoryId);

        // find the transaction category
        Optional<TransactionCategory> transactionCategory = transactionCategoryRepository.findById(transactionCategoryId);

        if(transactionCategory.isEmpty()) return null;

        TransactionCategory newTransactionCategory = transactionCategory.get();
        newTransactionCategory.setUser(transactionCategory.get().getUser());
        newTransactionCategory.setCategoryName(newCategoryName);
        newTransactionCategory.setCategoryColor(newCategoryColor);

        return transactionCategoryRepository.save(newTransactionCategory);
    }

    // delete
    public void delTransactionCategoryById(int transactionCategoryId){
        logger.info("Deleting TransactionCategory with Id: " + transactionCategoryId);

        // find transaction category
        Optional<TransactionCategory> transactionCategory = transactionCategoryRepository.findById(transactionCategoryId);

        if(transactionCategory.isEmpty()) return;

        transactionCategoryRepository.delete(transactionCategory.get());
    }

}

















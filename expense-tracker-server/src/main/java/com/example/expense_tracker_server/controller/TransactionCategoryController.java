package com.example.expense_tracker_server.controller;

import com.example.expense_tracker_server.entity.TransactionCategory;
import com.example.expense_tracker_server.repository.TransactionCategoryRepository;
import com.example.expense_tracker_server.service.TransactionCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/transaction-categories")
public class TransactionCategoryController {
    private static final Logger logger = Logger.getLogger(TransactionCategoryController.class.getName());

    @Autowired
    private TransactionCategoryService transactionCategoryService;

//    @PostMapping
//    public ResponseEntity<TransactionCategory> createTransactionCategory(@RequestParam int userId,
//                        @RequestParam String categoryName, @RequestParam String categoryColor){
//        logger.info("Create Transaction Category for: {" + categoryName + ", " + categoryColor + "}");
//
//        transactionCategoryService.createTransactionCategory(userId, categoryName, categoryColor);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }

    @PostMapping
    public ResponseEntity<TransactionCategory> createTransactionCategory(@RequestBody TransactionCategory transactionCategory){
        logger.info("Create Transaction Category for: {" + transactionCategory.getCategoryName() + ", " +
                transactionCategory.getCategoryColor() + "}");

        transactionCategoryService.createTransactionCategory(transactionCategory.getUser().getId(),
                                            transactionCategory.getCategoryName(), transactionCategory.getCategoryColor());

        // todo return transaction category obj
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionCategory> getTransactionCategoryById(@PathVariable int id){
        logger.info("Getting Transaction Category with Id: " + id);

        Optional<TransactionCategory> transactionCategory = transactionCategoryService.getTransactionCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(transactionCategory.get());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionCategory>> getAllTransactionCategoriesByUserId(@PathVariable int userId){
        logger.info("Getting All Transaction Categories with User Id: " + userId);

        List<TransactionCategory> transactionCategoryList = transactionCategoryService.getAllTransactionCategoriesByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(transactionCategoryList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionCategory> updateTransactionCategory(@PathVariable int id,
                                                                         @RequestParam String newCategoryName,
                                                                         @RequestParam String newCategoryColor){
        logger.info("Updating Transaction Category with Id: " + id);

        TransactionCategory updatedTransactionCategory =
                transactionCategoryService.updateTransactionCategoryById(id, newCategoryName, newCategoryColor);

        return ResponseEntity.status(HttpStatus.OK).body(updatedTransactionCategory);
    }
}

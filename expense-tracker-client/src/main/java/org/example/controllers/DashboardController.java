package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.example.components.TransactionComponent;
import org.example.dialogs.CreateNewCategoryDialog;
import org.example.dialogs.CreateNewTransactionDialog;
import org.example.dialogs.ViewAndEditCategoryDialog;
import org.example.models.Transaction;
import org.example.models.User;
import org.example.utils.SqlUtil;
import org.example.views.DashboardView;
import org.example.views.LoginView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class DashboardController {
    private DashboardView dashboardView;
    private User user;
    private List<Transaction> userTransactions;

    public DashboardController(DashboardView dashboardView){
        this.dashboardView = dashboardView;
        fetchUserData();

        addMenuActions();
        addContentActions();
    }

    public void fetchUserData(){
        System.out.println("Fetching User Data");
        dashboardView.getRecentTransactionsBox().getChildren().clear();

        user = SqlUtil.getUserByEmail(dashboardView.getEmail());

        // retrieve transactions
        userTransactions = SqlUtil.getAllTransactionsByUserId(user.getId());

        // don't
        if(userTransactions == null) return;

        for(Transaction transaction : userTransactions){
            dashboardView.getRecentTransactionsBox().getChildren().add(
                    new TransactionComponent(transaction)
            );
        }

        // calculate the total income, total expense, and total balance
        // we use BigDecimal instead of double so that we can control how we round the numbers
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for(Transaction transaction : userTransactions){
            BigDecimal transactionAmount = BigDecimal.valueOf(transaction.getTransactionAmount()); // Convert to BigDecimal
            if(transaction.getTransactionType().equalsIgnoreCase("income")){
                totalIncome = totalIncome.add(transactionAmount);
            }else{
                totalExpense = totalExpense.add(transactionAmount);
            }
        }
        System.out.println(totalExpense);
        BigDecimal currentBalance = totalIncome.subtract(totalExpense);

        // Round to 2 decimal places
        totalIncome = totalIncome.setScale(2, RoundingMode.HALF_UP);
        totalExpense = totalExpense.setScale(2, RoundingMode.HALF_UP);
        currentBalance = currentBalance.setScale(2, RoundingMode.HALF_UP);

        // update view
        dashboardView.getTotalExpense().setText("$" + totalExpense);
        dashboardView.getTotalIncome().setText("$" + totalIncome);
        dashboardView.getCurrentBalance().setText("$" + currentBalance);
    }

    private void addMenuActions(){
        dashboardView.getCreateCategoryMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new CreateNewCategoryDialog(user).showAndWait();
            }
        });

        dashboardView.getViewCategoriesMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new ViewAndEditCategoryDialog(user, DashboardController.this).showAndWait();
            }
        });

        dashboardView.getLogoutMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new LoginView().show();
            }
        });
    }

    private void addContentActions(){
        dashboardView.getAddTransactionButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // launch create transaction dialog
                new CreateNewTransactionDialog(user, DashboardController.this).showAndWait();
            }
        });
    }

//    private TransactionComponent createTransactionComponent(JsonElement jsonElement){
//
//    }
}

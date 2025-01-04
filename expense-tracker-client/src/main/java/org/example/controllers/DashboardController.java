package org.example.controllers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.example.components.TransactionComponent;
import org.example.dialogs.CreateNewCategoryDialog;
import org.example.dialogs.CreateOrEditTransactionDialog;
import org.example.dialogs.ViewOrEditCategoryDialog;
import org.example.models.MonthlyFinance;
import org.example.models.Transaction;
import org.example.models.User;
import org.example.utils.SqlUtil;
import org.example.views.DashboardView;
import org.example.views.LoginView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class DashboardController {
    private DashboardView dashboardView;
    private User user;
    private List<Transaction> userTransactions;
    private int year;

    public DashboardController(DashboardView dashboardView, int year){
        this.dashboardView = dashboardView;
        this.year = year;
        fetchUserData();

        addMenuActions();
        addContentActions();
    }

    public void fetchUserData(){
        dashboardView.getRecentTransactionsBox().getChildren().clear();

        user = SqlUtil.getUserByEmail(dashboardView.getEmail());

        // retrieve transactions
        userTransactions = SqlUtil.getAllTransactionsByUserId(user.getId());

        // calculate table
        dashboardView.getTransactionsTable().setItems(calculateMonthlyFinances());


        // calculate the total income, total expense, and total balance
        // we use BigDecimal instead of double so that we can control how we round the numbers
        // N: the reason for this change is so that we can recalculate if we delete the last transaction
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        if(userTransactions != null){
            // render transaction components
            for(Transaction transaction : userTransactions){
                dashboardView.getRecentTransactionsBox().getChildren().add(
                        new TransactionComponent(this, transaction)
                );
            }

            // update total income and total expense
            for(Transaction transaction : userTransactions){
                BigDecimal transactionAmount = BigDecimal.valueOf(transaction.getTransactionAmount()); // Convert to BigDecimal
                if(transaction.getTransactionType().equalsIgnoreCase("income")){
                    totalIncome = totalIncome.add(transactionAmount);
                }else{
                    totalExpense = totalExpense.add(transactionAmount);
                }
            }
        }

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
                new ViewOrEditCategoryDialog(user, DashboardController.this).showAndWait();
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
                new CreateOrEditTransactionDialog(DashboardController.this, true).showAndWait();
            }
        });
    }

    private ObservableList<MonthlyFinance> calculateMonthlyFinances(){
        // parallel indexing (each index refers to the month - 1, i.e. january is 1 but in index 0, february is 2 but in index 1)
        double[] incomeCounter = new double[12];
        double[] expenseCounter = new double[12];

        // we do this so that we can still return 0s if there are no transactions
        if(userTransactions != null){
            // total up income and expense for each month
            for(Transaction transaction : userTransactions){
                LocalDate transactionDate = transaction.getTransactionDate();
                if(transaction.getTransactionType().equalsIgnoreCase("income")){
                    incomeCounter[transactionDate.getMonth().getValue() - 1] += transaction.getTransactionAmount();
                }else{
                    expenseCounter[transactionDate.getMonth().getValue() - 1] += transaction.getTransactionAmount();
                }
            }
        }

        // add each monthly finance
        ObservableList<MonthlyFinance> monthlyFinances = FXCollections.observableArrayList();
        for(int i = 0; i < 12; i++){
            MonthlyFinance monthlyFinance = new MonthlyFinance(
                    Month.of(i + 1).name(), // used to get the name based on the index
                    new BigDecimal(String.valueOf(incomeCounter[i])),
                    new BigDecimal(String.valueOf(expenseCounter[i]))
            );

            monthlyFinances.add(monthlyFinance);
        }

        return monthlyFinances;
    }



    public User getUser(){
        return user;
    }
}

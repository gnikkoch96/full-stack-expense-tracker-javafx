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

    private void fetchUserData(){
        System.out.println("Fetching User Data");

        user = SqlUtil.getUserByEmail(dashboardView.getEmail());

        // retrieve transactions
        userTransactions = SqlUtil.getAllTransactionsByUserId(user.getId());

        for(Transaction transaction : userTransactions){
            dashboardView.getRecentTransactionsBox().getChildren().add(
                    new TransactionComponent(transaction)
            );
        }

        // calculate the total income, total expense, and total balance
        double totalIncome = 0;
        double totalExpense = 0;

        for(Transaction transaction : userTransactions){
            if(transaction.getTransactionType().equalsIgnoreCase("income")){
                totalIncome += transaction.getTransactionAmount();
            }else{
                totalExpense += transaction.getTransactionAmount();
            }
        }

        double currentBalance = totalIncome - totalExpense;

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
                new ViewAndEditCategoryDialog(user).showAndWait();
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
                new CreateNewTransactionDialog(user).showAndWait();
            }
        });
    }

//    private TransactionComponent createTransactionComponent(JsonElement jsonElement){
//
//    }
}

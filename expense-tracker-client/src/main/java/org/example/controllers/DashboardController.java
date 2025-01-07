package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.example.components.TransactionComponent;
import org.example.dialogs.CreateNewCategoryDialog;
import org.example.dialogs.CreateOrEditTransactionDialog;
import org.example.dialogs.ViewOrEditCategoryDialog;
import org.example.dialogs.ViewTransactionsDialog;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashboardController {
    private DashboardView dashboardView;
    private User user;

    private List<Transaction> recentTransactions;
    private List<Transaction> currentTransactionsByYear;

    // todo think of a better way of doing this
    private List<Transaction> allUserTransactions;

    private int currentYear;

    public DashboardController(DashboardView dashboardView, int currentYear){
        this.dashboardView = dashboardView;
        this.currentYear = currentYear;
        fetchUserData();

        addMenuActions();
        addContentActions();
        addTableActions();
    }

    public void fetchUserData(){
        dashboardView.getRecentTransactionsBox().getChildren().clear();

        user = SqlUtil.getUserByEmail(dashboardView.getEmail());

        // retrieve transactions
        recentTransactions = SqlUtil.getAllTransactionsByUserId(user.getId(), null, null);
        allUserTransactions = SqlUtil.getAllTransactionsByUserId(user.getId(), null, null);
        currentTransactionsByYear = SqlUtil.getAllTransactionsByUserId(user.getId(), currentYear, null);

        System.out.println("Retrieved all transactions");

        calculateValidYears();

        // calculate table
        dashboardView.getTransactionsTable().setItems(calculateMonthlyFinances());

        // calculate the total income, total expense, and total balance
        // we use BigDecimal instead of double so that we can control how we round the numbers
        // N: the reason for this change is so that we can recalculate if we delete the last transaction
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        if(recentTransactions != null){
            // render transaction components
            for(Transaction transaction : recentTransactions){
                dashboardView.getRecentTransactionsBox().getChildren().add(
                        new TransactionComponent(this, transaction)
                );
            }
        }

        if(currentTransactionsByYear != null){
            // update total income and total expense
            for(Transaction transaction : currentTransactionsByYear){
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

    private void addTableActions(){
        dashboardView.getYearComboBox().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // update current year
                currentYear = dashboardView.getYearComboBox().getValue();

                // refresh data
                fetchUserData();
            }
        });


        // add an action listener to each table row
        dashboardView.getTransactionsTable().setRowFactory(new Callback<TableView<MonthlyFinance>, TableRow<MonthlyFinance>>() {
            @Override
            public TableRow<MonthlyFinance> call(TableView<MonthlyFinance> monthlyFinanceTableView) {
                TableRow<MonthlyFinance> row = new TableRow<>();

                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(!row.isEmpty() && mouseEvent.getClickCount() == 2){
                            MonthlyFinance monthlyFinance = row.getItem();
                            new ViewTransactionsDialog(
                                    user,
                                    DashboardController.this,
                                    monthlyFinance.getMonth()
                            ).showAndWait();
                        }
                    }
                });

                return row;
            }
        });
    }

    private void calculateValidYears(){
        if(allUserTransactions == null) return;

        Set<Integer> years = new HashSet<>();

        for(Transaction transaction : allUserTransactions){
            LocalDate transactionDate = transaction.getTransactionDate();

            // only adds years that aren't already in the drop down menu for years
            if(!dashboardView.getYearComboBox().getItems().contains(transactionDate.getYear())){
                years.add(transactionDate.getYear());
            }
        }

        dashboardView.getYearComboBox().getItems().addAll(years);
    }

    private ObservableList<MonthlyFinance> calculateMonthlyFinances(){
        // parallel indexing (each index refers to the month - 1, i.e. january is 1 but in index 0, february is 2 but in index 1)
        double[] incomeCounter = new double[12];
        double[] expenseCounter = new double[12];

        // we do this so that we can still return 0s if there are no transactions
        if(currentTransactionsByYear != null){
            // total up income and expense for each month
            for(Transaction transaction : currentTransactionsByYear){
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

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }
}

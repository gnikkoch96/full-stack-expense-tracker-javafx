package org.example.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.controllers.DashboardController;
import org.example.utils.Config;
import org.example.utils.ViewNavigator;

public class DashboardView implements View{
    private String email;
    private Label currentBalanceLabel, currentBalance;
    private Label totalIncomeLabel, totalIncome;
    private Label totalExpenseLabel, totalExpense;

    public DashboardView(String email){
        this.email = email;
        currentBalanceLabel = new Label("Current Balance:");
        totalIncomeLabel = new Label("Total Income:");
        totalExpenseLabel = new Label("Total Expense:");

        // test todo remove
        currentBalance = new Label("$0.00");
        totalIncome = new Label("$0.00");
        totalExpense = new Label("$0.00");
    }

    @Override
    public void show() {
        Scene scene = createScene();
        scene.getStylesheets().add(getClass().getResource(
                "/style.css"
        ).toExternalForm());

        new DashboardController(this);
        ViewNavigator.switchViews(scene);
    }

    private Scene createScene(){
        // layout
        VBox vBox = new VBox();
        vBox.getStyleClass().addAll("main-background", "dashboard-padding");
        vBox.setAlignment(Pos.TOP_CENTER);

        HBox balanceSummaryBox = createBalanceSummaryBox();

        vBox.getChildren().addAll(balanceSummaryBox);
        return new Scene(vBox, Config.APP_WIDTH, Config.APP_HEIGHT);
    }

    private HBox createBalanceSummaryBox(){
        HBox balanceSummaryBox = new HBox(400);

        // current balance
        VBox currentBalanceBox = new VBox();
        currentBalanceLabel.getStyleClass().addAll("text-size-lg", "text-light-gray");
        currentBalance.getStyleClass().addAll("text-size-lg", "text-white");
        currentBalanceBox.getChildren().addAll(currentBalanceLabel,currentBalance);

        // total income
        VBox totalIncomeBox = new VBox();
        totalIncomeLabel.getStyleClass().addAll("text-size-lg", "text-light-gray");
        totalIncome.getStyleClass().addAll("text-size-lg", "text-white");
        totalIncomeBox.getChildren().addAll(totalIncomeLabel, totalIncome);

        // total expense
        VBox totalExpenseBox = new VBox();
        totalExpenseLabel.getStyleClass().addAll("text-size-lg", "text-light-gray");
        totalExpense.getStyleClass().addAll("text-size-lg", "text-white");
        totalExpenseBox.getChildren().addAll(totalExpenseLabel, totalExpense);

        balanceSummaryBox.getChildren().addAll(currentBalanceBox, totalIncomeBox, totalExpenseBox);
        return balanceSummaryBox;
    }

    // getters and setters
    public String getEmail() {
        return email;
    }

    public Label getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Label currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Label getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Label totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Label getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Label totalExpense) {
        this.totalExpense = totalExpense;
    }
}

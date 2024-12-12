package org.example.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.controllers.DashboardController;
import org.example.dialogs.CreateNewCategoryDialog;
import org.example.dialogs.ViewAndEditCategoryDialog;
import org.example.utils.Util;
import org.example.utils.ViewNavigator;

public class DashboardView implements View{
    private String email;

    // menu
    private MenuItem createCategoryMenuItem, viewCategoriesMenuItem, logoutMenuItem;

    private Label currentBalanceLabel, currentBalance;
    private Label totalIncomeLabel, totalIncome;
    private Label totalExpenseLabel, totalExpense;
    private Button addTransactionButton;

    private DashboardController dashboardController;

    public DashboardView(String email){
        this.email = email;
        currentBalanceLabel = new Label("Current Balance:");
        totalIncomeLabel = new Label("Total Income:");
        totalExpenseLabel = new Label("Total Expense:");
        addTransactionButton = new Button("+");

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

        dashboardController = new DashboardController(this);

        ViewNavigator.switchViews(scene);
    }

    private Scene createScene(){
        // menu
        MenuBar menuBar = createMenuBar();

        // layout
        VBox vBox = new VBox();
        vBox.getStyleClass().addAll("main-background");
        vBox.getChildren().addAll(menuBar);

        // main content (Note: this is to prevent the padding from affecting menu)
        VBox vBoxContent = new VBox();
        vBoxContent.getStyleClass().addAll("dashboard-padding");
        vBoxContent.setAlignment(Pos.TOP_CENTER);

        HBox balanceSummaryBox = createBalanceSummaryBox();
        HBox mainDashboardContentBox = createContentBox();

        vBoxContent.getChildren().addAll(balanceSummaryBox, mainDashboardContentBox);
        vBox.getChildren().addAll(vBoxContent);
        return new Scene(vBox, Util.APP_WIDTH, Util.APP_HEIGHT);
    }

    private MenuBar createMenuBar(){
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        createCategoryMenuItem = new MenuItem("Create Category");
        viewCategoriesMenuItem = new MenuItem("View Categories");
        logoutMenuItem = new MenuItem("Logout");

        fileMenu.getItems().addAll(createCategoryMenuItem, viewCategoriesMenuItem, logoutMenuItem);
        menuBar.getMenus().addAll(fileMenu);
        return menuBar;
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

    private HBox createContentBox(){
        HBox contentBox = new HBox();


        contentBox.getChildren().addAll(addTransactionButton);
        return contentBox;
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

    public Button getAddTransactionButton() {
        return addTransactionButton;
    }

    public void setAddTransactionButton(Button addTransactionButton) {
        this.addTransactionButton = addTransactionButton;
    }

    public MenuItem getCreateCategoryMenuItem() {
        return createCategoryMenuItem;
    }

    public void setCreateCategoryMenuItem(MenuItem createCategoryMenuItem) {
        this.createCategoryMenuItem = createCategoryMenuItem;
    }

    public MenuItem getViewCategoriesMenuItem() {
        return viewCategoriesMenuItem;
    }

    public void setViewCategoriesMenuItem(MenuItem viewCategoriesMenuItem) {
        this.viewCategoriesMenuItem = viewCategoriesMenuItem;
    }

    public MenuItem getLogoutMenuItem() {
        return logoutMenuItem;
    }

    public void setLogoutMenuItem(MenuItem logoutMenuItem) {
        this.logoutMenuItem = logoutMenuItem;
    }
}

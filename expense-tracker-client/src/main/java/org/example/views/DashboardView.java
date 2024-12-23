package org.example.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.controllers.DashboardController;
import org.example.utils.Util;
import org.example.utils.ViewNavigator;

public class DashboardView implements View{
    private String email;

    // menu
    private MenuItem createCategoryMenuItem, viewCategoriesMenuItem, logoutMenuItem;

    private Label currentBalanceLabel, currentBalance;
    private Label totalIncomeLabel, totalIncome;
    private Label totalExpenseLabel, totalExpense;

    private Label recentTransactionLabel;
    private Button addTransactionButton;
    private VBox recentTransactionsBox;

    private DashboardController dashboardController;

    public DashboardView(String email){
        this.email = email;
        currentBalanceLabel = new Label("Current Balance:");
        totalIncomeLabel = new Label("Total Income:");
        totalExpenseLabel = new Label("Total Expense:");

        recentTransactionLabel = new Label("Recent Transactions");
        addTransactionButton = new Button("+");

        // test todo remove when you can get this value via calculations
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
        VBox.setVgrow(vBoxContent, Priority.ALWAYS);

        HBox balanceSummaryBox = createBalanceSummaryBox();
        GridPane contentGridPane = createContentGridPane();
        VBox.setVgrow(contentGridPane, Priority.ALWAYS); // expands grid vertically to fit the rest of the parent

        vBoxContent.getChildren().addAll(balanceSummaryBox, contentGridPane);
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

    private GridPane createContentGridPane(){
        GridPane contentGridPane = new GridPane();

        // set constraints to the cells in the gridpane
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        contentGridPane.getColumnConstraints().addAll(col1, col2);

        VBox transactionContentBox = createTransactionContentBox();
        GridPane.setVgrow(transactionContentBox, Priority.ALWAYS);

        contentGridPane.add(transactionContentBox, 1, 0);

        return contentGridPane;
    }

    private VBox createTransactionContentBox(){
        VBox transactionContentBox = new VBox();

        // label and button
        // todo clean up
        HBox transactionLabelAndButton = new HBox();
        recentTransactionLabel.getStyleClass().addAll("text-size-lg", "text-light-gray");

        // add spacing
        Region spaceRegion = new Region();
        HBox.setHgrow(spaceRegion, Priority.ALWAYS);

        addTransactionButton.getStyleClass().addAll("field-background", "text-size-md", "text-light-gray", "rounded-border");
        transactionLabelAndButton.getChildren().addAll(recentTransactionLabel, spaceRegion, addTransactionButton);

        // recent transactions
        recentTransactionsBox = new VBox(10);
        recentTransactionsBox.getStyleClass().addAll("field-background", "rounded-border", "padding-10px");

        ScrollPane recentTransactionScrollpane = new ScrollPane(recentTransactionsBox);

        // makes scroll pane take up the width and height of its parent
        recentTransactionScrollpane.setFitToWidth(true);
        recentTransactionScrollpane.setFitToHeight(true);

        VBox.setVgrow(recentTransactionScrollpane, Priority.ALWAYS);

        transactionContentBox.getChildren().addAll(transactionLabelAndButton, recentTransactionScrollpane);
        return transactionContentBox;
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

    public VBox getRecentTransactionsBox() {
        return recentTransactionsBox;
    }

    public void setRecentTransactionsBox(VBox recentTransactionsBox) {
        this.recentTransactionsBox = recentTransactionsBox;
    }
}

package org.example.views;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import org.example.controllers.DashboardController;
import org.example.models.MonthlyFinance;
import org.example.utils.Util;
import org.example.utils.ViewNavigator;

import java.math.BigDecimal;

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

    // table
    private TableView<MonthlyFinance> transactionsTable;

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
        VBox vBoxContent = new VBox(20);
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
        HBox balanceSummaryBox = new HBox();

        // current balance
        VBox currentBalanceBox = new VBox();
        currentBalanceLabel.getStyleClass().addAll("text-size-lg", "text-light-gray");
        currentBalance.getStyleClass().addAll("text-size-lg", "text-white");
        currentBalanceBox.getChildren().addAll(currentBalanceLabel,currentBalance);

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        // total income
        VBox totalIncomeBox = new VBox();
        totalIncomeLabel.getStyleClass().addAll("text-size-lg", "text-light-gray");
        totalIncome.getStyleClass().addAll("text-size-lg", "text-white");
        totalIncomeBox.getChildren().addAll(totalIncomeLabel, totalIncome);

        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);

        // total expense
        VBox totalExpenseBox = new VBox();
        totalExpenseLabel.getStyleClass().addAll("text-size-lg", "text-light-gray");
        totalExpense.getStyleClass().addAll("text-size-lg", "text-white");
        totalExpenseBox.getChildren().addAll(totalExpenseLabel, totalExpense);

        balanceSummaryBox.getChildren().addAll(currentBalanceBox, region1, totalIncomeBox, region2, totalExpenseBox);
        return balanceSummaryBox;
    }

    private GridPane createContentGridPane(){
        GridPane contentGridPane = new GridPane();
        contentGridPane.setHgap(10);

        // set constraints to the cells in the gridpane
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        contentGridPane.getColumnConstraints().addAll(col1, col2);

        VBox transactionsTableBox = createTransactionsTableBox();
        GridPane.setVgrow(transactionsTableBox, Priority.ALWAYS);

        VBox transactionContentBox = createTransactionContentBox();
        transactionContentBox.getStyleClass().addAll("field-background", "rounded-border", "padding-10px");
        GridPane.setVgrow(transactionContentBox, Priority.ALWAYS);

        contentGridPane.add(transactionsTableBox, 0, 0);
        contentGridPane.add(transactionContentBox, 1, 0);
        return contentGridPane;
    }

    private VBox createTransactionsTableBox(){
        VBox transactionTableBox = new VBox();

        transactionsTable = new TableView<>();
        transactionsTable.getStyleClass().addAll("rounded-borders");

        // 1st param data model to extract data from
        // 2nd param is the type of the value we are extracting from the model
        TableColumn<MonthlyFinance, String> monthColumn = new TableColumn<>("Month");

        // we use PropertyValueFactory to extract the month/income/expense data from our MonthlyFinance data model
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));

        TableColumn<MonthlyFinance, BigDecimal> incomeColumn = new TableColumn<>("Income");
        incomeColumn.setCellValueFactory(new PropertyValueFactory<>("income"));

        TableColumn<MonthlyFinance, BigDecimal> expenseColumn = new TableColumn<>("Expense");
        expenseColumn.setCellValueFactory(new PropertyValueFactory<>("expense"));

        // todo will need to create a query for this
        ObservableList<MonthlyFinance> monthlyFinances = FXCollections.observableArrayList(
                new MonthlyFinance("January", new BigDecimal("5000.00"), new BigDecimal("2000.00")),
                new MonthlyFinance("February", new BigDecimal("6000.00"), new BigDecimal("2500.00")),
                new MonthlyFinance("March", new BigDecimal("4500.00"), new BigDecimal("1800.00")),
                new MonthlyFinance("April", new BigDecimal("7000.00"), new BigDecimal("3000.00"))
        );

        transactionsTable.setItems(monthlyFinances);
        transactionsTable.getColumns().addAll(monthColumn, incomeColumn, expenseColumn);
        VBox.setVgrow(transactionsTable, Priority.ALWAYS);

        /**
         * We do this so that we can calculate the width of the transaction table correctly or else we get 0 as a return values
         * The reason is that by the time we render the dashboard view, javafx is still calculating the layout sizes
         * This is the standard and generally preferred way to handle layout-related operations that need the final sizes of components.
         * Platform.runLater() schedules a task to be executed on the JavaFX application thread after the layout has been calculated.
         */
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                double colsWidth = transactionsTable.getWidth() * (0.33);
                monthColumn.setPrefWidth(colsWidth);
                incomeColumn.setPrefWidth(colsWidth);
                expenseColumn.setPrefWidth(colsWidth);
            }
        });

        transactionTableBox.getChildren().add(transactionsTable);
        return transactionTableBox;
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
        recentTransactionsBox.getStyleClass().addAll();

        ScrollPane recentTransactionScrollpane = new ScrollPane(recentTransactionsBox);

        // makes scroll pane take up the width and height of its parent
        recentTransactionScrollpane.setFitToWidth(true);
        recentTransactionScrollpane.setFitToHeight(true);

//        VBox.setVgrow(recentTransactionScrollpane, Priority.ALWAYS);

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

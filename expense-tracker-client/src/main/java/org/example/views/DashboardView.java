package org.example.views;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import org.example.animations.LoadingAnimationPane;
import org.example.controllers.DashboardController;
import org.example.models.MonthlyFinance;
import org.example.utils.Util;
import org.example.utils.ViewNavigator;

import java.math.BigDecimal;
import java.time.Year;

public class DashboardView implements View{
    private String email;

    // menu
    private MenuItem createCategoryMenuItem, viewCategoriesMenuItem, logoutMenuItem;

    private LoadingAnimationPane loadingAnimationPane;

    private Label currentBalanceLabel, currentBalance;
    private Label totalIncomeLabel, totalIncome;
    private Label totalExpenseLabel, totalExpense;

    private Label recentTransactionLabel;
    private Button addTransactionButton;
    private Button viewChartButton;
    private ComboBox<Integer> yearComboBox;

    private VBox recentTransactionsBox;
    private ScrollPane recentTransactionsScrollPane;

    // table
    private TableView<MonthlyFinance> transactionsTable;
    private TableColumn<MonthlyFinance, String> monthColumn;
    private TableColumn<MonthlyFinance, BigDecimal> incomeColumn;
    private TableColumn<MonthlyFinance, BigDecimal> expenseColumn;

    private DashboardController dashboardController;

    public DashboardView(String email){
        this.email = email;
        loadingAnimationPane = new LoadingAnimationPane(Util.APP_WIDTH, Util.APP_HEIGHT);

        currentBalanceLabel = new Label("Current Balance:");
        totalIncomeLabel = new Label("Total Income:");
        totalExpenseLabel = new Label("Total Expense:");

        recentTransactionLabel = new Label("Recent Transactions");
        addTransactionButton = new Button("+");

        currentBalance = new Label();
        totalIncome = new Label();
        totalExpense = new Label();
    }

    @Override
    public void show() {
        Scene scene = createScene();
        scene.getStylesheets().add(getClass().getResource(
                "/style.css"
        ).toExternalForm());

        dashboardController = new DashboardController(this, Year.now().getValue());

        // add a listener to the width and height changes (important to keep UI responsive)
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                loadingAnimationPane.resizeWidth(t1.doubleValue());
                resizeTableWidthColumns();
            }
        });

        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                loadingAnimationPane.resizeHeight(t1.doubleValue());
            }
        });

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
        vBox.getChildren().addAll(vBoxContent, loadingAnimationPane);

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

        VBox transactionsTableBox = new VBox(20);
        HBox comboBoxAndChartButtonContainer = createComboBoxAndChartButtonContainer();
        VBox transactionsTableContentBox = createTransactionsTableContentBox();
        VBox.setVgrow(transactionsTableContentBox, Priority.ALWAYS);

        transactionsTableBox.getChildren().addAll(comboBoxAndChartButtonContainer, transactionsTableContentBox);

        VBox recentTransactionsBox = createRecentTransactionsBox();
        recentTransactionsBox.getStyleClass().addAll("field-background", "rounded-border", "padding-10px");
        GridPane.setVgrow(recentTransactionsBox, Priority.ALWAYS);

        contentGridPane.add(transactionsTableBox, 0, 0);
        contentGridPane.add(recentTransactionsBox, 1, 0);
        return contentGridPane;
    }

    private HBox createComboBoxAndChartButtonContainer(){
        HBox hbox = new HBox(15);
        yearComboBox = new ComboBox<>();
        yearComboBox.getStyleClass().addAll("text-size-md");
        yearComboBox.setValue(Year.now().getValue());

        viewChartButton = new Button("View Chart");
        viewChartButton.getStyleClass().addAll("field-background", "text-light-gray", "text-size-md");

        hbox.getChildren().addAll(yearComboBox, viewChartButton);
        return hbox;
    }

    private VBox createTransactionsTableContentBox(){
        VBox transactionTableBox = new VBox();

        transactionsTable = new TableView<>();

        // 1st param data model to extract data from
        // 2nd param is the type of the value we are extracting from the model
        monthColumn = new TableColumn<>("Month");

        // we use PropertyValueFactory to extract the month/income/expense data from our MonthlyFinance data model
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        monthColumn.getStyleClass().addAll("main-background", "text-size-md", "text-light-gray");
        monthColumn.setResizable(false);

        incomeColumn = new TableColumn<>("Income");
        incomeColumn.setCellValueFactory(new PropertyValueFactory<>("income"));
        incomeColumn.getStyleClass().addAll("main-background", "text-size-md", "text-light-gray");
        incomeColumn.setResizable(false);

        expenseColumn = new TableColumn<>("Expense");
        expenseColumn.setCellValueFactory(new PropertyValueFactory<>("expense"));
        expenseColumn.getStyleClass().addAll("main-background", "text-size-md", "text-light-gray");
        expenseColumn.setResizable(false);

        transactionsTable.getColumns().addAll(monthColumn, incomeColumn, expenseColumn);
        VBox.setVgrow(transactionsTable, Priority.ALWAYS);

        resizeTableWidthColumns();

        transactionTableBox.getChildren().add(transactionsTable);
        return transactionTableBox;
    }

    private VBox createRecentTransactionsBox(){
        VBox transactionContentBox = new VBox();

        // label and button
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

        recentTransactionsScrollPane = new ScrollPane(recentTransactionsBox);

        // makes scroll pane take up the width and height of its parent
        recentTransactionsScrollPane.setFitToWidth(true);
        recentTransactionsScrollPane.setFitToHeight(true);
        
        transactionContentBox.getChildren().addAll(transactionLabelAndButton, recentTransactionsScrollPane);
        return transactionContentBox;
    }

    private void resizeTableWidthColumns(){
        /**
         * We do this so that we can calculate the width of the transaction table correctly or else we get 0 as a return values
         * The reason is that by the time we render the dashboard view, javafx is still calculating the layout sizes
         * This is the standard and generally preferred way to handle layout-related operations that need the final sizes of components.
         * Platform.runLater() schedules a task to be executed on the JavaFX application thread after the layout has been calculated.
         */
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                double colsWidth = transactionsTable.getWidth() * (0.335);
                monthColumn.setPrefWidth(colsWidth);
                incomeColumn.setPrefWidth(colsWidth);
                expenseColumn.setPrefWidth(colsWidth);
            }
        });
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

    public TableView<MonthlyFinance> getTransactionsTable() {
        return transactionsTable;
    }

    public void setTransactionsTable(TableView<MonthlyFinance> transactionsTable) {
        this.transactionsTable = transactionsTable;
    }

    public ComboBox<Integer> getYearComboBox() {
        return yearComboBox;
    }

    public void setYearComboBox(ComboBox<Integer> yearComboBox) {
        this.yearComboBox = yearComboBox;
    }

    public ScrollPane getRecentTransactionsScrollPane() {
        return recentTransactionsScrollPane;
    }

    public void setRecentTransactionsScrollPane(ScrollPane recentTransactionsScrollPane) {
        this.recentTransactionsScrollPane = recentTransactionsScrollPane;
    }

    public LoadingAnimationPane getLoadingAnimationPane() {
        return loadingAnimationPane;
    }

    public void setLoadingAnimationPane(LoadingAnimationPane loadingAnimationPane) {
        this.loadingAnimationPane = loadingAnimationPane;
    }

    public Button getViewChartButton() {
        return viewChartButton;
    }

    public void setViewChartButton(Button viewChartButton) {
        this.viewChartButton = viewChartButton;
    }
}

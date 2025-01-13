package org.example.dialogs;

import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.components.TransactionComponent;
import org.example.controllers.DashboardController;
import org.example.models.Transaction;
import org.example.models.TransactionCategory;
import org.example.utils.SqlUtil;
import org.example.utils.Util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CreateOrEditTransactionDialog extends CustomDialog{
    // used to access the fetchUserData() to refresh when a new transaction is added
    private DashboardController dashboardController;

    private List<TransactionCategory> transactionCategories;

    private TextField transactionNameField, transactionAmountField;
    private DatePicker transactionDatePicker;
    private ComboBox<String> transactionCategoryBox;
    private ToggleGroup transactionTypeToggleGroup;

    // this is used for editing the transaction.
    private TransactionComponent transactionComponent;
    private boolean isCreating;

    // this has to go first (than the other constructors) so that transaction isn't null in the beginning
    public CreateOrEditTransactionDialog(DashboardController dashboardController, TransactionComponent transactionComponent,
                                         boolean isCreating) {
        super(dashboardController.getUser());
        this.dashboardController = dashboardController;
        this.isCreating = isCreating;
        this.transactionComponent = transactionComponent;

        setTitle(isCreating ? "Create New Transaction" : "Edit Transaction");
        setWidth(700);
        setHeight(595);

        transactionCategories = SqlUtil.getCategoriesByUser(dashboardController.getUser());

        VBox contentBox = createContentBox();
        getDialogPane().setContent(contentBox);
    }

    public CreateOrEditTransactionDialog(DashboardController dashboardController, boolean isCreating) {
        // this will call on the other constructor as it's going to be pretty much the same here
        this(dashboardController, null, isCreating);
    }

    public CreateOrEditTransactionDialog(TransactionComponent transactionComponent, boolean isCreating){
        this(transactionComponent.getDashboardController(), transactionComponent, isCreating);
    }

    private VBox createContentBox(){
        VBox contentBox = new VBox(30);
        contentBox.setAlignment(Pos.CENTER);

        transactionNameField = new TextField();
        transactionNameField.setPromptText("Enter Transaction Name");
        transactionNameField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-md",
                    "rounded-border");

        // todo add validation to make sure that the input is a number
        transactionAmountField = new TextField();
        transactionAmountField.setPromptText("Enter Transaction Amount");
        transactionAmountField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-md",
                    "rounded-border");

        transactionDatePicker = new DatePicker();
        transactionDatePicker.setPromptText("Enter Date");
        transactionDatePicker.getStyleClass().addAll("field-background", "text-light-gray", "text-size-md",
                    "rounded-border");
        transactionDatePicker.setPrefWidth(Double.MAX_VALUE);

        transactionCategoryBox = new ComboBox<>();
        for(TransactionCategory transactionCategory : transactionCategories){
            transactionCategoryBox.getItems().add(transactionCategory.getCategoryName());
        }
        transactionCategoryBox.setPromptText("Choose Category");
        transactionCategoryBox.getStyleClass().addAll("field-background", "text-light-gray", "text-size-md",
                    "rounded-border");
        transactionCategoryBox.setPrefWidth(Double.MAX_VALUE);

        if(!isCreating){
            Transaction transaction = transactionComponent.getTransaction();

            // populate the fields with the transaction data
            transactionNameField.setText(transaction.getTransactionName());
            transactionAmountField.setText(String.valueOf(transaction.getTransactionAmount()));
            transactionDatePicker.setValue(transaction.getTransactionDate());

            // note: transaction category can be null
            transactionCategoryBox.setValue(
                    transaction.getTransactionCategory() != null ? transaction.getTransactionCategory().getCategoryName()
                        : ""
            );
        }

        contentBox.getChildren().addAll(transactionNameField, transactionAmountField, transactionDatePicker,
                transactionCategoryBox, createRadioButtons(), createConfirmCancelButtons());

        return contentBox;
    }

    private HBox createRadioButtons(){
        HBox radioButtons = new HBox(50);
        radioButtons.setAlignment(Pos.CENTER);

        transactionTypeToggleGroup = new ToggleGroup();
        RadioButton incomeRadioBtn = new RadioButton("Income");
        incomeRadioBtn.setToggleGroup(transactionTypeToggleGroup);
        incomeRadioBtn.getStyleClass().addAll("text-size-md", "text-light-gray");

        RadioButton expenseRadioBtn = new RadioButton("Expense");
        expenseRadioBtn.setToggleGroup(transactionTypeToggleGroup);
        expenseRadioBtn.getStyleClass().addAll("text-size-md", "text-light-gray");
        radioButtons.getChildren().addAll(incomeRadioBtn, expenseRadioBtn);

        if(!isCreating){
            Transaction transaction = transactionComponent.getTransaction();

            // select the corresponding type button
            if (transaction.getTransactionType().equalsIgnoreCase("income")) {
                incomeRadioBtn.setSelected(true);
            } else {
                expenseRadioBtn.setSelected(true);
            }
        }

        return radioButtons;
    }

    private HBox createConfirmCancelButtons(){
        HBox confirmCancelButtons = new HBox(50);
        confirmCancelButtons.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("Save");
        saveBtn.setPrefWidth(200);
        saveBtn.getStyleClass().addAll("bg-light-blue", "text-white", "text-size-md", "rounded-border");

        // depending on if the user creating or editing, it will perform different actions
        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    JsonObject transactionJsonData = createTransactionJsonData();

                    // call backend to store transaction to database
                    boolean transactionActionStatus;

                    // depending on flag we will either be creating or updating a transaction
                    if(isCreating){
                        transactionActionStatus = SqlUtil.postTransaction(transactionJsonData);
                    }else{
                        transactionActionStatus = SqlUtil.updateTransaction(transactionJsonData);
                    }

                    if(transactionActionStatus){
                        Util.showAlertDialog(
                                Alert.AlertType.INFORMATION,
                                isCreating ? "Success: Created a new Transaction!"
                                        : "Success: Updated Transaction!"
                        );

                        // reset the fields
                        resetFields();

                        // refresh dashboard
                        dashboardController.fetchUserData();
                    }else{
                        Util.showAlertDialog(
                                Alert.AlertType.ERROR,
                                isCreating ? "Error: Failed to create Transaction"
                                        : "Error: Failed to Update Transaction"
                        );
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(200);
        cancelBtn.getStyleClass().addAll("text-size-md", "rounded-border");
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                CreateOrEditTransactionDialog.this.close();
            }
        });

        confirmCancelButtons.getChildren().addAll(saveBtn, cancelBtn);
        return confirmCancelButtons;
    }

    private JsonObject createTransactionJsonData(){
        JsonObject transactionData = new JsonObject();

        if(!isCreating){
            transactionData.addProperty("id", transactionComponent.getTransaction().getId());
        }

        TransactionCategory category = getTransactionCategoryByName(transactionCategoryBox.getValue());
        JsonObject transactionCategoryData = new JsonObject();

        if(category != null){
            transactionCategoryData.addProperty("id", category.getId());
            transactionData.add("transactionCategory", transactionCategoryData);
        }


        JsonObject userData = new JsonObject();
        userData.addProperty("id", user.getId());
        transactionData.add("user", userData);

        String transactionName = transactionNameField.getText();
        transactionData.addProperty("transactionName", transactionName);

        // used to catch error if we are converting an invalid string to a double
        double transactionAmount = Double.parseDouble(transactionAmountField.getText());
        transactionData.addProperty("transactionAmount", transactionAmount);

        LocalDate transactionDate = transactionDatePicker.getValue();
        transactionData.addProperty("transactionDate", transactionDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

        String transactionType = ((RadioButton) transactionTypeToggleGroup.getSelectedToggle()).getText();
        transactionData.addProperty("transactionType", transactionType);

        if(transactionComponent != null)
            updateTransactionComponent(transactionName, transactionAmount, transactionDate, transactionType, category);

        return transactionData;
    }

    private void updateTransactionComponent(String transactionName, double transactionAmount,
                                            LocalDate transactionDate, String transactionType, TransactionCategory transactionCategory){
        transactionComponent.getTransactionNameLabel().setText(transactionName);
        transactionComponent.getTransactionDateLabel().setText(transactionDate.toString());

        // category
        if(transactionCategory != null){
            transactionComponent.getTransactionCategoryLabel().setText(transactionCategory.getCategoryName());
            transactionComponent.getTransactionCategoryLabel().setStyle("-fx-background: #" + transactionCategory.getCategoryColor());
        }

        // amount
        transactionComponent.getTransactionAmount().setText("$" + transactionAmount);

        // depending if it is income or expense
        if(transactionType.equalsIgnoreCase("income")){
            transactionComponent.getTransactionAmount().getStyleClass().add("text-light-green");
        }else{
            transactionComponent.getTransactionAmount().getStyleClass().add("text-light-red");
        }
    }

    private TransactionCategory getTransactionCategoryByName(String categoryName){
        for(TransactionCategory transactionCategory : transactionCategories){
            if(transactionCategory.getCategoryName().equals(categoryName)){
                return transactionCategory;
            }
        }

        // could not find
        return null;
    }

    private void resetFields(){
        transactionNameField.setText("");
        transactionAmountField.setText("");
        transactionDatePicker.setValue(null);
        transactionCategoryBox.setValue("Choose Category");
    }
}






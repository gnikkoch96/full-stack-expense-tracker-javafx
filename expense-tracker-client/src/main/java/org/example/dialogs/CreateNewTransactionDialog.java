package org.example.dialogs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.models.User;
import org.example.utils.SqlUtil;

import java.util.List;

public class CreateNewTransactionDialog extends CustomDialog{
    private List<String> transactionCategories;

    private TextField transactionNameField, transactionAmountField;
    private DatePicker transactionDatePicker;
    private ComboBox<String> transactionCategoryBox;
    private ToggleGroup transactionTypeToggleGroup;
    private Button saveBtn, cancelBtn;

    public CreateNewTransactionDialog(User user) {
        super(user);
        setTitle("Create New Transaction");
        setWidth(700);
        setHeight(595);

        transactionCategories = SqlUtil.fetchCategories(user);

        VBox contentBox = createContentBox();
        getDialogPane().setContent(contentBox);
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
        transactionCategoryBox.getItems().addAll(transactionCategories);
        transactionCategoryBox.setPromptText("Choose Category");
        transactionCategoryBox.getStyleClass().addAll("field-background", "text-light-gray", "text-size-md",
                    "rounded-border");
        transactionCategoryBox.setPrefWidth(Double.MAX_VALUE);

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

        return radioButtons;
    }

    private HBox createConfirmCancelButtons(){
        HBox confirmCancelButtons = new HBox(50);
        confirmCancelButtons.setAlignment(Pos.CENTER);

        saveBtn = new Button("Save");
        saveBtn.setPrefWidth(200);
        saveBtn.getStyleClass().addAll("bg-light-blue", "text-white", "text-size-md", "rounded-border");

        cancelBtn = new Button("Cancel");
        cancelBtn.setPrefWidth(200);
        cancelBtn.getStyleClass().addAll("text-size-md", "rounded-border");
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                CreateNewTransactionDialog.this.close();
            }
        });

        confirmCancelButtons.getChildren().addAll(saveBtn, cancelBtn);
        return confirmCancelButtons;
    }

}






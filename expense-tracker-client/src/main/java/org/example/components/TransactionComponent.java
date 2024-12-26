package org.example.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.example.models.Transaction;
import org.example.models.TransactionCategory;

public class TransactionComponent extends HBox {
    private Transaction transaction;
    private TransactionCategory transactionCategory;

    private Label transactionCategoryLabel, transactionNameLabel, transactionAmount, transactionDateLabel;
    private Button editBtn, delBtn;

    public TransactionComponent(Transaction transaction){
        this.transaction = transaction;

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().addAll("padding-10px", "rounded-border", "main-background");

        VBox catNameDateBox = createCatNameDateBox();

        // horizontal space
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        // change the dollar sign to your currency
        transactionAmount = new Label("$" + transaction.getTransactionAmount());
        transactionAmount.getStyleClass().addAll("text-size-md");

        // depending on the transaction type the color will change
        if(transaction.getTransactionType().equalsIgnoreCase("Expense")){
            transactionAmount.getStyleClass().add("text-light-red");
        }else{
            transactionAmount.getStyleClass().add("text-light-green");
        }

        HBox actionButtons = createActionButtons();

        getChildren().addAll(catNameDateBox, region, transactionAmount, actionButtons);
    }

    private VBox createCatNameDateBox(){
        VBox catNameDateBox = new VBox();

        if(transaction.getTransactionCategory() != null) {
            transactionCategoryLabel = new Label(transaction.getTransactionCategory().getCategoryName());

            // N: must add a # in front, for proper formatting
            transactionCategoryLabel.setTextFill(Paint.valueOf("#" + transaction.getTransactionCategory().getCategoryColor()));
        }else {
            transactionCategoryLabel = new Label("Undefined");
            transactionCategoryLabel.getStyleClass().addAll("text-light-gray");
        }


        transactionNameLabel = new Label(transaction.getTransactionName());
        transactionNameLabel.getStyleClass().addAll("text-light-gray", "text-size-md");

        transactionDateLabel = new Label(transaction.getTransactionDate().toString());
        transactionDateLabel.getStyleClass().addAll("text-light-gray");

        catNameDateBox.getChildren().addAll(transactionCategoryLabel, transactionNameLabel, transactionDateLabel);
        return catNameDateBox;
    }

    private HBox createActionButtons(){
        HBox actionButtons = new HBox(20);
        actionButtons.setAlignment(Pos.CENTER);

        editBtn = new Button("Edit");
        editBtn.getStyleClass().addAll("text-size-md", "rounded-borders");

        delBtn = new Button("Del");
        delBtn.getStyleClass().addAll("text-size-md", "rounded-borders", "bg-light-red", "text-white");

        actionButtons.getChildren().addAll(editBtn, delBtn);
        return actionButtons;
    }
}










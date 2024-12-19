package org.example.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.models.Transaction;
import org.example.models.TransactionCategory;

public class TransactionComponent extends HBox {
    private Transaction transaction;
    private TransactionCategory transactionCategory;

    private Label transactionCategoryLabel, transactionName, transactionAmount, transactionDate;
    private Button editBtn, saveBtn, delBtn;

    public TransactionComponent(Transaction transaction){
        this.transaction = transaction;

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().addAll("view-category-component-padding", "rounded-border", "field-background");

        VBox catNameDateBox = createCatNameDateBox();

        getChildren().addAll(catNameDateBox);
    }

    private VBox createCatNameDateBox(){
        VBox catNameDateBox = new VBox();

        transactionCategoryLabel = new Label(transactionCategory.getCategoryName());

        return catNameDateBox;
    }
}










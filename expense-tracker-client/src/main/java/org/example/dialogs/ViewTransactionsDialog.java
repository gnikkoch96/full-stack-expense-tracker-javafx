package org.example.dialogs;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.example.components.TransactionComponent;
import org.example.controllers.DashboardController;
import org.example.models.Transaction;
import org.example.models.User;
import org.example.utils.SqlUtil;

import java.time.Month;
import java.util.List;

public class ViewTransactionsDialog extends CustomDialog{
    private User user;
    private String monthName;

    private DashboardController dashboardController;

    public ViewTransactionsDialog(User user, DashboardController dashboardController, String monthName) {
        super(user);
        this.user = user;
        this.dashboardController = dashboardController;
        this.monthName = monthName;

        setTitle("View Transactions");
        setWidth(815);
        setHeight(500);

        ScrollPane dialogContent = createDialogVBoxContent();
        getDialogPane().setContent(dialogContent);
    }

    private ScrollPane createDialogVBoxContent(){
        VBox dialogVBox = new VBox(20);

        ScrollPane scrollPane = new ScrollPane(dialogVBox);
        scrollPane.setMinHeight(getHeight() - 40);
        scrollPane.setFitToWidth(true);

        List<Transaction> transactions = SqlUtil.getAllTransactionsByUserId(
                user.getId(),
                dashboardController.getCurrentYear(),
                Month.valueOf(monthName).getValue()
        );

        if(transactions != null){
            for(Transaction transaction : transactions){
                TransactionComponent transactionComponent = new TransactionComponent(dashboardController, transaction);
                transactionComponent.getStyleClass().addAll("border-light-gray");
                dialogVBox.getChildren().add(transactionComponent);
            }
        }

        return scrollPane;
    }

}

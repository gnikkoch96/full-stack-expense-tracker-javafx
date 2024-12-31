package org.example.dialogs;

import com.google.gson.*;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.example.components.CategoryComponent;
import org.example.controllers.DashboardController;
import org.example.models.TransactionCategory;
import org.example.models.User;
import org.example.utils.SqlUtil;
import java.util.List;


public class ViewOrEditCategoryDialog extends CustomDialog{
    private DashboardController dashboardController;

    public ViewOrEditCategoryDialog(User user, DashboardController dashboardController) {
        super(user);
        this.dashboardController = dashboardController;

        setTitle("View Categories");
        setWidth(815);
        setHeight(500);

        ScrollPane dialogContent = createDialogVBoxContent();
        getDialogPane().setContent(dialogContent);
    }

    private ScrollPane createDialogVBoxContent(){
        VBox dialogVBox = new VBox(20);

        ScrollPane scrollPane = new ScrollPane(dialogVBox);
        scrollPane.setMinHeight(getHeight() - 40); // makes scrollpane take the whole height of the dialog, we subtract by 40 because the category component gets cut off
        scrollPane.setFitToWidth(true); // makes the VBox match the width of the ScrollPane

        // perform read on db for all the categories based on the user id
        List<TransactionCategory> transactionCategories = SqlUtil.getCategoriesByUser(user);
        for(TransactionCategory transactionCategory : transactionCategories){
            CategoryComponent categoryComponent = createCategoryComponent(transactionCategory);
            dialogVBox.getChildren().add(categoryComponent);
        }

        return scrollPane;
    }

    private CategoryComponent createCategoryComponent(TransactionCategory transactionCategory) {
        // create the UI component to add to display the category
        return new CategoryComponent(
                dashboardController,
                transactionCategory
        );
    }
}

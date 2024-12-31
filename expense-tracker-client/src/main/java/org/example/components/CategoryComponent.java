package org.example.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.controllers.DashboardController;
import org.example.models.TransactionCategory;
import org.example.utils.ApiHandler;
import org.example.utils.SqlUtil;
import org.example.utils.Util;

import java.io.IOException;
import java.net.HttpURLConnection;


// Note: similar to react components, we will create components like this for better organization
// we will use this to create a component for each category that we retrieve
public class CategoryComponent extends HBox{
    private DashboardController dashboardController;

    private TransactionCategory transactionCategory;

    private TextField categoryTextField;
    private ColorPicker colorPicker;
    private Button editButton;
    private Button saveButton;
    private Button deleteButton;

    // flags
    private boolean isEditing;

    public CategoryComponent(DashboardController dashboardController
                             ,TransactionCategory transactionCategory){
        this.dashboardController = dashboardController;
        this.transactionCategory = transactionCategory;

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().addAll("view-category-component-padding", "rounded-border", "field-background", "padding-10px");

        categoryTextField = new TextField(transactionCategory.getCategoryName());
        HBox.setHgrow(categoryTextField, Priority.ALWAYS);
        categoryTextField.setMaxWidth(Double.MAX_VALUE);

        categoryTextField.setEditable(false);
        categoryTextField.getStyleClass().addAll("field-background", "text-size-md", "text-light-gray");

        colorPicker = new ColorPicker();
        colorPicker.setDisable(true);
        colorPicker.setValue(Color.valueOf(transactionCategory.getCategoryColor()));
        colorPicker.getStyleClass().addAll("text-size-sm");

        editButton = new Button("Edit");
        editButton.setMinWidth(50);
        editButton.getStyleClass().addAll("text-size-sm");
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                isEditing = true;
                handleToggle();
            }
        });

        // is hidden when edit button is shown
        saveButton = new Button("Save");
        saveButton.setVisible(false);
        saveButton.setMinWidth(50);
        saveButton.setManaged(false); // Note: to fully remove the button from layout you need to do this or else it leaves space for the button
        saveButton.getStyleClass().addAll("text-size-sm");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                isEditing = false;
                handleToggle();

                // retrieve data
                String newCategoryName = categoryTextField.getText();
                String newCategoryColor = Util.getHexColorValue(colorPicker);

                // save to database
                // todo put in sqlutil
                HttpURLConnection httpConn = null;
                try {
                    // different example
                     httpConn = ApiHandler.fetchApiResponse(
                            "/api/transaction-categories/" + transactionCategory.getId() + "?newCategoryName=" + newCategoryName + "&newCategoryColor=" + newCategoryColor,
                            ApiHandler.RequestMethod.PUT,
                            null
                    );

                    if(httpConn != null && httpConn.getResponseCode() != 200){
                        System.out.println("Updating Error: " + httpConn.getResponseCode());
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }finally {
                    if(httpConn != null){
                        httpConn.disconnect();
                    }
                }
            }
        });

        deleteButton = new Button("Del");
        deleteButton.getStyleClass().addAll("bg-light-red", "text-white", "text-size-sm");
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // delete from database
                boolean deletedTransactionCategoryStatus = SqlUtil.deleteTransactionCategoryById(transactionCategory.getId());

                // failed to delete transaction category
                if(!deletedTransactionCategoryStatus) return;

                // remove component from UI
                setVisible(false);
                setManaged(false);
                if(getParent() instanceof VBox){
                    ((VBox) getParent()).getChildren().remove(CategoryComponent.this);
                }

                // refresh dashboard
                dashboardController.fetchUserData();
            }
        });

        getChildren().addAll(categoryTextField, colorPicker, editButton, saveButton, deleteButton);
    }

    private void handleToggle(){
        if(isEditing){
            isEditing = false;

            // enable category text
            categoryTextField.setEditable(true);
            categoryTextField.setStyle("-fx-background-color: #fff; -fx-text-fill: #000");

            // enable color picker
            colorPicker.setDisable(false);

            // hide edit button
            editButton.setVisible(false);
            editButton.setManaged(false);

            // display save button
            saveButton.setVisible(true);
            saveButton.setManaged(true);

        }else{
            isEditing = true;

            // disable category text
            categoryTextField.setEditable(false);
            categoryTextField.setStyle("-fx-background-color: #515050; -fx-text-fill: #BEB9B9;");

            // disable color picker
            colorPicker.setDisable(true);

            // hide save button
            saveButton.setVisible(false);
            saveButton.setManaged(false);

            // display edit button
            editButton.setVisible(true);
            editButton.setManaged(true);
        }
    }
}

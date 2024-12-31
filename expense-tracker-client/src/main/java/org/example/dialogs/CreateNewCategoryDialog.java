package org.example.dialogs;

import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.models.User;
import org.example.utils.SqlUtil;
import org.example.utils.Util;

// Note: add validation
// Note: the way the dialog works is that it expects to return something but that is not necessary for us
public class CreateNewCategoryDialog extends CustomDialog {
    private TextField newCategoryTextField;
    private ColorPicker colorPicker;
    private Button createCategoryButton;

    public CreateNewCategoryDialog(User user){
        super(user);
        setTitle("Create new Category");
        VBox dialogVBox = createDialogVBoxContent();
        getDialogPane().setContent(dialogVBox);
    }

    private VBox createDialogVBoxContent(){
        VBox dialogVBox = new VBox(20);
        dialogVBox.setAlignment(Pos.CENTER);

        newCategoryTextField = new TextField();
        newCategoryTextField.setPromptText("Create new Category");
        newCategoryTextField.setFocusTraversable(false);
        newCategoryTextField.getStyleClass().addAll("text-size-md", "field-background", "text-light-gray");

        colorPicker = new ColorPicker();
        colorPicker.getStyleClass().addAll("text-size-md");
        colorPicker.setMaxWidth(Double.MAX_VALUE);

        createCategoryButton = new Button("Create");
        createCategoryButton.getStyleClass().addAll("text-size-md", "bg-light-blue", "text-white");
        createCategoryButton.setMaxWidth(Double.MAX_VALUE);
        createCategoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // retrieve the values
                JsonObject transactionCategoryData = createCategoryJsonData();

                boolean createTransactionCategoryStatus = SqlUtil.postTransactionCategory(transactionCategoryData);

                if(createTransactionCategoryStatus){
                    Util.showAlertDialog(Alert.AlertType.INFORMATION, "Success: Category created successfully!");
                    resetFields();
                }else{
                    Util.showAlertDialog(Alert.AlertType.ERROR,  "Error: Failed to create category");
                }
            }
        });

        dialogVBox.getChildren().addAll(newCategoryTextField, colorPicker, createCategoryButton);
        return dialogVBox;
    }

    private JsonObject createCategoryJsonData() {
        String categoryName = newCategoryTextField.getText();
        String color = Util.getHexColorValue(colorPicker);

        JsonObject transactionCategoryData = new JsonObject();

        JsonObject userData = new JsonObject();
        userData.addProperty("id", user.getId());

        transactionCategoryData.add("user", userData);
        transactionCategoryData.addProperty("categoryName", categoryName);
        transactionCategoryData.addProperty("categoryColor", color);

        System.out.println(transactionCategoryData);

        return transactionCategoryData;
    }

    private void resetFields(){
        newCategoryTextField.setText("");
        colorPicker.setValue(Color.WHITE);
    }
}

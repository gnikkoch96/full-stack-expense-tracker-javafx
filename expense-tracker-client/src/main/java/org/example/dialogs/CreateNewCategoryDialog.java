package org.example.dialogs;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.models.User;
import org.example.utils.ApiHandler;

import java.io.IOException;
import java.net.HttpURLConnection;

// Note: the way the dialog works is that it expects to return something but that is not necessary for us
public class CreateNewCategoryDialog extends Dialog<String> {
    private User user;
    private Alert infoAlert, errorAlert;

    public CreateNewCategoryDialog(User user){
        this.user = user;
        infoAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert = new Alert(Alert.AlertType.ERROR);

        getDialogPane().getStylesheets().add(getClass().getResource(
                "/style.css"
        ).toExternalForm());
        getDialogPane().getStyleClass().addAll("main-background");
        setTitle("Create new Category");

        VBox dialogVBox = createDialogVBoxContent();

        // Note: we need to add a buttontype to be able to close the dialog
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setVisible(false);
        okButton.setDisable(false);

        getDialogPane().setContent(dialogVBox);

    }

    private VBox createDialogVBoxContent(){
        VBox dialogVBox = new VBox(20);
        dialogVBox.setAlignment(Pos.CENTER);

        TextField newCategoryTextField = new TextField();
        newCategoryTextField.setPromptText("Create new Category");
        newCategoryTextField.setFocusTraversable(false);
        newCategoryTextField.getStyleClass().addAll("text-size-md", "field-background", "text-light-gray");

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.getStyleClass().addAll("text-size-md");
        colorPicker.setMaxWidth(Double.MAX_VALUE);

        Button createCategoryButton = new Button("Create");
        createCategoryButton.getStyleClass().addAll("text-size-md", "bg-light-blue", "text-white");
        createCategoryButton.setMaxWidth(Double.MAX_VALUE);
        createCategoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // retrieve the values
                String categoryName = newCategoryTextField.getText();
                String color = colorPicker.getValue().toString();

                // Note: the reason why we removed two is because ColorPicker returns a hex that includes the alpha values which
                // we don't want. They are the last two characters so we substring them out before storing them into the database.
                String hexColorValue = color.substring(0, color.length() - 2);

                JsonObject transactionCategoryData = new JsonObject();

                JsonObject userData = new JsonObject();
                userData.addProperty("id", user.getId());

                transactionCategoryData.add("user", userData);
                transactionCategoryData.addProperty("categoryName", categoryName);
                transactionCategoryData.addProperty("categoryColor", hexColorValue);

                HttpURLConnection httpConn = null;
                try{
                    httpConn = ApiHandler.fetchApiResponse(
                            "/api/transaction-categories", ApiHandler.RequestMethod.POST, transactionCategoryData
                    );

                    if(httpConn != null && httpConn.getResponseCode() != 204){
                        System.out.println("Error: " + httpConn.getResponseCode());
                        errorAlert.setContentText("There was an error creating your Category, please try again");
                        errorAlert.showAndWait();
                        return;
                    }

                    // reset the fields
                    newCategoryTextField.setText("");
                    colorPicker.setValue(Color.WHITE);

                    infoAlert.setContentText("Successfully Created Category!");
                    infoAlert.showAndWait();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        });

        dialogVBox.getChildren().addAll(newCategoryTextField, colorPicker, createCategoryButton);
        return dialogVBox;
    }
}

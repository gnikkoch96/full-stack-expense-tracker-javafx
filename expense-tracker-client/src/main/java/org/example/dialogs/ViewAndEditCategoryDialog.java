package org.example.dialogs;

import com.google.gson.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.models.User;
import org.example.utils.ApiHandler;

import java.awt.*;
import java.net.HttpURLConnection;


public class ViewAndEditCategoryDialog extends CustomDialog{
    public ViewAndEditCategoryDialog(User user) {
        super(user);
        setTitle("View Categories");
        VBox dialogContent = createDialogVBoxContent();
        getDialogPane().setContent(dialogContent);
    }

    private VBox createDialogVBoxContent(){
        VBox dialogVBox = new VBox(20);

        // perform read on db for all the categories based on the user id
        HttpURLConnection httpConn = ApiHandler.fetchApiResponse(
                "/api/transaction-categories/user/" + user.getId(),
                ApiHandler.RequestMethod.GET,
                null
        );

        String results = ApiHandler.readApiResponse(httpConn);
        JsonArray resultJson = new JsonParser().parse(results).getAsJsonArray();
        for(int i = 0; i < resultJson.size(); i++){
            JsonElement jsonElement = resultJson.get(i);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            // create the UI component to add to display the category
            HBox categoryComponent = createCategoryComponent(
                    jsonObject.get("categoryName").getAsString(),
                    jsonObject.get("categoryColor").getAsString()
            );

            dialogVBox.getChildren().add(categoryComponent);
        }

        return dialogVBox;
    }

    // we will use this to create a component for each category that we retrieve
    private HBox createCategoryComponent(String categoryText, String hexColorCode){
        HBox categoryComponent = new HBox(10);
        categoryComponent.setAlignment(Pos.CENTER);
        categoryComponent.getStyleClass().addAll("view-category-component-padding", "rounded-border");

        Label categoryLabel = new Label(categoryText);
        categoryLabel.getStyleClass().addAll("view-category-margin", "text-size-md");

        ColorPicker colorPicker = new ColorPicker();

        colorPicker.setValue(Color.valueOf(hexColorCode));

        Button editButton = new Button("Edit");
        editButton.getStyleClass().addAll("text-size-md");

        Button deleteButton = new Button("Del");
        deleteButton.getStyleClass().addAll("bg-light-red", "text-white", "text-size-md");

        categoryComponent.getChildren().addAll(categoryLabel, colorPicker, editButton, deleteButton);
        return categoryComponent;
    }
}

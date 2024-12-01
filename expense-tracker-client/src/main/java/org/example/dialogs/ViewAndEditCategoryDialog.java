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
import org.example.components.CategoryComponent;
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
            CategoryComponent categoryComponent = new CategoryComponent(
                    jsonObject.get("categoryName").getAsString(),
                    jsonObject.get("categoryColor").getAsString()
            );

            dialogVBox.getChildren().add(categoryComponent);
        }

        return dialogVBox;
    }
}

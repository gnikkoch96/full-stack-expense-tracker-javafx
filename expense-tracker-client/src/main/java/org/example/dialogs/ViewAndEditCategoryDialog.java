package org.example.dialogs;

import com.google.gson.*;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.example.components.CategoryComponent;
import org.example.models.TransactionCategory;
import org.example.models.User;
import org.example.utils.ApiHandler;

import java.io.IOException;
import java.net.HttpURLConnection;


public class ViewAndEditCategoryDialog extends CustomDialog{
    public ViewAndEditCategoryDialog(User user) {
        super(user);
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
        HttpURLConnection httpConn = null;
        try{
            httpConn = ApiHandler.fetchApiResponse(
                    "/api/transaction-categories/user/" + user.getId(),
                    ApiHandler.RequestMethod.GET,
                    null
            );

            if(httpConn != null && httpConn.getResponseCode() != 200){
                System.out.println("Getting Error: " + httpConn.getResponseCode());
            }

            // retrieve json response
            String results = ApiHandler.readApiResponse(httpConn);
            JsonArray resultJson = new JsonParser().parse(results).getAsJsonArray();

            // create category ui component for each record returned
            for(int i = 0; i < resultJson.size(); i++){
                JsonElement jsonElement = resultJson.get(i);
                CategoryComponent categoryComponent = createCategoryComponent(jsonElement);
                dialogVBox.getChildren().add(categoryComponent);
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(httpConn != null){
                httpConn.disconnect();
            }
        }

        return scrollPane;
    }

    private CategoryComponent createCategoryComponent(JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        TransactionCategory transactionCategory = new TransactionCategory(
                jsonObject.get("id").getAsInt(),
                jsonObject.get("categoryName").getAsString(),
                jsonObject.get("categoryColor").getAsString()
        );

        // create the UI component to add to display the category
        CategoryComponent categoryComponent = new CategoryComponent(
                transactionCategory
        );
        return categoryComponent;
    }
}

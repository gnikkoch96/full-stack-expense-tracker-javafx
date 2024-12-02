package org.example.dialogs;

import com.google.gson.*;

import javafx.scene.layout.VBox;
import org.example.components.CategoryComponent;
import org.example.models.TransactionCategory;
import org.example.models.User;
import org.example.utils.ApiHandler;
import java.net.HttpURLConnection;


public class ViewAndEditCategoryDialog extends CustomDialog{
    public ViewAndEditCategoryDialog(User user) {
        super(user);
        setTitle("View Categories");
        setWidth(800);
        setHeight(500);

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
            CategoryComponent categoryComponent = createCategoryComponent(jsonElement);
            dialogVBox.getChildren().add(categoryComponent);
        }

        return dialogVBox;
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

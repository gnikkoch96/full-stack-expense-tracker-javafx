package org.example.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.example.models.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

// we'll be using this class to query things easier for us
public class SqlUtil {
    public static List<String> fetchCategories(User user){
        List<String> categories = new ArrayList<>();

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

            for(int i = 0; i < resultJson.size(); i++){
                JsonElement jsonElement = resultJson.get(i);
                String categoryName = jsonElement.getAsJsonObject().get("categoryName").getAsString();
                categories.add(categoryName);
            }

            return categories;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(httpConn != null){
                httpConn.disconnect();
            }
        }

        // couldn't find any categories
        return null;
    }
}

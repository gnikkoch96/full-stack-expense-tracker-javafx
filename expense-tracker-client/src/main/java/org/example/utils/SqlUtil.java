package org.example.utils;

import com.google.gson.*;
import javafx.scene.paint.Color;
import org.example.models.Transaction;
import org.example.models.TransactionCategory;
import org.example.models.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// we'll be using this class to query things easier for us
public class SqlUtil {
    // GET requests
    public static List<TransactionCategory> getCategoriesByUser(User user){
        List<TransactionCategory> categories = new ArrayList<>();

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

                int categoryId = jsonElement.getAsJsonObject().get("id").getAsInt();
                String categoryName = jsonElement.getAsJsonObject().get("categoryName").getAsString();
                String categoryColor = jsonElement.getAsJsonObject().get("categoryColor").getAsString();

                categories.add(new TransactionCategory(categoryId, categoryName, categoryColor));
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

    public static User getUserByEmail(String userEmail){
        HttpURLConnection httpConn = null;
        try {
            // call on the spring user api to get user by email
            httpConn = ApiHandler.fetchApiResponse("/api/users?email=" + userEmail,
                    ApiHandler.RequestMethod.GET, null);

            if(httpConn != null && httpConn.getResponseCode() != 200){
                System.out.println("Error: " + httpConn.getResponseCode());
                return null;
            }

            String userDataJson = ApiHandler.readApiResponse(httpConn);

            // convert string to jsonobject
            JsonObject jsonObject = JsonParser.parseString(userDataJson).getAsJsonObject();

            // create user
            int id = jsonObject.get("id").getAsInt();
            String name = jsonObject.get("name").getAsString();
            String email = jsonObject.get("email").getAsString();
            String password = jsonObject.get("password").getAsString();

            // Note: since there is no built in method of getting a json object as a local date time obj we have to take some extra steps
            LocalDateTime createdAt = new Gson().fromJson(jsonObject.get("created_at"), LocalDateTime.class);
            return new User(id, name, email, password, createdAt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            // safely disconnect from the api
            if(httpConn != null)
                httpConn.disconnect();
        }
    }

    // note: year can be null depending on the data we are trying to get
    // null year means get the top 10 recent transactinos
    // having a year means we will only get the transactions for that year instead of all transactions
    public static List<Transaction> getAllTransactionsByUserId(int userId, Integer year, Integer month){
        List<Transaction> transactions = new ArrayList<>();

        HttpURLConnection httpConn = null;
        try {
            // depending on the year value it will add the year param to the url or not
            String urlPath = "/api/transactions/user/" + userId;
            System.out.println(year + " " + month);
            if(year != null) {
                urlPath += "?year=" + year;

                if(month != null){
                    urlPath += "&month=" + month;
                }
            }

            System.out.println(urlPath);

            // call on the spring user api to get user by email
            httpConn = ApiHandler.fetchApiResponse(urlPath,
                    ApiHandler.RequestMethod.GET, null);

            if(httpConn != null && httpConn.getResponseCode() != 200){
                System.out.println("Error: " + httpConn.getResponseCode());
                return null;
            }

            String results = ApiHandler.readApiResponse(httpConn);
            JsonArray resultJson = new JsonParser().parse(results).getAsJsonArray();

            for(int i = 0; i < resultJson.size(); i++){
                JsonObject transactionObj = resultJson.get(i).getAsJsonObject();
                int transactionId = transactionObj.get("id").getAsInt();

                TransactionCategory transactionCategory = null;
                if (transactionObj.has("transactionCategory") && !transactionObj.get("transactionCategory").isJsonNull()) { // Check if the field exists and is not null
                    JsonObject transactionCategoryObject = transactionObj.get("transactionCategory").getAsJsonObject();
                    int transactionCategoryId = transactionCategoryObject.get("id").getAsInt();
                    String transactionCategoryName = transactionCategoryObject.get("categoryName").getAsString();
                    String transactionCategoryColor = transactionCategoryObject.get("categoryColor").getAsString();
                    transactionCategory = new TransactionCategory(transactionCategoryId, transactionCategoryName, transactionCategoryColor);
                }

                String transactionName = transactionObj.get("transactionName").getAsString();
                double transactionAmount = transactionObj.get("transactionAmount").getAsDouble();
                LocalDate transactionDate = LocalDate.parse(transactionObj.get("transactionDate").getAsString());
                String transactionType = transactionObj.get("transactionType").getAsString();

                // create transaction obj
                Transaction transaction = new Transaction(transactionId, transactionCategory, transactionName,
                        transactionAmount, transactionDate, transactionType);
                transactions.add(transaction);
            }

            return transactions;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            // safely disconnect from the api
            if(httpConn != null)
                httpConn.disconnect();
        }
    }

    // page - refers to the page that we want
    public static List<Transaction> getRecentTransactionsByUserId(int userId, int startPage, int endPage, int size){
        List<Transaction> transactions = new ArrayList<>();

        HttpURLConnection httpConn = null;
        try {
            // depending on the year value it will add the year param to the url or not
            String urlPath = "/api/transactions/recent/user/" + userId + "?startPage=" + startPage + "&endPage=" + endPage + "&size=" + size;
            System.out.println(urlPath);

            // call on the spring user api to get user by email
            httpConn = ApiHandler.fetchApiResponse(urlPath,
                    ApiHandler.RequestMethod.GET, null);

            if(httpConn != null && httpConn.getResponseCode() != 200){
                System.out.println("Error: " + httpConn.getResponseCode());
                return null;
            }

            String results = ApiHandler.readApiResponse(httpConn);
            JsonArray resultJson = new JsonParser().parse(results).getAsJsonArray();

            for(int i = 0; i < resultJson.size(); i++){
                JsonObject transactionObj = resultJson.get(i).getAsJsonObject();
                int transactionId = transactionObj.get("id").getAsInt();

                TransactionCategory transactionCategory = null;
                if (transactionObj.has("transactionCategory") && !transactionObj.get("transactionCategory").isJsonNull()) { // Check if the field exists and is not null
                    JsonObject transactionCategoryObject = transactionObj.get("transactionCategory").getAsJsonObject();
                    int transactionCategoryId = transactionCategoryObject.get("id").getAsInt();
                    String transactionCategoryName = transactionCategoryObject.get("categoryName").getAsString();
                    String transactionCategoryColor = transactionCategoryObject.get("categoryColor").getAsString();
                    transactionCategory = new TransactionCategory(transactionCategoryId, transactionCategoryName, transactionCategoryColor);
                }

                String transactionName = transactionObj.get("transactionName").getAsString();
                double transactionAmount = transactionObj.get("transactionAmount").getAsDouble();
                LocalDate transactionDate = LocalDate.parse(transactionObj.get("transactionDate").getAsString());
                String transactionType = transactionObj.get("transactionType").getAsString();

                // create transaction obj
                Transaction transaction = new Transaction(transactionId, transactionCategory, transactionName,
                        transactionAmount, transactionDate, transactionType);
                transactions.add(transaction);
            }

            return transactions;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            // safely disconnect from the api
            if(httpConn != null)
                httpConn.disconnect();
        }
    }

    // POST requests
    public static String loginUser(String email, String password){
        HttpURLConnection httpConn = null;
        try {
            // call on the spring user api to get user by email
            httpConn = ApiHandler.fetchApiResponse(
                    "/api/users/login?email=" + email + "&password=" + password,
                    ApiHandler.RequestMethod.POST, null);

            // failed to login
            if(httpConn != null && httpConn.getResponseCode() != 200){
                return null;
            }

            // login success, switch to dashboard view
            String apiResults = ApiHandler.readApiResponse(httpConn);
            return apiResults;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            // safely disconnect from the api
            if(httpConn != null)
                httpConn.disconnect();
        }
    }

    public static boolean postUser(JsonObject userData){
        HttpURLConnection httpConn = null;
        try {
            // call on the spring user api to create the user
            httpConn = ApiHandler.fetchApiResponse("/api/users",
                    ApiHandler.RequestMethod.POST, userData);

            // failed to create user
            if(httpConn != null && httpConn.getResponseCode() != 204){
                System.out.println("Error: " + httpConn.getResponseCode());
                return false;
            }

            // successfully created user
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            // safely disconnect from the api
            if(httpConn != null)
                httpConn.disconnect();
        }
    }

    public static boolean postTransaction(JsonObject transactionData){
        HttpURLConnection httpConn = null;
        try{
            httpConn = ApiHandler.fetchApiResponse(
                    "/api/transactions",
                    ApiHandler.RequestMethod.POST,
                    transactionData
            );

            if(httpConn != null && httpConn.getResponseCode() != 204){
                System.out.println("Getting Error: " + httpConn.getResponseCode());
                return false;
            }

            // post successful
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(httpConn != null){
                httpConn.disconnect();
            }
        }

        // post failed
        return false;
    }

    public static boolean postTransactionCategory(JsonObject transactionCategoryData){
        HttpURLConnection httpConn = null;
        try{
            httpConn = ApiHandler.fetchApiResponse(
                    "/api/transaction-categories", ApiHandler.RequestMethod.POST, transactionCategoryData
            );

            if(httpConn != null && httpConn.getResponseCode() != 204){
                return false;
            }

            return true;
        }catch(IOException e){
            e.printStackTrace();
        }

        return false;
    }

    // PUT requests
    public static void updateTransactionCategory(int categoryId, String newCategoryName,
                                                    String newCategoryColor){
        HttpURLConnection httpConn = null;
        try {
            // different example
            httpConn = ApiHandler.fetchApiResponse(
                    "/api/transaction-categories/" + categoryId +
                            "?newCategoryName=" + newCategoryName + "&newCategoryColor=" + newCategoryColor,
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

    public static boolean updateTransaction(JsonObject transactionData){
        HttpURLConnection httpConn = null;
        try{
            httpConn = ApiHandler.fetchApiResponse(
                    "/api/transactions",
                    ApiHandler.RequestMethod.PUT,
                    transactionData
            );

            if(httpConn != null && httpConn.getResponseCode() != 200){
                System.out.println("Getting Error: " + httpConn.getResponseCode());
                return false;
            }

            // update successful
            return true;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(httpConn != null){
                httpConn.disconnect();
            }
        }

        return false;
    }

    // DELETE requests
    public static boolean deleteTransactionCategoryById(int transactionCategoryId){
        HttpURLConnection httpConn = null;
        try{
            httpConn = ApiHandler.fetchApiResponse(
                    "/api/transaction-categories/" + transactionCategoryId,
                    ApiHandler.RequestMethod.DELETE,
                    null
            );

            if(httpConn != null && httpConn.getResponseCode() != 204){
                System.out.println("Deleting Error: " + httpConn.getResponseCode());
                return false;
            }

            return true;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(httpConn != null){
                httpConn.disconnect();
            }
        }

        return false;
    }

    public static boolean deleteTransactionById(int transactionId){
        HttpURLConnection httpConn = null;
        try{
            httpConn = ApiHandler.fetchApiResponse(
                    "/api/transactions/" + transactionId,
                    ApiHandler.RequestMethod.DELETE,
                    null
            );

            if(httpConn != null && httpConn.getResponseCode() != 204){
                System.out.println("Getting Error: " + httpConn.getResponseCode());
                return false;
            }

            return true;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(httpConn != null){
                httpConn.disconnect();
            }
        }

        return false;
    }
}

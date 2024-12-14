package org.example.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.example.dialogs.CreateNewCategoryDialog;
import org.example.dialogs.CreateNewTransactionDialog;
import org.example.dialogs.ViewAndEditCategoryDialog;
import org.example.models.User;
import org.example.utils.ApiHandler;
import org.example.views.DashboardView;
import org.example.views.LoginView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;

public class DashboardController {
    private DashboardView dashboardView;
    private User user;

    public DashboardController(DashboardView dashboardView){
        this.dashboardView = dashboardView;
        fetchUserData();

        addMenuActions();
        addContentActions();
    }

    // todo move this to sqlutil
    private void fetchUserData(){
        System.out.println("Fetching User Data");
        // fetch user data
        HttpURLConnection httpConn = null;
        try {
            // call on the spring user api to get user by email
            httpConn = ApiHandler.fetchApiResponse("/api/users?email=" + dashboardView.getEmail(),
                    ApiHandler.RequestMethod.GET, null);

            if(httpConn != null && httpConn.getResponseCode() != 200){
                System.out.println("Error: " + httpConn.getResponseCode());
                return;
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
            user = new User(id, name, email, password, createdAt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            // safely disconnect from the api
            if(httpConn != null)
                httpConn.disconnect();
        }
    }

    private void addMenuActions(){
        dashboardView.getCreateCategoryMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new CreateNewCategoryDialog(user).showAndWait();
            }
        });

        dashboardView.getViewCategoriesMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new ViewAndEditCategoryDialog(user).showAndWait();
            }
        });

        dashboardView.getLogoutMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new LoginView().show();
            }
        });
    }

    private void addContentActions(){
        dashboardView.getAddTransactionButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // launch create transaction dialog
                new CreateNewTransactionDialog(user).showAndWait();
            }
        });
    }
}

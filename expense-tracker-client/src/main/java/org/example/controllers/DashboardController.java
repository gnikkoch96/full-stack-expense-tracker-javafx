package org.example.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.models.User;
import org.example.utils.ApiHandler;
import org.example.utils.ViewNavigator;
import org.example.views.DashboardView;

import java.io.IOException;
import java.net.HttpURLConnection;

public class DashboardController {
    private DashboardView dashboardView;
    private User user;

    public DashboardController(DashboardView dashboardView){
        this.dashboardView = dashboardView;
        fetchUserData();
    }

    private void fetchUserData(){
        // fetch user data
        HttpURLConnection httpConn = null;
        try {
            // call on the spring user api to get user by email
            httpConn = ApiHandler.fetchApiResponse("/api/users?email=" + dashboardView.getEmail(),
                    ApiHandler.RequestMethod.GET, null);

            if(httpConn != null && httpConn.getResponseCode() != 200){
                return;
            }

            String userDataJson = ApiHandler.readApiResponse(httpConn);

            // convert string to jsonobject
            JsonObject jsonObject = JsonParser.parseString(userDataJson).getAsJsonObject();

            // create user
            String name = jsonObject.get("name").getAsString();
            String email = jsonObject.get("email").getAsString();
            String password = jsonObject.get("password").getAsString();
            user = new User(name, email, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            // safely disconnect from the api
            if(httpConn != null)
                httpConn.disconnect();
        }
    }
}

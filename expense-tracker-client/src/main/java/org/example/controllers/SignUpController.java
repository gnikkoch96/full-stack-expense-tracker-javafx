package org.example.controllers;

import com.google.gson.JsonObject;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import org.example.utils.ApiHandler;
import org.example.utils.SqlUtil;
import org.example.views.LoginView;
import org.example.views.SignUpView;

import java.io.IOException;
import java.net.HttpURLConnection;

public class SignUpController {
    private SignUpView signUpView;

    public SignUpController(SignUpView signUpView){
        this.signUpView = signUpView;
        initialize();
    }

    private void initialize(){
        signUpView.getRegisterButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!validateInput()) return;

                JsonObject jsonData = getJsonData();

                // create user to database
                boolean createAccountStatus = SqlUtil.postUser(jsonData);

                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setContentText(createAccountStatus ? "Success: Account Created!" : "Error: Failed to Create Account...");
                infoAlert.showAndWait();
            }
        });

        // change to login view
        signUpView.getLoginLabel().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // switch view to login view
                new LoginView().show();
            }
        });
    }

    // retrieve user info to store into database
    private JsonObject getJsonData() {
        String name = signUpView.getNameField().getText();
        String email = signUpView.getUsernameField().getText();
        String password = signUpView.getPasswordField().getText();

        // Note: the property names must match that of the fields in the entity class not the sql tables
        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("name", name);
        jsonData.addProperty("email", email);
        jsonData.addProperty("password", password);
        return jsonData;
    }

    // validates the user input
    private boolean validateInput(){
        // missing email
        if(signUpView.getUsernameField().getText().isEmpty()){
            return false;
        }

        // email already exists
        HttpURLConnection httpConn = null;
        try {
            // call on the spring user api to get user by email
            httpConn = ApiHandler.fetchApiResponse("/api/users?email=" + signUpView.getUsernameField().getText(),
                    ApiHandler.RequestMethod.GET, null);

            if(httpConn != null && httpConn.getResponseCode() == 200){
                System.out.println("Error:" + httpConn.getResponseCode());
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            // safely disconnect from the api
            if(httpConn != null)
                httpConn.disconnect();
        }

        // missing name
        if(signUpView.getNameField().getText().isEmpty()){
            return false;
        }

        // missing password
        if(signUpView.getPasswordField().getText().isEmpty()){
            return false;
        }

        // missing re password
        if(signUpView.getRePasswordField().getText().isEmpty()){
            return false;
        }

        // retyped password doesn't match
        if(!signUpView.getRePasswordField().getText().equals(signUpView.getPasswordField().getText())){
            return false;
        }

        return true;
    }
}

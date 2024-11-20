package org.example.controllers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.example.utils.ApiHandler;
import org.example.utils.ViewNavigator;
import org.example.views.DashboardView;
import org.example.views.LoginView;
import org.example.views.SignUpView;

import java.io.IOException;
import java.net.HttpURLConnection;

public class LoginController {
    private LoginView loginView;

    public LoginController(LoginView loginView){
        this.loginView = loginView;
        initialize();
    }

    private void initialize(){
        loginView.getLoginButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!validateInput()) return;

                // store email and password
                String email = loginView.getUsernameField().getText();
                String password = loginView.getPasswordField().getText();

                // validate login
                HttpURLConnection httpConn = null;
                try {
                    // call on the spring user api to get user by email
                    httpConn = ApiHandler.fetchApiResponse(
                            "/api/users/login?email=" + email + "&password=" + password,
                            ApiHandler.RequestMethod.POST, null);

                    // failed to login
                    if(httpConn != null && httpConn.getResponseCode() != 200){
                        return;
                    }

                    // login success, switch to dashboard view
                    String apiResults = ApiHandler.readApiResponse(httpConn);
                    new DashboardView(apiResults).show();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }finally {
                    // safely disconnect from the api
                    if(httpConn != null)
                        httpConn.disconnect();
                }
            }
        });

        loginView.getSignUpLabel().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // switch to the sign up view
                new SignUpView().show();
            }
        });
    }

    private boolean validateInput(){
        // empty username
        if(loginView.getUsernameField().getText().isEmpty()){
            return false;
        }

        // empty password
        if(loginView.getPasswordField().getText().isEmpty()){
            return false;
        }

        return true;
    }
}

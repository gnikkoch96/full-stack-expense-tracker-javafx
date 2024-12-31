package org.example.controllers;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import org.example.utils.SqlUtil;
import org.example.views.DashboardView;
import org.example.views.LoginView;
import org.example.views.SignUpView;

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
                String results = SqlUtil.loginUser(email, password);

                Alert alert;
                if(results == null){
                    // login failed
                    alert = new Alert(Alert.AlertType.ERROR);
                }else{
                    // login successful (switch to dashboard)
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    new DashboardView(results).show();
                }

                alert.setContentText(results == null ? "Error: Invalid Credentials" : "Success: Login Successfully!");
                alert.showAndWait();
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

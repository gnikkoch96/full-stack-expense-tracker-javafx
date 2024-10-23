package org.example.controllers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.example.utils.ViewNavigator;
import org.example.views.LoginView;
import org.example.views.SignUpView;

public class LoginController {
    private LoginView loginView;

    public LoginController(LoginView loginView){
        this.loginView = loginView;
        initialize();
    }

    private void initialize(){
        loginView.getSignUpLabel().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // switch to the sign up view
                new SignUpView().show();
            }
        });
    }
}

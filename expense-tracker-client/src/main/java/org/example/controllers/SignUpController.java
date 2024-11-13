package org.example.controllers;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import org.example.views.LoginView;
import org.example.views.SignUpView;

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

                // retrieve user info to store into database
                String name = signUpView.getNameField().getText();
                String email = signUpView.getUsernameField().getText();
                String password = signUpView.getPasswordField().getText();

                // call on the spring user api to create the user

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

    // validates the user input
    private boolean validateInput(){
        // missing email
        if(signUpView.getUsernameField().getText().isEmpty()){
            return false;
        }

        // email already exists

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

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
        signUpView.getLoginLabel().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // switch view to login view
                new LoginView().show();
            }
        });
    }
}

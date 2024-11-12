package org.example.views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.controllers.LoginController;
import org.example.utils.Config;
import org.example.utils.ViewNavigator;

public class LoginView implements View{
    private Label expenseTrackerLabel = new Label("Expense Tracker");
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Button loginButton = new Button("Login");
    private Label signUpLabel = new Label("Don't have an account? Click Here");

    @Override
    public void show() {
        Scene scene = createScene();
        scene.getStylesheets().add(getClass().getResource(
                "/style.css"
        ).toExternalForm());

        new LoginController(this);
        ViewNavigator.switchViews(scene);
    }

    private Scene createScene(){
        // layout
        VBox vBox = new VBox(74);
        vBox.getStyleClass().addAll("main-background");
        vBox.setAlignment(Pos.TOP_CENTER);

        expenseTrackerLabel.getStyleClass().addAll("header", "text-white");
        VBox loginForm = createLoginForm();

        // optional: prevents fields from being focused so that you can see input prompts
        for(Node node : loginForm.getChildren()){
            node.setFocusTraversable(false);
        }

        vBox.getChildren().addAll(expenseTrackerLabel, loginForm);
        return new Scene(vBox, Config.APP_WIDTH, Config.APP_HEIGHT);
    }

    private VBox createLoginForm(){
        VBox loginForm = new VBox(51);
        loginForm.setAlignment(Pos.CENTER);

        usernameField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-lg",
                                                        "rounded-border");
        usernameField.setPromptText("Enter Email");
        usernameField.setMaxWidth(473); // px

        passwordField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-lg",
                                                        "rounded-border");
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(473); // px

        loginButton.getStyleClass().addAll("text-size-lg", "bg-light-blue", "text-white", "text-weight-700",
                                                        "rounded-border");
        loginButton.setMaxWidth(473); // px

        signUpLabel.getStyleClass().addAll("link-text", "text-size-md", "text-light-gray", "text-underline");

        loginForm.getChildren().addAll(usernameField, passwordField, loginButton, signUpLabel);
        return loginForm;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Label getSignUpLabel() {
        return signUpLabel;
    }
}












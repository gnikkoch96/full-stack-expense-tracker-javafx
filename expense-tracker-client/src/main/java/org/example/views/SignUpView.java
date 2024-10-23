package org.example.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.controllers.SignUpController;
import org.example.utils.Config;
import org.example.utils.ViewNavigator;

public class SignUpView implements View{
    private Label expenseTrackerLabel = new Label("Expense Tracker");

    private TextField nameField = new TextField();
    private TextField usernameField = new TextField();


    private PasswordField passwordField = new PasswordField();
    private PasswordField rePasswordField = new PasswordField();

    private Button registerButton = new Button("Register");
    private Label loginLabel = new Label("Already have an account? Login here");

    @Override
    public void show() {
        Scene scene = createScene();
        scene.getStylesheets().add(getClass().getResource(
                "/style.css"
        ).toExternalForm());

        new SignUpController(this);
        ViewNavigator.switchViews(scene);
    }

    private Scene createScene(){
        VBox vBox = new VBox(44);
        vBox.getStyleClass().addAll("main-background");
        vBox.setAlignment(Pos.TOP_CENTER);

        expenseTrackerLabel.getStyleClass().addAll("header", "text-white");

        VBox signupForm = createSignUpForm();

        vBox.getChildren().addAll(expenseTrackerLabel, signupForm);
        return new Scene(vBox, Config.APP_WIDTH, Config.APP_HEIGHT);
    }

    private VBox createSignUpForm(){
        VBox signupForm = new VBox(30);
        signupForm.setAlignment(Pos.CENTER);

        nameField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-lg",
                                                "rounded-border");
        nameField.setPromptText("Enter Name");
        nameField.setMaxWidth(473); // px

        usernameField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-lg",
                "rounded-border");
        usernameField.setPromptText("Enter Email");
        usernameField.setMaxWidth(473); // px

        passwordField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-lg",
                "rounded-border");
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(473); // px

        rePasswordField.getStyleClass().addAll("field-background", "text-light-gray", "text-size-lg",
                "rounded-border");
        rePasswordField.setPromptText("Re-Enter Password");
        rePasswordField.setMaxWidth(473); // px

        registerButton.getStyleClass().addAll("text-size-lg", "bg-light-blue", "text-white", "text-weight-700",
                                                        "rounded-border");
        registerButton.setMaxWidth(473); // px

        loginLabel.getStyleClass().addAll("link-text", "text-size-md", "text-light-gray", "text-underline");

        signupForm.getChildren().addAll(nameField, usernameField, passwordField, rePasswordField, registerButton, loginLabel);
        return signupForm;
    }

    public Label getExpenseTrackerLabel() {
        return expenseTrackerLabel;
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public PasswordField getRePasswordField() {
        return rePasswordField;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Label getLoginLabel() {
        return loginLabel;
    }
}












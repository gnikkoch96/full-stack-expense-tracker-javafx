package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.utils.ViewNavigator;
import org.example.views.DashboardView;
import org.example.views.LoginView;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Expense Tracker");
        ViewNavigator.setMainStage(stage);

        // the login view will be the first to be shown when the app is ran
        LoginView loginView = new LoginView();
        loginView.show();

//        new DashboardView("email@email.com").show();
    }
}

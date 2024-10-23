package org.example.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * ViewNavigator is a utility class responsible for managing the navigation
 * between different scenes within the same primary stage (window) of a JavaFX application.
 * It provides methods to set the main stage and switch between different views (scenes).
 */
public class ViewNavigator {

    // The primary stage (window) of the application, where scenes will be displayed.
    private static Stage mainStage;

    /**
     * Sets the primary stage of the application.
     * This method should be called once at the start of the application to initialize the main stage.
     *
     * @param stage The primary stage provided by the JavaFX application (usually in the start() method).
     */
    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    /**
     * Switches the currently displayed scene on the main stage to the specified scene.
     * If the main stage has been initialized, this method will replace the current scene
     * with the new one and display it.
     *
     * @param scene The new scene to be displayed on the main stage.
     */
    public static void switchViews(Scene scene) {
        if (mainStage != null) {
            mainStage.setScene(scene);
            mainStage.show();
        }
    }
}


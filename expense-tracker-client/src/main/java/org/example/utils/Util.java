package org.example.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;

public class Util {
    public static int APP_WIDTH = 1614;
    public static int APP_HEIGHT = 833;

    // Note: the reason why we removed two is because ColorPicker returns a hex that includes the alpha values which
    // we don't want. They are the last two characters so we substring them out before storing them into the database.
    // we also don't want to include the first 2 characters which are the "0x"
    public static String getHexColorValue(ColorPicker colorPicker){
        String color = colorPicker.getValue().toString();
        return color.substring(2, color.length() - 2);
    }

    public static void showAlertDialog(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

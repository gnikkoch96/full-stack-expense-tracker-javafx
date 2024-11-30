package org.example.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.example.models.User;

// Note: This class will be used to organize our Dialog code better
// we are going to put all the code that is common to all of our dialogs here to prevent repeating ourselves
public class CustomDialog extends Dialog<String> {
    protected User user;
    protected Alert infoAlert, errorAlert;

    public CustomDialog(User user){
        this.user = user;
        infoAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert = new Alert(Alert.AlertType.ERROR);

        // all dialogs will have this background
        getDialogPane().getStylesheets().add(getClass().getResource(
                "/style.css"
        ).toExternalForm());
        getDialogPane().getStyleClass().addAll("main-background");

        // Note: we need to add a buttontype to be able to close the dialog
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setVisible(false);
        okButton.setDisable(false);
    }
}

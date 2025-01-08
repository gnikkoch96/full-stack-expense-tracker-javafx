package org.example.animations;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LoadingAnimationPane extends Pane {
    private final Rectangle rectangle;
    private final Label loadingLabel;

    public LoadingAnimationPane(double screenWidth, double screenHeight){
        rectangle = new Rectangle(screenWidth, screenHeight, Color.BLACK);
        rectangle.setOpacity(0.5);

        loadingLabel = new Label("Loading...");
        loadingLabel.getStyleClass().addAll("text-size-lg", "text-white");
        loadingLabel.setLayoutX(screenWidth/2 - 70);
        loadingLabel.setLayoutY(screenHeight/2 - 10);

        setMinSize(screenWidth, screenHeight);

        getChildren().addAll(rectangle, loadingLabel);
        setVisible(false);
        setManaged(false);
    }

    // these methods are so that we can change the width of the UI nodes when the scene's size changes (makes it more responsive)
    public void resizeWidth(double newWidth){
        rectangle.setWidth(newWidth);
        loadingLabel.setLayoutX(newWidth/2 - 70);
        setMinWidth(newWidth);
    }


    // these methods are so that we can change the height of the UI nodes when the scene's size changes (makes it more responsive)
    public void resizeHeight(double newHeight){
        rectangle.setHeight(newHeight);
        loadingLabel.setLayoutY(newHeight/2 - 70);
        setMinHeight(newHeight);
    }
}

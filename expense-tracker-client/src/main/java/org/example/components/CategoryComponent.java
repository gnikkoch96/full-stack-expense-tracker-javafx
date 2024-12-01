package org.example.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

// Note: similar to react components, we will create components like this for better organization
// we will use this to create a component for each category that we retrieve
public class CategoryComponent extends HBox{
    private Label categoryLabel;
    private ColorPicker colorPicker;
    private Button editButton;
    private Button saveButton;
    private Button deleteButton;

    // flags

    public CategoryComponent(String categoryText, String hexColorCode){
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().addAll("view-category-component-padding", "rounded-border", "field-background");

        categoryLabel = new Label(categoryText);
        categoryLabel.setMinWidth(150);
        categoryLabel.getStyleClass().addAll("view-category-margin", "text-size-md", "text-light-gray");

        colorPicker = new ColorPicker();
        colorPicker.setDisable(true);
        colorPicker.setValue(Color.valueOf(hexColorCode));
        colorPicker.getStyleClass().addAll("text-size-sm");

        editButton = new Button("Edit");
        editButton.getStyleClass().addAll("text-size-sm");

        // is hidden when edit button is shown
        saveButton = new Button("Save");
        saveButton.setVisible(false);
        saveButton.setDisable(true);
        saveButton.setMinWidth(0);
        saveButton.getStyleClass().addAll("text-size-sm");

        deleteButton = new Button("Del");
        deleteButton.getStyleClass().addAll("bg-light-red", "text-white", "text-size-sm");

        getChildren().addAll(categoryLabel, colorPicker, editButton, deleteButton);
    }
}

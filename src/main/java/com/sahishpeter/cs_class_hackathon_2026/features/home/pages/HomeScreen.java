package com.sahishpeter.cs_class_hackathon_2026.features.home.pages;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HomeScreen extends VBox {

    public HomeScreen() {

        setAlignment(Pos.CENTER);
        getStyleClass().add("main");

        Label welcomeText = new Label();

        Button button = new Button("Click me.");
        button.getStyleClass().add("test");
        button.setOnAction(e -> welcomeText.setText("Hello World"));

        getChildren().addAll(welcomeText, button);

    }

}
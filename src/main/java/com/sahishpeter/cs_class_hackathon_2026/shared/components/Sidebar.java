package com.sahishpeter.cs_class_hackathon_2026.shared.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    public Sidebar() {
        
        setAlignment(Pos.CENTER);
        setMaxWidth(200);
        getStyleClass().add("sidebar");

        getChildren().add(new Label("Hi"));

    }

}
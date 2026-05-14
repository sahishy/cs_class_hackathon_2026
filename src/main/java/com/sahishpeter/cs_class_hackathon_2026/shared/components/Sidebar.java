package com.sahishpeter.cs_class_hackathon_2026.shared.components;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    public Sidebar() {
        
        setMaxWidth(256);
        getStyleClass().add("sidebar");

        Label label = new Label("Home");
        label.getStyleClass().add("sidebar-tab");
        label.setMaxWidth(Double.MAX_VALUE);

        getChildren().addAll(label);

    }

}
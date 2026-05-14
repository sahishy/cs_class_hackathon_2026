package com.sahishpeter.cs_class_hackathon_2026.features.home.components;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class QuestionInput extends VBox {

    public QuestionInput() {

        setAlignment(Pos.CENTER);
        setPrefHeight(256);
        getStyleClass().add("question-input");

        Label greetingLabel = new Label("Hey, Sahish!");
        greetingLabel.getStyleClass().add("h1");

        getChildren().addAll(greetingLabel);

    }

}

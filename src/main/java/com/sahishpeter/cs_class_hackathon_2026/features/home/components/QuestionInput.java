package com.sahishpeter.cs_class_hackathon_2026.features.home.components;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class QuestionInput extends VBox {

    public QuestionInput() {

        setAlignment(Pos.CENTER);
        setPrefHeight(256);
        setSpacing(24);

        Label greetingLabel = new Label("Hey, Sahish!");
        greetingLabel.getStyleClass().add("h1");
        
        TextField textField = new TextField();
        textField.getStyleClass().add("question-input");
        textField.setMaxWidth(400);

        getChildren().addAll(greetingLabel, textField);

    }

}

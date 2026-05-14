package com.sahishpeter.cs_class_hackathon_2026.features.home.components;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import com.sahishpeter.cs_class_hackathon_2026.shared.components.Card;

public class LessonCard extends VBox {
    
    public LessonCard() {

        Card card = new Card();
        card.setPrefHeight(256);

        Label label = new Label("hello");

        card.getChildren().add(label);

        getChildren().add(card);

    }

}

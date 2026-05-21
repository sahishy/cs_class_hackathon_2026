package com.sahishpeter.cs_class_hackathon_2026.features.home.components;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.shared.components.Card;

public class LessonCard extends VBox {
    
    public LessonCard(Lesson lesson) {

        Card card = new Card();
        card.setPrefHeight(256);

        VBox thumbnailHolder = new VBox();
        thumbnailHolder.setFillWidth(true);
        thumbnailHolder.setPrefHeight(128);
        thumbnailHolder.getStyleClass().add("lesson-thumbnail");

        VBox infoHolder = new VBox();
        infoHolder.getStyleClass().add("lesson-info");
        Label label = new Label(lesson.title());
        label.getStyleClass().add("h3");
        infoHolder.getChildren().add(label);

        card.getChildren().addAll(thumbnailHolder, infoHolder);

        getChildren().add(card);

    }

}

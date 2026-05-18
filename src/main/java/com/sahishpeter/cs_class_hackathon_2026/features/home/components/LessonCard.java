package com.sahishpeter.cs_class_hackathon_2026.features.home.components;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.shared.components.Card;

public class LessonCard extends VBox {
    
    public LessonCard(Lesson lesson) {

        Card card = new Card();
        card.setPrefHeight(256);

        VBox thumbnail = new VBox();
        thumbnail.setFillWidth(true);

        Label label = new Label(lesson.title());

        card.getChildren().addAll(thumbnail, label);

        getChildren().add(card);

    }

}

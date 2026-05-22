package com.sahishpeter.cs_class_hackathon_2026.features.home.components;

import java.util.function.Consumer;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.Cursor;

import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.shared.components.Card;

public class LessonCard extends VBox {
    
    public LessonCard(Lesson lesson, Consumer<Lesson> onOpenLesson) {

        Card card = new Card();
        card.setPrefHeight(256);
        card.getStyleClass().add("lesson-card");
        card.setCursor(Cursor.HAND);
        card.setOnMouseClicked(event -> onOpenLesson.accept(lesson));

        VBox thumbnailHolder = new VBox();
        thumbnailHolder.setFillWidth(true);
        thumbnailHolder.setPrefHeight(128);
        thumbnailHolder.getStyleClass().add("lesson-thumbnail");

        VBox infoHolder = new VBox();
        infoHolder.getStyleClass().add("lesson-info");
        String cardTitle = (lesson.lessonTitle() != null && !lesson.lessonTitle().isBlank()) ? lesson.lessonTitle() : lesson.title();
        Label label = new Label(cardTitle == null || cardTitle.isBlank() ? "Untitled Lesson" : cardTitle);
        label.getStyleClass().add("h3");
        infoHolder.getChildren().add(label);

        card.getChildren().addAll(thumbnailHolder, infoHolder);

        getChildren().add(card);

    }

}

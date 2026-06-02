package com.sahishpeter.cs_class_hackathon_2026.features.home.components;

import java.util.function.Consumer;

import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.features.math.components.Graph;
import com.sahishpeter.cs_class_hackathon_2026.shared.components.Card;
import com.sahishpeter.cs_class_hackathon_2026.shared.utils.Formatters;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LessonCard extends VBox {

    public LessonCard(Lesson lesson, Consumer<Lesson> onOpenLesson) {

        Card card = new Card();
        card.setPrefHeight(256);
        card.getStyleClass().addAll("hoverable", "lesson-card");
        card.setOnMouseClicked(event -> onOpenLesson.accept(lesson));

        VBox thumbnailHolder = new VBox();
        thumbnailHolder.setFillWidth(true);
        thumbnailHolder.setPrefHeight(128);
        thumbnailHolder.getStyleClass().add("lesson-thumbnail");

        Rectangle clipRectangle = new Rectangle();
        clipRectangle.widthProperty().bind(thumbnailHolder.widthProperty());
        clipRectangle.heightProperty().bind(thumbnailHolder.heightProperty());
        clipRectangle.setFill(Color.RED);
        thumbnailHolder.setClip(clipRectangle);

        Graph graph = new Graph(lesson.thumbnailGraph(), 200, false);
        thumbnailHolder.getChildren().add(graph);

        String title = lesson.title();
        String topic = lesson.topic().toUpperCase();
        String updatedAt = Formatters.formatTimeAgo(lesson.updatedAt());

        VBox infoHolder = new VBox();
        infoHolder.getStyleClass().add("lesson-info");
        VBox.setVgrow(infoHolder, Priority.ALWAYS);

        VBox topHolder = new VBox();
        topHolder.setSpacing(4);

        Label headerLabel = new Label(topic);
        headerLabel.getStyleClass().add("h4");
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("h3");

        topHolder.getChildren().addAll(headerLabel, titleLabel);

        HBox bottomHolder = new HBox();
        bottomHolder.setAlignment(Pos.BOTTOM_RIGHT);
        VBox.setVgrow(bottomHolder, Priority.ALWAYS);

        Label updatedAtLabel = new Label(updatedAt);
        updatedAtLabel.getStyleClass().add("p");

        bottomHolder.getChildren().addAll(updatedAtLabel);

        infoHolder.getChildren().addAll(topHolder, bottomHolder);
        card.getChildren().addAll(thumbnailHolder, infoHolder);

        getChildren().add(card);

    }

}

package com.sahishpeter.cs_class_hackathon_2026.features.home.components;

import com.sahishpeter.cs_class_hackathon_2026.shared.components.Card;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LessonCardSkeleton extends VBox {

    public LessonCardSkeleton() {

        Card card = new Card();
        card.setPrefHeight(256);

        VBox thumbnailHolder = new VBox();
        thumbnailHolder.setFillWidth(true);
        thumbnailHolder.setPrefHeight(128);
        thumbnailHolder.getStyleClass().addAll("lesson-thumbnail", "skeleton-block");

        VBox infoHolder = new VBox();
        infoHolder.getStyleClass().addAll("lesson-info");
        VBox.setVgrow(infoHolder, Priority.ALWAYS);

        VBox topHolder = new VBox();
        topHolder.setSpacing(8);

        Region topicLine = new Region();
        topicLine.getStyleClass().addAll("skeleton-block", "skeleton-line", "short");

        Region titleLine = new Region();
        titleLine.getStyleClass().addAll("skeleton-block", "skeleton-line", "medium");

        topHolder.getChildren().addAll(topicLine, titleLine);

        HBox bottomHolder = new HBox();
        bottomHolder.setAlignment(Pos.BOTTOM_RIGHT);
        VBox.setVgrow(bottomHolder, Priority.ALWAYS);

        Region timeLine = new Region();
        timeLine.getStyleClass().addAll("skeleton-block", "skeleton-line", "short");
        bottomHolder.getChildren().add(timeLine);

        infoHolder.getChildren().addAll(topHolder, bottomHolder);
        card.getChildren().addAll(thumbnailHolder, infoHolder);

        getChildren().add(card);

    }

}
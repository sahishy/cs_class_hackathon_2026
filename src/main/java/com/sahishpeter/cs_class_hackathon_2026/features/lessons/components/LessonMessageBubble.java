package com.sahishpeter.cs_class_hackathon_2026.features.lessons.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

public class LessonMessageBubble extends HBox {

    public LessonMessageBubble(String sender, String text) {

        boolean isUser = "user".equals(sender);

        getStyleClass().add("message-row");
        setMaxWidth(Double.MAX_VALUE);
        setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        Pane holder = new Pane();

        Label bubble = new Label(text);
        bubble.setWrapText(true);
        bubble.getStyleClass().addAll("message", sender);

        SVGPath swoosh = new SVGPath();
        swoosh.setContent("M 0 18 C 16 15, 22 8, 26 0 L 8 0 C 12 6, 8 13, 0 18 Z");
        swoosh.getStyleClass().addAll("message-swoosh", sender);
        swoosh.setManaged(false);
        if (isUser) {
            swoosh.setScaleX(-1);
        }

        holder.getChildren().addAll(bubble, swoosh);
        swoosh.toBack();
        bubble.toFront();

        double tailInset = 8;
        Runnable positionSwoosh = () -> {

            double bubbleWidth = bubble.getLayoutBounds().getWidth();
            double bubbleHeight = bubble.getLayoutBounds().getHeight();
            double swooshHeight = swoosh.getLayoutBounds().getHeight();
            double aiRightNudge = 10;
            double userLeftNudge = 18;

            double x = isUser
                ? bubbleWidth - tailInset - userLeftNudge
                : -tailInset + aiRightNudge;
            double y = bubbleHeight - (swooshHeight * 0.8);
            swoosh.relocate(x, y);

        };
        bubble.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> positionSwoosh.run());
        positionSwoosh.run();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if (isUser) {
            getChildren().addAll(spacer, holder);
        } else {
            getChildren().addAll(holder, spacer);
        }

    }

}
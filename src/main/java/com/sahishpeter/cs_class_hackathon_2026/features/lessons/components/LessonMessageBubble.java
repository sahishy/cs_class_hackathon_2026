package com.sahishpeter.cs_class_hackathon_2026.features.lessons.components;

import com.sahishpeter.cs_class_hackathon_2026.features.math.components.LatexRenderer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class LessonMessageBubble extends HBox {

    public LessonMessageBubble(String sender, String text, String latex) {

        boolean isUser = "user".equals(sender);

        getStyleClass().add("message-row");
        setMaxWidth(Double.MAX_VALUE);
        setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        StackPane holder = new StackPane();
        holder.setMinWidth(0);

        VBox bubble = new VBox();
        bubble.setMinWidth(0);
        bubble.setSpacing(16);
        bubble.maxWidthProperty().bind(widthProperty().multiply(0.8));
        bubble.getStyleClass().addAll("message", sender);

        Label textLabel = new Label(text == null ? "" : text);
        textLabel.setWrapText(true);
        textLabel.setMinWidth(0);
        textLabel.setMaxWidth(Double.MAX_VALUE);
        textLabel.getStyleClass().addAll("message-text", sender);
        bubble.getChildren().add(textLabel);

        if (!isUser && latex != null && !latex.isBlank()) {
            bubble.getChildren().add(LatexRenderer.render(latex, 18f));
        }

        SVGPath swoosh = generateSwoosh(sender, isUser);

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

    private SVGPath generateSwoosh(String sender, boolean isUser) {

        SVGPath swoosh = new SVGPath();
        swoosh.setContent("M 0 18 C 16 15, 22 8, 26 0 L 8 0 C 12 6, 8 13, 0 18 Z");
        swoosh.getStyleClass().addAll("message-swoosh", sender);
        swoosh.setManaged(false);

        if (isUser) {
            swoosh.setScaleX(-1);
        }

        return swoosh;
    }

}
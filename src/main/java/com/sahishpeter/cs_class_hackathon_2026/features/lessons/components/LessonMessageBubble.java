package com.sahishpeter.cs_class_hackathon_2026.features.lessons.components;

import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.features.math.components.LatexRenderer;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class LessonMessageBubble extends HBox {

    public LessonMessageBubble(String sender, String title, List<Lesson.LessonContentBlock> content) {

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

        if (!isUser && title != null && !title.isBlank()) {
            Label titleLabel = new Label(title);
            titleLabel.setWrapText(true);
            titleLabel.setMinWidth(0);
            titleLabel.setMaxWidth(Double.MAX_VALUE);
            titleLabel.getStyleClass().addAll("message-title", "h3", sender);
            bubble.getChildren().add(titleLabel);
        }

        createMessageContent(bubble, sender, content, isUser);

        SVGPath swoosh = createSwoosh(sender, isUser);

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

    private void createMessageContent(VBox bubble, String sender, List<Lesson.LessonContentBlock> content, boolean isUser) {

        List<Lesson.LessonContentBlock> safeContent = content == null ? List.of() : content;

        for (Lesson.LessonContentBlock block : safeContent) {
            if (block == null || block.type() == null || block.value() == null || block.value().isBlank()) {
                continue;
            }

            String type = block.type().trim().toLowerCase();
            if (type.equals("latex") && !isUser) {
                bubble.getChildren().add(LatexRenderer.render(block.value(), 18f));
            } else {
                bubble.getChildren().add(createTextLabel(sender, block.value()));
            }
        }

        if (bubble.getChildren().isEmpty()) {
            bubble.getChildren().add(createTextLabel(sender, ""));
        }

    }

    private Label createTextLabel(String sender, String content) {

        Label textLabel = new Label(content == null ? "" : content);
        textLabel.setWrapText(true);
        textLabel.setMinWidth(0);
        textLabel.setMaxWidth(Double.MAX_VALUE);
        textLabel.getStyleClass().addAll("message-text", sender);
        return textLabel;

    }

    private SVGPath createSwoosh(String sender, boolean isUser) {

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
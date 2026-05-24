package com.sahishpeter.cs_class_hackathon_2026.features.lessons.components;

import com.sahishpeter.cs_class_hackathon_2026.features.math.components.LatexRenderer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class LessonMessageBubble extends HBox {

    private static final Pattern LATEX_MARKER_PATTERN = Pattern.compile("\\[\\[latex:(\\d+)\\]\\]");

    public LessonMessageBubble(String sender, String title, String text, List<String> latexSnippets) {

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

        createMessageContent(bubble, sender, text, latexSnippets, isUser);

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

    private void createMessageContent(VBox bubble, String sender, String text, List<String> latexSnippets, boolean isUser) {

        String safeText = text == null ? "" : text;
        List<String> safeLatexSnippets = latexSnippets == null ? List.of() : latexSnippets;

        if (isUser || safeLatexSnippets.isEmpty()) {
            bubble.getChildren().add(createTextLabel(sender, safeText));
            return;
        }

        Matcher matcher = LATEX_MARKER_PATTERN.matcher(safeText);
        int lastIndex = 0;

        while (matcher.find()) {

            if (matcher.start() > lastIndex) {
                bubble.getChildren().add(createTextLabel(sender, safeText.substring(lastIndex, matcher.start())));
            }

            int latexIndex = Integer.parseInt(matcher.group(1));
            if (latexIndex >= 0 && latexIndex < safeLatexSnippets.size()) {
                String snippet = safeLatexSnippets.get(latexIndex);
                if (snippet != null && !snippet.isBlank()) {
                    bubble.getChildren().add(LatexRenderer.render(snippet, 18f));
                }
            } else {
                bubble.getChildren().add(createTextLabel(sender, matcher.group()));
            }

            lastIndex = matcher.end();

        }

        if (lastIndex < safeText.length()) {
            bubble.getChildren().add(createTextLabel(sender, safeText.substring(lastIndex)));
        }

        if (bubble.getChildren().isEmpty()) {
            bubble.getChildren().add(createTextLabel(sender, safeText));
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
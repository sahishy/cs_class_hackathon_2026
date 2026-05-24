package com.sahishpeter.cs_class_hackathon_2026.features.lessons.pages;

import java.util.ArrayList;
import java.util.List;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.components.LessonMessageBubble;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.LessonMessage;
import com.sahishpeter.cs_class_hackathon_2026.shared.components.Card;
import com.sahishpeter.cs_class_hackathon_2026.shared.components.EdgeFadeOverlay;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LessonChat extends VBox {

    private final Lesson lesson;
    private final List<LessonMessage> messages = new ArrayList<>();
    private final VBox messagesContainer = new VBox();
    private final VBox messagesArea = new VBox();
    private int currentStep = 0;

    public LessonChat(Lesson lesson) {

        this.lesson = lesson;

        getStyleClass().add("lesson-left-pane");

        Card chatCard = new Card();
        VBox.setVgrow(chatCard, Priority.ALWAYS);

        configureMessagesArea();

        getMessagesFromCurrentStep();
        renderMessages();

        StackPane messagesWithTopFade = new StackPane();
        EdgeFadeOverlay messagesTopFade = new EdgeFadeOverlay(EdgeFadeOverlay.Direction.TOP);
        StackPane.setAlignment(messagesTopFade, Pos.TOP_CENTER);
        messagesWithTopFade.getChildren().addAll(
            messagesArea,
            messagesTopFade
        );

        BorderPane chatLayout = new BorderPane();
        chatLayout.setCenter(messagesWithTopFade);
        chatLayout.setBottom(buildInputDock());
        BorderPane.setMargin(messagesWithTopFade, new Insets(0, 0, 8, 0));

        chatCard.getChildren().add(chatLayout);
        VBox.setVgrow(chatLayout, Priority.ALWAYS);

        getChildren().add(chatCard);
        VBox.setVgrow(this, Priority.ALWAYS);

    }

    private void configureMessagesArea() {

        messagesContainer.getStyleClass().add("messages");
        messagesContainer.setSpacing(16);
        messagesContainer.setFillWidth(true);

        Region topSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);

        messagesArea.getChildren().addAll(topSpacer, messagesContainer);
        messagesArea.setFillWidth(true);
        VBox.setVgrow(messagesArea, Priority.ALWAYS);

    }

    private HBox buildInputDock() {

        HBox inputDock = new HBox(8);
        inputDock.getStyleClass().add("input-dock");
        inputDock.setAlignment(Pos.CENTER_LEFT);

        TextField input = new TextField();
        input.getStyleClass().add("input");
        input.setPromptText("Ask a question...");
        input.setFocusTraversable(false);

        Button sendButton = new Button("↑");
        sendButton.getStyleClass().addAll("button", "primary", "icon");

        Runnable send = () -> {
            String text = input.getText() == null ? "" : input.getText().trim();
            if (text.isBlank()) {
                return;
            }

            long now = System.currentTimeMillis();
            messages.add(new LessonMessage("user-" + now, "user", text, null, now));
            messages.add(new LessonMessage("ai-" + now, "ai", "AI response coming soon...", null, now + 1));
            input.clear();
            renderMessages();
        };

        sendButton.setOnAction(event -> send.run());
        input.setOnAction(event -> send.run());

        HBox.setHgrow(input, Priority.ALWAYS);
        inputDock.getChildren().addAll(input, sendButton);
        return inputDock;

    }

    public void setCurrentStep(int stepIndex) {

        currentStep = Math.max(0, stepIndex);
        getMessagesFromCurrentStep();
        renderMessages();

    }

    private void getMessagesFromCurrentStep() {

        messages.clear();

        List<Lesson.LessonStep> steps = lesson == null || lesson.steps() == null ? List.of() : lesson.steps();
        if (steps.isEmpty()) {

            long now = System.currentTimeMillis();

            messages.add(new LessonMessage("ai-intro", "ai", "Let's walk through this lesson together.", null, now));
            messages.add(new LessonMessage("ai-placeholder", "ai", "Lesson steps will appear here soon.", null, now + 1));

            return;

        }

        int stepIndex = Math.max(0, Math.min(currentStep, steps.size() - 1));
        Lesson.LessonStep step = steps.get(stepIndex);
        StringBuilder text = new StringBuilder();

        text.append(step.title() == null || step.title().isBlank() ? "Step " + (stepIndex + 1) : step.title());
        
        if (step.explanation() != null && !step.explanation().isBlank()) {
            text.append("\n\n").append(step.explanation());
        }

        long createdAt = lesson.createdAt() > 0 ? lesson.createdAt() : System.currentTimeMillis();
        messages.add(new LessonMessage("ai-step-" + stepIndex, "ai", text.toString(), step.latex(), createdAt + stepIndex));

    }

    private void renderMessages() {

        messagesContainer.getChildren().clear();

        for (LessonMessage message : messages) {
            messagesContainer.getChildren().add(
                new LessonMessageBubble(message.sender(), message.text(), message.latex())
            );
        }
        
    }

}
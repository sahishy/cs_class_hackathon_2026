package com.sahishpeter.cs_class_hackathon_2026.features.lessons.pages;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.sahishpeter.cs_class_hackathon_2026.features.calculator.components.Calculator;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.contexts.LessonContext;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.shared.components.Card;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LessonScreen extends VBox {

    private final String lessonId;
    private final Runnable onBack;

    private final List<LessonMessage> messages = new ArrayList<>();
    private final VBox messagesContainer = new VBox(10);
    private final Label stepLabel = new Label("Step 1");

    private int currentStep = 0;

    public LessonScreen(String lessonId, Runnable onBack) {
        this.lessonId = lessonId;
        this.onBack = onBack;

        getStyleClass().add("screen");
        setFillWidth(true);

        Lesson lesson = findLesson();
        if (lesson == null) {
            Label empty = new Label("Lesson not found.");
            empty.getStyleClass().add("h2");
            getChildren().add(empty);
            return;
        }

        buildLayout(lesson);
    }

    private Lesson findLesson() {
        for (Lesson lesson : LessonContext.getLessons()) {
            if (lesson.id() != null && lesson.id().equals(lessonId)) {
                return lesson;
            }
        }
        return null;
    }

    private void buildLayout(Lesson lesson) {
        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("secondary");
        backButton.setOnAction(event -> onBack.run());

        Label lessonTitle = new Label(displayTitle(lesson));
        lessonTitle.getStyleClass().add("h2");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topRow.getChildren().addAll(backButton, spacer, lessonTitle);

        HBox content = new HBox(16);
        content.getStyleClass().add("lesson-content");

        VBox leftPane = buildLeftPane(lesson);
        VBox rightPane = buildRightPane(lesson);

        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.SOMETIMES);

        content.getChildren().addAll(leftPane, rightPane);

        VBox.setVgrow(content, Priority.ALWAYS);
        getChildren().addAll(topRow, content);
    }

    private VBox buildLeftPane(Lesson lesson) {
        VBox leftPane = new VBox(12);
        leftPane.getStyleClass().add("lesson-left-pane");

        Card chatCard = new Card();
        chatCard.getStyleClass().add("lesson-chat-card");
        VBox.setVgrow(chatCard, Priority.ALWAYS);

        ScrollPane messagesScroll = new ScrollPane();
        messagesScroll.getStyleClass().addAll("scroll", "lesson-messages-scroll");
        messagesScroll.setFitToWidth(true);
        messagesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messagesScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        messagesContainer.getStyleClass().add("lesson-messages-container");

        seedMessagesFromLesson(lesson);
        renderMessages();

        messagesScroll.setContent(messagesContainer);

        BorderPane chatLayout = new BorderPane();
        chatLayout.setCenter(messagesScroll);
        chatLayout.setBottom(buildInputDock());
        BorderPane.setMargin(messagesScroll, new Insets(0, 0, 8, 0));

        chatCard.getChildren().add(chatLayout);
        VBox.setVgrow(chatLayout, Priority.ALWAYS);

        leftPane.getChildren().add(chatCard);
        VBox.setVgrow(leftPane, Priority.ALWAYS);

        return leftPane;
    }

    private HBox buildInputDock() {

        HBox inputDock = new HBox(8);
        inputDock.getStyleClass().add("lesson-input-dock");
        inputDock.setAlignment(Pos.CENTER_LEFT);

        TextField input = new TextField();
        input.getStyleClass().add("lesson-input");
        input.setPromptText("Ask a question about this lesson...");

        Button sendButton = new Button("↑");
        sendButton.getStyleClass().add("lesson-send-button");

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

    private VBox buildRightPane(Lesson lesson) {

        VBox rightPane = new VBox(12);
        rightPane.getStyleClass().add("lesson-right-pane");
        rightPane.setPrefWidth(420);

        Card rightCard = new Card();
        rightCard.getStyleClass().add("lesson-right-card");
        VBox.setVgrow(rightCard, Priority.ALWAYS);

        VBox calcHolder = new VBox();
        calcHolder.setAlignment(Pos.TOP_CENTER);
        calcHolder.getChildren().add(new Calculator());
        VBox.setVgrow(calcHolder, Priority.ALWAYS);

        HBox pagination = new HBox(16);
        pagination.getStyleClass().add("lesson-pagination");
        pagination.setAlignment(Pos.CENTER);

        Button prev = new Button("Previous");
        prev.getStyleClass().addAll("secondary");
        prev.setOnAction(event -> {
            if (currentStep > 0) {
                currentStep--;
                refreshStepLabel(lesson);
            }
        });

        Button next = new Button("Next");
        next.getStyleClass().addAll("primary");
        next.setOnAction(event -> {
            int max = Math.max(0, lesson.steps().size() - 1);
            if (currentStep < max) {
                currentStep++;
                refreshStepLabel(lesson);
            }
        });

        stepLabel.getStyleClass().add("lesson-step-label");
        refreshStepLabel(lesson);

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        pagination.getChildren().addAll(prev, leftSpacer, stepLabel, rightSpacer, next);

        rightCard.getChildren().addAll(calcHolder, pagination);
        rightPane.getChildren().add(rightCard);

        return rightPane;
    }

    private void refreshStepLabel(Lesson lesson) {
        int count = lesson.steps() == null ? 0 : lesson.steps().size();
        stepLabel.setText(count == 0 ? "Step 1" : "Step " + (currentStep + 1));
    }

    private void seedMessagesFromLesson(Lesson lesson) {
        messages.clear();

        List<Lesson.LessonStep> steps = lesson.steps() == null ? List.of() : lesson.steps();
        if (steps.isEmpty()) {
            long now = System.currentTimeMillis();
            messages.add(new LessonMessage("ai-intro", "ai", "Let's walk through this lesson together.", null, now));
            messages.add(new LessonMessage("ai-placeholder", "ai", "Lesson steps will appear here soon.", null, now + 1));
            return;
        }

        long createdAt = lesson.createdAt() > 0 ? lesson.createdAt() : System.currentTimeMillis();
        for (int i = 0; i < steps.size(); i++) {
            Lesson.LessonStep step = steps.get(i);
            StringBuilder text = new StringBuilder();
            text.append(step.title() == null || step.title().isBlank() ? "Step " + (i + 1) : step.title());
            if (step.explanation() != null && !step.explanation().isBlank()) {
                text.append("\n\n").append(step.explanation());
            }

            messages.add(new LessonMessage(
                "ai-step-" + i,
                "ai",
                text.toString(),
                i,
                createdAt + i
            ));
        }
    }

    private void renderMessages() {
        messagesContainer.getChildren().clear();

        messages.stream()
            .sorted(Comparator.comparingLong(LessonMessage::createdAt))
            .forEach(message -> {
                HBox row = new HBox();
                row.getStyleClass().add("lesson-message-row");

                Label bubble = new Label(message.text());
                bubble.setWrapText(true);
                bubble.getStyleClass().addAll("lesson-bubble", "lesson-bubble-" + message.sender());
                bubble.setMaxWidth(Double.MAX_VALUE);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                if ("user".equals(message.sender())) {
                    row.getChildren().addAll(spacer, bubble);
                } else {
                    row.getChildren().addAll(bubble, spacer);
                }

                messagesContainer.getChildren().add(row);
            });
    }

    private String displayTitle(Lesson lesson) {
        if (lesson.lessonTitle() != null && !lesson.lessonTitle().isBlank()) {
            return lesson.lessonTitle();
        }
        if (lesson.title() != null && !lesson.title().isBlank()) {
            return lesson.title();
        }
        return "Lesson";
    }

    private record LessonMessage(
        String id,
        String sender,
        String text,
        Integer stepIndex,
        long createdAt
    ) {}
}

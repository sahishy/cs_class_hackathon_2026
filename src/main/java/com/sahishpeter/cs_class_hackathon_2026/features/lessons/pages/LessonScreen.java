package com.sahishpeter.cs_class_hackathon_2026.features.lessons.pages;

import com.sahishpeter.cs_class_hackathon_2026.features.calculator.components.Calculator;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.contexts.LessonContext;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LessonScreen extends VBox {

    private final String lessonId;
    private final Label stepLabel = new Label("Step 1");

    private int currentStep = 0;

    public LessonScreen(String lessonId, Runnable onBack) {
        
        this.lessonId = lessonId;

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

        HBox content = new HBox(16);
        content.getStyleClass().add("lesson-content");

        LessonChatPanel leftPane = new LessonChatPanel(lesson);
        VBox rightPane = buildRightPane(lesson);

        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.SOMETIMES);

        content.getChildren().addAll(leftPane, rightPane);

        VBox.setVgrow(content, Priority.ALWAYS);
        getChildren().add(content);
        
    }

    private VBox buildRightPane(Lesson lesson) {

        VBox rightPane = new VBox(12);
        rightPane.getStyleClass().add("lesson-right-pane");
        rightPane.setPrefWidth(420);

        VBox calcHolder = new VBox();
        calcHolder.setAlignment(Pos.TOP_CENTER);
        calcHolder.getChildren().add(new Calculator());
        VBox.setVgrow(calcHolder, Priority.ALWAYS);

        HBox pagination = new HBox(16);
        pagination.getStyleClass().add("pagination");
        pagination.setAlignment(Pos.CENTER);

        Button prev = new Button("Previous");
        prev.getStyleClass().addAll("button", "secondary");
        prev.setOnAction(event -> {
            if (currentStep > 0) {
                currentStep--;
                updateStepLabel(lesson);
            }
        });

        Button next = new Button("Next");
        next.getStyleClass().addAll("button", "primary");
        next.setOnAction(event -> {
            int max = Math.max(0, lesson.steps().size() - 1);
            if (currentStep < max) {
                currentStep++;
                updateStepLabel(lesson);
            }
        });

        stepLabel.getStyleClass().add("step-label");
        updateStepLabel(lesson);

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        pagination.getChildren().addAll(prev, leftSpacer, stepLabel, rightSpacer, next);

        rightPane.getChildren().addAll(calcHolder, pagination);

        return rightPane;

    }

    private void updateStepLabel(Lesson lesson) {

        int count = lesson.steps() == null ? 0 : lesson.steps().size();
        stepLabel.setText(count == 0 ? "Step 1" : "Step " + (currentStep + 1));

    }
    
}

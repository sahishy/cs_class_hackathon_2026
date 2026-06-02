package com.sahishpeter.cs_class_hackathon_2026.features.lessons.pages;

import java.util.ArrayList;
import java.util.List;

import com.sahishpeter.cs_class_hackathon_2026.features.lessons.contexts.LessonContext;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson.LessonGraph;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson.LessonStep;
import com.sahishpeter.cs_class_hackathon_2026.features.math.components.Graph;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LessonScreen extends VBox {

    private final Runnable onBack;
    private final String lessonId;

    private Button prevButton;
    private Button nextButton;
    private Label stepLabel;
    private Graph graph;
    private LessonChat leftPane;
    private Label test;

    private int currentStep = 0;

    public LessonScreen(String lessonId, Runnable onBack) {

        this.onBack = onBack;
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

        leftPane = new LessonChat(lesson);
        leftPane.setCurrentStep(currentStep);
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

        VBox calculatorHolder = new VBox();
        calculatorHolder.setAlignment(Pos.CENTER_RIGHT);
        VBox.setVgrow(calculatorHolder, Priority.ALWAYS);

        graph = new Graph(new LessonGraph(new ArrayList(List.of("y = x")), null, null), 396, true);

        calculatorHolder.getChildren().add(graph);

        test = new Label(lesson.steps().get(currentStep).toString());
        test.setWrapText(true);

        HBox pagination = new HBox(16);
        pagination.getStyleClass().add("pagination");
        pagination.setAlignment(Pos.CENTER);

        prevButton = new Button("Previous");
        prevButton.getStyleClass().addAll("button", "secondary");
        prevButton.setOnAction(event -> {

            if (currentStep > 0) {

                currentStep--;

                updateStep(lesson);
                leftPane.setCurrentStep(currentStep);
                updatePaginationButtons(lesson);

            }

        });

        nextButton = new Button("Next");
        nextButton.getStyleClass().addAll("button", "primary");
        nextButton.setOnAction(event -> {

            int max = Math.max(0, lesson.steps().size() - 1);
            if (currentStep < max) {

                currentStep++;

                updateStep(lesson);
                leftPane.setCurrentStep(currentStep);
                updatePaginationButtons(lesson);

                return;
            }

            if (onBack != null) {
                onBack.run();
            }

        });

        stepLabel = new Label("Step 1");
        stepLabel.getStyleClass().add("step-label");
        updateStep(lesson);
        updatePaginationButtons(lesson);

        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        pagination.getChildren().addAll(prevButton, leftSpacer, stepLabel, rightSpacer, nextButton);
        rightPane.getChildren().addAll(calculatorHolder, pagination);
        // rightPane.getChildren().addAll(calculatorHolder, test, pagination);

        return rightPane;

    }

    private void updateStep(Lesson lesson) {

        int count = lesson.steps() == null ? 0 : lesson.steps().size();
        stepLabel.setText(count == 0 ? "Step 1" : "Step " + (currentStep + 1));

        System.out.println("updating graph");

        if (graph != null) {
            LessonStep lessonStep = lesson.steps().get(currentStep);
            graph.updateGraph(lessonStep.graph());
            test.setText(lessonStep.toString());

            System.out.println(lessonStep.graph());

        }

    }

    private void updatePaginationButtons(Lesson lesson) {

        int count = lesson.steps() == null ? 0 : lesson.steps().size();
        int max = Math.max(0, count - 1);
        boolean isFirstStep = currentStep <= 0;
        boolean isFinalStep = currentStep >= max;

        prevButton.setDisable(isFirstStep);
        nextButton.setText(isFinalStep ? "Done" : "Next");

    }

}

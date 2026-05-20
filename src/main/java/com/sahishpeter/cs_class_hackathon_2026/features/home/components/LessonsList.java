package com.sahishpeter.cs_class_hackathon_2026.features.home.components;

import java.util.List;

import com.google.cloud.firestore.ListenerRegistration;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.services.LessonService;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.shared.services.CurrentUserService;
import javafx.application.Platform;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class LessonsList extends GridPane {

    private static final double COLUMN_WIDTH = (1.0 / 3) * 100;
    private final LessonService lessonService;
    private final CurrentUserService currentUserService;
    private ListenerRegistration lessonListener;

    public LessonsList() {

        this.lessonService = new LessonService();
        this.currentUserService = new CurrentUserService();

        getStyleClass().add("lessons-list");
        setHgap(16);
        setVgap(16);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(COLUMN_WIDTH);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(COLUMN_WIDTH);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(COLUMN_WIDTH);

        getColumnConstraints().addAll(column1, column2, column3);

        startLessonsSubscription();
        sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (oldScene != null && newScene == null) {
                stopLessonsSubscription();
            }
        });

    }

    private void startLessonsSubscription() {
        String userId = currentUserService.getCurrentUserId();
        lessonListener = lessonService.subscribeToLessons(userId, lessons -> {
            Platform.runLater(() -> renderLessons(lessons));
        });
    }

    private void stopLessonsSubscription() {
        if (lessonListener != null) {
            lessonListener.remove();
            lessonListener = null;
        }
    }

    private void renderLessons(List<Lesson> lessons) {
        getChildren().clear();
        for(int i = 0; i < lessons.size(); i++) {
            int row = i / 3;
            int col = i % 3;
            LessonCard card = new LessonCard(lessons.get(i));
            add(card, col, row);

        }
    }

}
package com.sahishpeter.cs_class_hackathon_2026.features.home.components;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import com.sahishpeter.cs_class_hackathon_2026.features.lessons.contexts.LessonContext;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class LessonsList extends GridPane {

    private static final double COLUMN_WIDTH = (1.0 / 3) * 100;
    private final Consumer<String> onOpenLesson;
    private List<Lesson> lessons = List.of();
    private boolean generatingLesson;

    public LessonsList(Consumer<String> onOpenLesson) {

        this.onOpenLesson = onOpenLesson;

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

        LessonContext.useLessons(this, updatedLessons -> {
            this.lessons = updatedLessons;
            renderLessons();
        });

    }

    public void setGeneratingLesson(boolean generatingLesson) {
        this.generatingLesson = generatingLesson;
        renderLessons();
    }

    private void renderLessons() {

        getChildren().clear();

        List<Lesson> sortedLessons = lessons.stream()
                .sorted(Comparator.comparingLong(Lesson::updatedAt).reversed())
                .toList();


        int index = 0;

        if (generatingLesson) {
            add(new LessonCardSkeleton(), 0, 0);
            index++;
        }

        for (Lesson lesson : sortedLessons) {

            int row = index / 3;
            int col = index % 3;

            LessonCard card = new LessonCard(lesson, selectedLesson -> this.onOpenLesson.accept(selectedLesson.id()));
            add(card, col, row);

            index++;

        }

    }

}
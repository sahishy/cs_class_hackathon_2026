package com.sahishpeter.cs_class_hackathon_2026.features.home.components;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class LessonsList extends GridPane {

    private static final double COLUMN_WIDTH = (1.0 / 3) * 100;

    public LessonsList() {

        getStyleClass().add("lessons-list");
        setHgap(16);
        setVgap(16);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(COLUMN_WIDTH);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(COLUMN_WIDTH);
        ColumnConstraints column3 = new ColumnConstraints();
        column3.setPercentWidth(COLUMN_WIDTH);

        LessonCard card1 = new LessonCard();
        LessonCard card2 = new LessonCard();
        LessonCard card3 = new LessonCard();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                LessonCard card = new LessonCard();
                add(card, col, row);
            }
        }

        getColumnConstraints().addAll(column1, column2, column3);

    }

}
package com.sahishpeter.cs_class_hackathon_2026.features.home.pages;

import com.sahishpeter.cs_class_hackathon_2026.features.calculator.components.Calculator;
import com.sahishpeter.cs_class_hackathon_2026.features.home.components.LessonsList;
import com.sahishpeter.cs_class_hackathon_2026.features.home.components.QuestionInput;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class HomeScreen extends VBox {

    public HomeScreen() {

        setSpacing(16);
        setFillWidth(true);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().addAll("screen", "scroll");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox container = new VBox();
        container.setSpacing(16);
        container.getStyleClass().add("test3");

        QuestionInput questionInput = new QuestionInput();

        Label lessonsSectionLabel = new Label("My Lessons");
        lessonsSectionLabel.getStyleClass().add("h2");

        LessonsList lessonsList = new LessonsList();

        //test for peter
        Calculator calc = new Calculator();

        container.getChildren().addAll(
            questionInput,
            lessonsSectionLabel,
            calc,
            lessonsList
            
        );

        scrollPane.setContent(container);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        getChildren().add(scrollPane);

    }

}
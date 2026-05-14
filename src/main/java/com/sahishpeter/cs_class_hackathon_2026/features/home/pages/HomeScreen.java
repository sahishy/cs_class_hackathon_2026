package com.sahishpeter.cs_class_hackathon_2026.features.home.pages;

import com.sahishpeter.cs_class_hackathon_2026.features.home.components.LessonsList;
import com.sahishpeter.cs_class_hackathon_2026.features.home.components.QuestionInput;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HomeScreen extends VBox {

    // public HomeScreen() {

    // setSpacing(16);

    // ScrollPane scrollPane = new ScrollPane();
    // scrollPane.getStyleClass().add("screen");

    // VBox container = new VBox();
    // container.getStyleClass().add("test3");

    // QuestionInput questionInput = new QuestionInput();

    // Label lessonsSectionLabel = new Label("My Lessons");
    // lessonsSectionLabel.getStyleClass().add("h2");

    // LessonsList lessonsList = new LessonsList();

    // container.getChildren().addAll(questionInput, lessonsSectionLabel,
    // lessonsList);
    // scrollPane.setContent(container);
    // getChildren().add(container);

    // }

    public HomeScreen() {

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().addAll("screen", "scroll");
        VBox innerContent = new VBox(); // This will hold your scrollable items

        // Add some dummy items to make it scrollable
        for (int i = 0; i < 50; i++) {
        innerContent.getChildren().add(new Label("Item " + i));
        }

        // 3. Put the inner content into the ScrollPane
        scrollPane.setContent(innerContent);

        // 4. Configure ScrollPane to fill the VBox
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setFitToWidth(true); // Optional: makes content match ScrollPane

        getChildren().add(scrollPane);

    }

}
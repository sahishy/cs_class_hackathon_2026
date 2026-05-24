package com.sahishpeter.cs_class_hackathon_2026.features.home.components;

import java.util.function.Consumer;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class QuestionInput extends VBox {

    private final TextField textField;
    private final Consumer<String> onQuestionSubmitted;
    private boolean generatingLesson;

    public QuestionInput(Consumer<String> onQuestionSubmitted) {

        this.onQuestionSubmitted = onQuestionSubmitted;

        setAlignment(Pos.CENTER);
        setPrefHeight(256);
        setSpacing(24);

        Label greetingLabel = new Label("Hey, Sahish!");
        greetingLabel.getStyleClass().add("h1");
        
        textField = new TextField();
        textField.getStyleClass().add("input");
        textField.setMaxWidth(400);
        textField.setPromptText("Ask a question about math...");
        textField.setFocusTraversable(false);
        textField.setOnAction(event -> askQuestion());

        getChildren().addAll(greetingLabel, textField);

    }

    public void askQuestion() {

        if (generatingLesson) {
            return;
        }

        String question = textField.getText();
        if (question == null || question.isBlank()) {
            return;
        }

        onQuestionSubmitted.accept(question.trim());
        textField.clear();

    }

    public void setGeneratingLesson(boolean loading) {

        this.generatingLesson = loading;
        textField.setDisable(loading);
        textField.setPromptText(loading
                ? "Generating lesson..."
                : "Ask a question about math...");

    }

}

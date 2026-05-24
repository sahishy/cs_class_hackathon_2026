package com.sahishpeter.cs_class_hackathon_2026.features.home.pages;

import java.util.function.Consumer;
import com.sahishpeter.cs_class_hackathon_2026.features.home.components.LessonsList;
import com.sahishpeter.cs_class_hackathon_2026.features.home.components.QuestionInput;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.services.LessonService;
import com.sahishpeter.cs_class_hackathon_2026.features.user.contexts.UserContext;
import com.sahishpeter.cs_class_hackathon_2026.shared.components.EdgeFadeOverlay;
import javafx.geometry.Pos;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HomeScreen extends VBox {

    private QuestionInput questionInput;

    public HomeScreen(Consumer<String> onOpenLesson) {

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

        LessonService lessonService = new LessonService();
        LessonsList lessonsList = new LessonsList(onOpenLesson);

        questionInput = new QuestionInput(question -> {

            questionInput.setGeneratingLesson(true);
            lessonsList.setGeneratingLesson(true);

            lessonService.createLesson(UserContext.getCurrentUserId(), question)
                    .whenComplete((lessonId, error) -> Platform.runLater(() -> {

                        questionInput.setGeneratingLesson(false);
                        lessonsList.setGeneratingLesson(false);

                        if (error != null) {
                            error.printStackTrace();
                        }

                    }));
        });

        Label lessonsSectionLabel = new Label("My Lessons");
        lessonsSectionLabel.getStyleClass().add("h2");

        container.getChildren().addAll(
                questionInput,
                lessonsSectionLabel,
                lessonsList);

        scrollPane.setContent(container);

        StackPane homeWithFade = new StackPane();
        EdgeFadeOverlay homeBottomFade = new EdgeFadeOverlay(EdgeFadeOverlay.Direction.BOTTOM);
        StackPane.setAlignment(homeBottomFade, Pos.BOTTOM_CENTER);
        homeWithFade.getChildren().addAll(scrollPane, homeBottomFade);

        VBox.setVgrow(homeWithFade, Priority.ALWAYS);
        getChildren().add(homeWithFade);

    }

}
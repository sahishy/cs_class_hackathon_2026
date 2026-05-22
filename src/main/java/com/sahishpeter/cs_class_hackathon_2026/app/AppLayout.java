package com.sahishpeter.cs_class_hackathon_2026.app;

import com.sahishpeter.cs_class_hackathon_2026.features.home.pages.HomeScreen;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.pages.LessonScreen;
import com.sahishpeter.cs_class_hackathon_2026.shared.components.Sidebar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AppLayout extends HBox {

    private final VBox screen = new VBox();

    public AppLayout() {

        getStyleClass().add("main");

        Sidebar sidebar = new Sidebar();

        screen.setFillWidth(true);

        openHome();

        HBox.setHgrow(sidebar, Priority.ALWAYS);
        HBox.setHgrow(screen, Priority.ALWAYS);

        getChildren().addAll(sidebar, screen);

    }

    private void openHome() {
        HomeScreen homeScreen = new HomeScreen(this::openLesson);
        showScreen(homeScreen);
    }

    private void openLesson(String lessonId) {
        LessonScreen lessonScreen = new LessonScreen(lessonId, this::openHome);
        showScreen(lessonScreen);
    }

    private void showScreen(VBox nextScreen) {
        screen.getChildren().setAll(nextScreen);
        VBox.setVgrow(nextScreen, Priority.ALWAYS);
    }

}
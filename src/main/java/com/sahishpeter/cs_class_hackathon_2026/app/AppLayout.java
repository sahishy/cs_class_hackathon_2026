package com.sahishpeter.cs_class_hackathon_2026.app;

import com.sahishpeter.cs_class_hackathon_2026.features.home.pages.HomeScreen;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.pages.LessonScreen;
import com.sahishpeter.cs_class_hackathon_2026.shared.components.Sidebar;

import java.util.function.Consumer;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AppLayout extends HBox {

    private final VBox screen = new VBox();
    private static SimpleStringProperty currentScreen = new SimpleStringProperty("");

    public AppLayout() {

        getStyleClass().add("main");

        Runnable onOpenHome = this::openHome;
        Consumer<String> onOpenLesson = this::openLesson;
        Sidebar sidebar = new Sidebar(onOpenHome, onOpenLesson);

        screen.setFillWidth(true);

        openHome();

        HBox.setHgrow(sidebar, Priority.ALWAYS);
        HBox.setHgrow(screen, Priority.ALWAYS);

        getChildren().addAll(sidebar, screen);

    }

    private void openHome() {

        if(currentScreen.get().equals("home")) return;

        HomeScreen homeScreen = new HomeScreen(this::openLesson);
        showScreen(homeScreen);

        AppLayout.currentScreen.set("home");

    }

    private void openLesson(String lessonId) {

        if(currentScreen.get().equals("lesson/" + lessonId)) return;

        LessonScreen lessonScreen = new LessonScreen(lessonId, this::openHome);
        showScreen(lessonScreen);

        AppLayout.currentScreen.set("lesson/" + lessonId);

    }

    private void showScreen(VBox nextScreen) {
        screen.getChildren().setAll(nextScreen);
        VBox.setVgrow(nextScreen, Priority.ALWAYS);
    }

    public static SimpleStringProperty getCurrentScreen() {
        return AppLayout.currentScreen;
    }

}
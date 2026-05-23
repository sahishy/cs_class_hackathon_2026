package com.sahishpeter.cs_class_hackathon_2026.shared.components;

import java.util.List;
import java.util.function.Consumer;

import com.sahishpeter.cs_class_hackathon_2026.app.AppLayout;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.contexts.LessonContext;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    private final Image logo = new Image(Sidebar.class.getResource("/images/logo.png").toExternalForm());

    private final Runnable onOpenHome;
    private final Consumer<String> onOpenLesson;

    private final Label homeTab;
    private final VBox lessonsHolder;
    private String currentScreen = "";

    public Sidebar(Runnable onOpenHome, Consumer<String> onOpenLesson) {

        this.onOpenHome = onOpenHome;
        this.onOpenLesson = onOpenLesson;

        setMaxWidth(256);
        setSpacing(16);
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("sidebar");

        VBox logoHolder = buildLogoHolder();
        homeTab = buildHomeTab();

        Label recentsLabel = new Label("Recents");
        recentsLabel.setMaxWidth(Double.MAX_VALUE);
        recentsLabel.getStyleClass().add("sidebar-label");

        lessonsHolder = new VBox();
        lessonsHolder.setSpacing(4);

        VBox recentsHolder = new VBox();
        recentsHolder.setSpacing(4);
        recentsHolder.getChildren().addAll(recentsLabel, lessonsHolder);

        getChildren().addAll(logoHolder, homeTab, recentsHolder);

        AppLayout.getCurrentScreen().addListener((observable, oldValue, newValue) -> {
            currentScreen = newValue;
            updateActiveState();
        });

        LessonContext.useLessons(this, this::renderLessons);

        currentScreen = AppLayout.getCurrentScreen().get();
        updateActiveState();

    }

    private VBox buildLogoHolder() {

        VBox logoHolder = new VBox();
        logoHolder.setAlignment(Pos.CENTER);
        logoHolder.setPrefHeight(96);

        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(192);
        logoView.setPreserveRatio(true);

        logoHolder.getChildren().add(logoView);
        return logoHolder;

    }

    private Label buildHomeTab() {

        Label tab = new Label("Home");

        tab.setMaxWidth(Double.MAX_VALUE);
        tab.getStyleClass().add("sidebar-tab");
        tab.setOnMouseClicked(event -> onOpenHome.run());

        return tab;

    }

    private void renderLessons(List<Lesson> lessons) {

        lessonsHolder.getChildren().clear();

        for (Lesson lesson : lessons) {
            String id = lesson.id();
            String title = lesson.title() == null || lesson.title().isBlank() ? "Untitled Lesson" : lesson.title();

            Label tab = new Label(title);
            tab.setMaxWidth(Double.MAX_VALUE);
            tab.getStyleClass().add("sidebar-tab");
            tab.setUserData(id);
            tab.setOnMouseClicked(event -> onOpenLesson.accept(id));

            lessonsHolder.getChildren().add(tab);
        }

        updateActiveState();

    }

    private void updateActiveState() {

        setTabActive(homeTab, "home".equals(currentScreen));

        lessonsHolder.getChildren().forEach(node -> {
            
            if (!(node instanceof Label tab)) {
                return;
            }

            Object id = tab.getUserData();
            String lessonId = id == null ? "" : id.toString();
            boolean isActive = ("lesson/" + lessonId).equals(currentScreen);
            setTabActive(tab, isActive);

        });

    }

    private void setTabActive(Label tab, boolean isActive) {

        if (isActive) {
            if (!tab.getStyleClass().contains("active")) {
                tab.getStyleClass().add("active");
            }
            return;
        }

        tab.getStyleClass().remove("active");

    }

}
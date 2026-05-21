package com.sahishpeter.cs_class_hackathon_2026.shared.components;

import java.util.List;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.contexts.LessonContext;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Sidebar extends VBox {

    private final Image logo = new Image(Sidebar.class.getResource("/images/logo.png").toExternalForm());
    private final VBox lessonsHolder;

    public Sidebar() {

        setMaxWidth(256);
        setSpacing(16);
        setAlignment(Pos.TOP_CENTER);
        getStyleClass().add("sidebar");

        VBox logoHolder = new VBox();
        logoHolder.setAlignment(Pos.CENTER);
        logoHolder.setPrefHeight(96);
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(192);
        logoView.setPreserveRatio(true);
        logoHolder.getChildren().add(logoView);

        Label homeTab = new Label("Home");
        homeTab.getStyleClass().addAll("sidebar-tab", "active");
        homeTab.setMaxWidth(Double.MAX_VALUE);

        VBox recentsHolder = new VBox();
        recentsHolder.setSpacing(4);
        Label recentsLabel = new Label("Recents");
        recentsLabel.setMaxWidth(Double.MAX_VALUE);
        recentsLabel.getStyleClass().add("sidebar-label");
        lessonsHolder = new VBox();
        recentsHolder.getChildren().addAll(recentsLabel, lessonsHolder);

        getChildren().addAll(logoHolder, homeTab, recentsHolder);

        LessonContext.useLessons(this, this::renderLessons);

    }

    private void renderLessons(List<Lesson> lessons) {

        lessonsHolder.getChildren().clear();

        for(int i = 0; i < lessons.size(); i++) {

            Lesson lesson = lessons.get(i);
            String title = lesson.title();

            Label tab = new Label(title);
            tab.setMaxWidth(Double.MAX_VALUE);
            tab.getStyleClass().addAll("sidebar-tab");

            lessonsHolder.getChildren().add(tab);

        }

    }
    
}
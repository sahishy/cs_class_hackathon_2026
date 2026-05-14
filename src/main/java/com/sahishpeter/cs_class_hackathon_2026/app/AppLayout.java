package com.sahishpeter.cs_class_hackathon_2026.app;

import com.sahishpeter.cs_class_hackathon_2026.features.home.pages.HomeScreen;
import com.sahishpeter.cs_class_hackathon_2026.shared.components.Sidebar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

public class AppLayout extends HBox {

    public AppLayout() {

        getStyleClass().add("main");

        Sidebar sidebar = new Sidebar();

        VBox screen = new VBox();
    
        HomeScreen homeScreen = new HomeScreen();
        screen.getChildren().add(homeScreen);

        HBox.setHgrow(sidebar, Priority.ALWAYS);
        HBox.setHgrow(screen, Priority.ALWAYS);

        getChildren().addAll(sidebar, screen);

    }

}
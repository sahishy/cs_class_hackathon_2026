package com.sahishpeter.cs_class_hackathon_2026.app;

import com.sahishpeter.cs_class_hackathon_2026.features.home.pages.HomeScreen;
import com.sahishpeter.cs_class_hackathon_2026.shared.components.Sidebar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class AppLayout extends HBox {

    public AppLayout() {

        getStyleClass().add("container");

        Sidebar sidebar = new Sidebar();
        HomeScreen homeScreen = new HomeScreen();

        HBox.setHgrow(sidebar, Priority.ALWAYS);
        HBox.setHgrow(homeScreen, Priority.ALWAYS);

        getChildren().addAll(sidebar, homeScreen);

    }

}
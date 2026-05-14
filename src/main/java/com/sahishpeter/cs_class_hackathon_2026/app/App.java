package com.sahishpeter.cs_class_hackathon_2026.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        
        AppLayout appLayout = new AppLayout();

        Scene scene = new Scene(appLayout, 1280, 720);
        scene.getStylesheets().add(App.class.getResource("/styles.css").toExternalForm());

        stage.setTitle("HishHuyPT Calculator: Deluxe Edition");
        stage.setMinWidth(640);
        stage.setMinHeight(360);
        stage.setScene(scene);
        stage.show();
        
    }

}

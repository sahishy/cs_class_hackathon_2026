package com.sahishpeter.cs_class_hackathon_2026.app;

import com.sahishpeter.cs_class_hackathon_2026.lib.Firebase;
import com.sahishpeter.cs_class_hackathon_2026.shared.services.CurrentUserService;
import com.sahishpeter.cs_class_hackathon_2026.shared.services.UserService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        
        Firebase.initialize();

        CurrentUserService currentUserService = new CurrentUserService();
        UserService userService = new UserService();
        userService.upsertUser(currentUserService.getCurrentUserId(), "Sahish", "Durgam");
        
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

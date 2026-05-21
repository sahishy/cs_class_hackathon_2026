package com.sahishpeter.cs_class_hackathon_2026.app;

import java.util.ArrayList;
import java.util.List;

import com.sahishpeter.cs_class_hackathon_2026.features.lessons.contexts.LessonContext;
import com.sahishpeter.cs_class_hackathon_2026.features.user.contexts.UserContext;
import com.sahishpeter.cs_class_hackathon_2026.lib.Firebase;
import com.sahishpeter.cs_class_hackathon_2026.shared.contexts.BaseContext;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private List<BaseContext> contexts = new ArrayList<>();

    @Override
    public void start(Stage stage) {

        Firebase.configureGoogleSdkLogging();
        Firebase.initialize();

        initializeContexts();

        AppLayout appLayout = new AppLayout();

        Scene scene = new Scene(appLayout, 1280, 720);
        scene.getStylesheets().add(App.class.getResource("/styles.css").toExternalForm());

        stage.setTitle("HishHuyPT Calculator: Deluxe Edition");
        stage.setMinWidth(640);
        stage.setMinHeight(360);
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void stop() {
        disposeContexts();
    }

    private void initializeContexts() {

        contexts.add(UserContext.getInstance());
        contexts.add(LessonContext.getInstance());

        for (BaseContext context : contexts) {
            context.initialize();
        }

    }

    private void disposeContexts() {
        for (BaseContext context : contexts) {
            context.dispose();
        }
    }

}

package com.sahishpeter.cs_class_hackathon_2026.lib;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;

import io.github.cdimascio.dotenv.Dotenv;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public final class Firebase {

    private static final String SERVICE_ACCOUNT_PATH_ENV = "FIREBASE_SERVICE_ACCOUNT_PATH";
    private static volatile boolean initialized = false;

    public static void configureGoogleSdkLogging() {

        Level level = Level.WARNING;

        Logger.getLogger("com.google.cloud.firestore").setLevel(level);
        Logger.getLogger("com.google.api.gax").setLevel(level);
        Logger.getLogger("com.google.api").setLevel(level);
        Logger.getLogger("io.grpc").setLevel(level);

        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(level);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(level);
        }
        
    }

    public static synchronized void initialize() {

        if (initialized || !FirebaseApp.getApps().isEmpty()) {
            initialized = true;
            return;
        }

        Dotenv dotenv = Dotenv.load();
        String serviceAccountPath = dotenv.get(SERVICE_ACCOUNT_PATH_ENV);

        try (FileInputStream serviceAccount = new FileInputStream(serviceAccountPath)) {

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            FirebaseApp.initializeApp(options);
            initialized = true;

        } catch (IOException exception) {
            throw new IllegalStateException("Failed to initialize Firebase Admin SDK", exception);
        }
        
    }

    public static Firestore firestore() {

        if(!initialized && FirebaseApp.getApps().isEmpty()) {
            initialize();
        }

        return FirestoreClient.getFirestore();

    }
}

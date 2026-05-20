package com.sahishpeter.cs_class_hackathon_2026.lib;

import java.io.FileInputStream;
import java.io.IOException;
import io.github.cdimascio.dotenv.Dotenv;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public final class Firebase {

    private static final String SERVICE_ACCOUNT_PATH_ENV = "FIREBASE_SERVICE_ACCOUNT_PATH";

    private static volatile boolean initialized = false;

    private Firebase() {
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
        if (!initialized && FirebaseApp.getApps().isEmpty()) {
            initialize();
        }

        return FirestoreClient.getFirestore();
    }
}

package com.sahishpeter.cs_class_hackathon_2026.features.lessons.services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.lib.Firebase;

public class LessonService {

    private final Firestore firestore;

    public LessonService() {
        this.firestore = Firebase.firestore();
    }

    public ListenerRegistration subscribeToLessons(String userId, Consumer<List<Lesson>> onChange) {
        return firestore.collection("lessons")
            .whereEqualTo("userId", userId)
            .addSnapshotListener((snapshot, error) -> {
                if (error != null || snapshot == null) {
                    return;
                }

                List<Lesson> lessons = new ArrayList<>();
                for (QueryDocumentSnapshot document : snapshot) {
                    String lessonUserId = document.getString("userId");
                    String title = document.getString("title");

                    if (lessonUserId == null || title == null) {
                        continue;
                    }

                    lessons.add(new Lesson(lessonUserId, title));
                }

                onChange.accept(lessons);
            });
    }

    public void upsertLesson(String lessonId, Lesson lesson) {
        firestore.collection("lessons").document(lessonId).set(lesson);
    }
}

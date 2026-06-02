package com.sahishpeter.cs_class_hackathon_2026.features.lessons.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import com.sahishpeter.cs_class_hackathon_2026.features.ai.services.AIService;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.utils.LessonUtils;
import com.sahishpeter.cs_class_hackathon_2026.lib.Firebase;

public class LessonService {

    private final Firestore firestore;
    private final AIService aiService;

    public LessonService() {
        this.firestore = Firebase.firestore();
        this.aiService = new AIService();

        System.out.println("[LessonService] REAL FIRESTORE LISTENER CREATED");
    }

    public CompletableFuture<String> createLesson(String userId, String question) {

        if (userId == null || userId.isBlank()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("userId is required"));
        }

        if (question == null || question.isBlank()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("question is required"));
        }

        DocumentReference lessonDoc = firestore.collection("lessons").document();
        String lessonId = lessonDoc.getId();
        long timestamp = System.currentTimeMillis();
        String normalizedQuestion = question.trim();

        return aiService.generateLesson(normalizedQuestion)
                .thenApply(json -> aiService.parseLessonFromJson(json, lessonId, userId, normalizedQuestion, timestamp))
                .thenApply(aiLesson -> new Lesson(
                    lessonId,
                    userId,
                    normalizedQuestion,
                    aiLesson.title(),
                    aiLesson.topic(),
                    timestamp,
                    timestamp,
                    aiLesson.thumbnailGraph(),
                    aiLesson.steps()))
                .thenCompose(finalLesson -> upsertLesson(lessonId, finalLesson).thenApply(ignored -> lessonId));
        
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
                        lessons.add(LessonUtils.fromDocument(document));
                    }

                    onChange.accept(lessons);

                });

    }

    public CompletableFuture<Void> upsertLesson(String lessonId, Lesson lesson) {

        if (lessonId == null || lesson == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("lessonId and lesson are required"));
        }

        Map<String, Object> map = LessonUtils.toFirestoreMap(lesson);
        if(map == null) {
            return CompletableFuture.failedFuture(new RuntimeException("toFirestoreMap failed"));
        }

        return CompletableFuture.supplyAsync(() -> {

            try {

                ApiFuture<WriteResult> writeFuture = firestore.collection("lessons")
                        .document(lessonId)
                        .set(map);
                
                writeFuture.get();
                return null;

            } catch (Exception exception) {
                throw new RuntimeException(exception.getMessage(), exception);
            }

        });

    }

}

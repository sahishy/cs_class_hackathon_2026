package com.sahishpeter.cs_class_hackathon_2026.features.lessons.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        System.out.println("[LessonService] REAL FIRESTORE LISTENER CREATED");
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

                    String lessonId = document.getId();
                    String lessonUserId = document.getString("userId");
                    String question = document.getString("question");
                    String title = document.getString("title");
                    String topic = document.getString("topic");

                    Long createdAtValue = document.getLong("createdAt");
                    long createdAt = createdAtValue != null ? createdAtValue : System.currentTimeMillis();

                    Lesson.LessonGraph thumbnailGraph = parseGraph(document.get("thumbnailGraph"));
                    List<Lesson.LessonStep> steps = parseSteps(document.get("steps"));

                    lessons.add(new Lesson(
                        lessonId,
                        lessonUserId != null ? lessonUserId : "",
                        question != null ? question : "",
                        title != null ? title : "Untitled Lesson",
                        topic != null ? topic : "",
                        createdAt,
                        createdAt,
                        thumbnailGraph,
                        steps
                    ));

                }

                onChange.accept(lessons);

            });

    }

    public void upsertLesson(String lessonId, Lesson lesson) {

        if(lessonId == null || lesson == null) return;
        firestore.collection("lessons").document(lessonId).set(lesson);

    }

    private List<Lesson.LessonStep> parseSteps(Object rawSteps) {

        List<Lesson.LessonStep> steps = new ArrayList<>();

        if (!(rawSteps instanceof List<?> rawList)) {
            return steps;
        }

        for (Object rawStep : rawList) {
            if (!(rawStep instanceof Map<?, ?> map)) {
                continue;
            }

            String title = map.get("title") instanceof String value ? value : "Step";
            String explanation = map.get("explanation") instanceof String value ? value : "";
            Lesson.LessonGraph graph = parseGraph(map.get("graph"));

            steps.add(new Lesson.LessonStep(title, explanation, graph));
        }

        return steps;

    }

    private Lesson.LessonGraph parseGraph(Object rawGraph) {

        if (!(rawGraph instanceof Map<?, ?> map)) {
            return new Lesson.LessonGraph("function", List.of(), List.of());
        }

        String type = map.get("type") instanceof String value ? value : "function";

        List<String> expressions = new ArrayList<>();
        Object rawExpressions = map.get("expressions");
        if (rawExpressions instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof String expression) {
                    expressions.add(expression);
                }
            }
        }

        List<List<Double>> points = new ArrayList<>();
        Object rawPoints = map.get("points");
        if (rawPoints instanceof List<?> list) {
            for (Object row : list) {
                if (!(row instanceof List<?> pointPair) || pointPair.size() < 2) {
                    continue;
                }

                Object xRaw = pointPair.get(0);
                Object yRaw = pointPair.get(1);

                if (xRaw instanceof Number x && yRaw instanceof Number y) {
                    points.add(List.of(x.doubleValue(), y.doubleValue()));
                }
            }
        }

        return new Lesson.LessonGraph(type, expressions, points);

    }

}

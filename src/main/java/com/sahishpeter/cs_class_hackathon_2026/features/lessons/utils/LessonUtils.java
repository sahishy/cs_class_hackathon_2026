package com.sahishpeter.cs_class_hackathon_2026.features.lessons.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.features.math.types.Point;

public class LessonUtils {

    public static Lesson fromDocument(QueryDocumentSnapshot document) {

        String lessonId = document.getId();
        String lessonUserId = document.getString("userId");
        String question = document.getString("question");
        String title = document.getString("title");
        String topic = document.getString("topic");

        Long createdAtValue = document.getLong("createdAt");
        long createdAt = createdAtValue != null ? createdAtValue : System.currentTimeMillis();

        Map<?, ?> graphMap = document.get("thumbnailGraph") instanceof Map<?, ?> map ? map : null;
        List<String> expressions = new ArrayList<>();
        List<Point> points = new ArrayList<>();

        if (graphMap != null) {
            Object rawExpressions = graphMap.get("expressions");
            if (rawExpressions instanceof List<?> expressionList) {
                for (Object item : expressionList) {
                    if (item instanceof String expression) {
                        expressions.add(expression);
                    }
                }
            }
            Object rawPoints = graphMap.get("points");
            if (rawPoints instanceof List<?> pointList) {
                for (Object item : pointList) {
                    Point parsedPoint = parsePoint(item);
                    if (parsedPoint != null) {
                        points.add(parsedPoint);
                    }
                }
            }
        }

        Lesson.LessonGraph thumbnailGraph = new Lesson.LessonGraph(expressions, points);
        
        List<Lesson.LessonStep> steps = new ArrayList<>();
        Object rawSteps = document.get("steps");
        if (rawSteps instanceof List<?> stepList) {
            for (Object rawStep : stepList) {

                if (!(rawStep instanceof Map<?, ?> stepMap)) {
                    continue;
                }

                String stepTitle = stepMap.get("title") instanceof String value ? value : "Step";
                String explanation = stepMap.get("explanation") instanceof String value ? value : "";
                List<String> latexSnippets = new ArrayList<>();
                if (stepMap.get("latexSnippets") instanceof List<?> snippetList) {
                    for (Object item : snippetList) {
                        if (item instanceof String snippet) {
                            latexSnippets.add(snippet);
                        }
                    }
                } else if (stepMap.get("latex") instanceof String legacyLatex && !legacyLatex.isBlank()) {
                    latexSnippets.add(legacyLatex);
                }

                List<String> stepExpressions = new ArrayList<>();
                List<Point> stepPoints = new ArrayList<>();
                if (stepMap.get("graph") instanceof Map<?, ?> stepGraphMap) {
                    Object rawStepExpressions = stepGraphMap.get("expressions");
                    if (rawStepExpressions instanceof List<?> expressionList) {
                        for (Object item : expressionList) {
                            if (item instanceof String expression) {
                                stepExpressions.add(expression);
                            }
                        }
                    }
                    Object rawStepPoints = stepGraphMap.get("points");
                    if (rawStepPoints instanceof List<?> pointList) {
                        for (Object item : pointList) {
                            Point parsedPoint = parsePoint(item);
                            if (parsedPoint != null) {
                                stepPoints.add(parsedPoint);
                            }
                        }
                    }
                }

                steps.add(new Lesson.LessonStep(stepTitle, explanation, latexSnippets, new Lesson.LessonGraph(stepExpressions, stepPoints)));

            }
        }

        return new Lesson(
                lessonId,
                lessonUserId != null ? lessonUserId : "",
                question != null ? question : "",
                title != null ? title : "Untitled Lesson",
                topic != null ? topic : "",
                createdAt,
                createdAt,
                thumbnailGraph,
                steps);

    }

    public static Map<String, Object> toFirestoreMap(Lesson lesson) {

        Map<String, Object> map = new HashMap<>();
        map.put("id", lesson.id());
        map.put("userId", lesson.userId());
        map.put("question", lesson.question());
        map.put("title", lesson.title());
        map.put("topic", lesson.topic());
        map.put("createdAt", lesson.createdAt());
        map.put("updatedAt", lesson.updatedAt());

        Map<String, Object> thumbnailGraph = new HashMap<>();
        thumbnailGraph.put("expressions",
                lesson.thumbnailGraph() != null ? lesson.thumbnailGraph().expressions() : List.of());
        thumbnailGraph.put("points",
                toPointMaps(lesson.thumbnailGraph() != null ? lesson.thumbnailGraph().points() : List.of()));
        map.put("thumbnailGraph", thumbnailGraph);

        List<Map<String, Object>> steps = new ArrayList<>();
        if (lesson.steps() != null) {
            for (Lesson.LessonStep step : lesson.steps()) {
                Map<String, Object> stepMap = new HashMap<>();
                stepMap.put("title", step.title());
                stepMap.put("explanation", step.explanation());
                stepMap.put("latexSnippets", step.latexSnippets() != null ? step.latexSnippets() : List.of());

                Map<String, Object> stepGraph = new HashMap<>();
                stepGraph.put("expressions", step.graph() != null ? step.graph().expressions() : List.of());
                stepGraph.put("points", toPointMaps(step.graph() != null ? step.graph().points() : List.of()));
                stepMap.put("graph", stepGraph);

                steps.add(stepMap);
            }
        }

        map.put("steps", steps);
        return map;

    }

    private static List<Map<String, Double>> toPointMaps(List<Point> points) {

        List<Map<String, Double>> mappedPoints = new ArrayList<>();
        if (points == null) {
            return mappedPoints;
        }

        for (Point point : points) {
            Map<String, Double> mappedPoint = new HashMap<>();
            mappedPoint.put("x", point.x());
            mappedPoint.put("y", point.y());
            mappedPoints.add(mappedPoint);
        }

        return mappedPoints;

    }

    private static Point parsePoint(Object rawPoint) {

        if (rawPoint instanceof Map<?, ?> pointMap
                && pointMap.get("x") instanceof Number x
                && pointMap.get("y") instanceof Number y) {
            return new Point(x.doubleValue(), y.doubleValue());
        }

        if (rawPoint instanceof List<?> pair
                && pair.size() >= 2
                && pair.get(0) instanceof Number x
                && pair.get(1) instanceof Number y) {
            return new Point(x.doubleValue(), y.doubleValue());
        }

        return null;

    }

}
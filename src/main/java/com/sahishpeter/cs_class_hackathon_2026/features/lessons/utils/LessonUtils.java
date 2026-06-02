package com.sahishpeter.cs_class_hackathon_2026.features.lessons.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.features.math.types.GraphShade;
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
        List<GraphShade> shades = new ArrayList<>();

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

            Object rawShades = graphMap.get("shades");
            if (rawShades instanceof List<?> shadeList) {
                for (Object item : shadeList) {
                    GraphShade parsedShade = parseShade(item);
                    if (parsedShade != null) {
                        shades.add(parsedShade);
                    }
                }
            }
        }

        Lesson.LessonGraph thumbnailGraph = new Lesson.LessonGraph(expressions, points, shades);
        
        List<Lesson.LessonStep> steps = new ArrayList<>();
        Object rawSteps = document.get("steps");
        if (rawSteps instanceof List<?> stepList) {
            for (Object rawStep : stepList) {

                if (!(rawStep instanceof Map<?, ?> stepMap)) {
                    continue;
                }

                String stepTitle = stepMap.get("title") instanceof String value ? value : "Step";
                List<Lesson.LessonContentBlock> content = new ArrayList<>();
                if (stepMap.get("content") instanceof List<?> contentList) {
                    for (Object item : contentList) {
                        if (item instanceof Map<?, ?> contentMap) {
                            Object typeRaw = contentMap.get("type");
                            Object valueRaw = contentMap.get("value");
                            if (typeRaw instanceof String type && valueRaw instanceof String blockValue) {
                                String normalizedType = type.trim().toLowerCase();
                                if (("text".equals(normalizedType) || "latex".equals(normalizedType)) && !blockValue.isBlank()) {
                                    content.add(new Lesson.LessonContentBlock(normalizedType, blockValue));
                                }
                            }
                        }
                    }
                } else {
                    String explanation = stepMap.get("explanation") instanceof String value ? value : "";
                    if (!explanation.isBlank()) {
                        content.add(new Lesson.LessonContentBlock("text", explanation));
                    }

                    if (stepMap.get("latexSnippets") instanceof List<?> snippetList) {
                        for (Object item : snippetList) {
                            if (item instanceof String snippet && !snippet.isBlank()) {
                                content.add(new Lesson.LessonContentBlock("latex", snippet));
                            }
                        }
                    } else if (stepMap.get("latex") instanceof String legacyLatex && !legacyLatex.isBlank()) {
                        content.add(new Lesson.LessonContentBlock("latex", legacyLatex));
                    }
                }

                List<String> stepExpressions = new ArrayList<>();
                List<Point> stepPoints = new ArrayList<>();
                List<GraphShade> stepShades = new ArrayList<>();
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

                    Object rawStepShades = stepGraphMap.get("shades");
                    if (rawStepShades instanceof List<?> shadeList) {
                        for (Object item : shadeList) {
                            GraphShade parsedShade = parseShade(item);
                            if (parsedShade != null) {
                                stepShades.add(parsedShade);
                            }
                        }
                    }
                }

                steps.add(new Lesson.LessonStep(stepTitle, content, new Lesson.LessonGraph(stepExpressions, stepPoints, stepShades)));

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
        thumbnailGraph.put("shades",
                toShadeMaps(lesson.thumbnailGraph() != null ? lesson.thumbnailGraph().shades() : List.of()));
        map.put("thumbnailGraph", thumbnailGraph);

        List<Map<String, Object>> steps = new ArrayList<>();
        if (lesson.steps() != null) {
            for (Lesson.LessonStep step : lesson.steps()) {

                Map<String, Object> stepMap = new HashMap<>();
                stepMap.put("title", step.title());
                
                List<Map<String, Object>> content = new ArrayList<>();
                if (step.content() != null) {
                    for (Lesson.LessonContentBlock block : step.content()) {
                        if (block == null || block.type() == null || block.value() == null) {
                            continue;
                        }

                        String normalizedType = block.type().trim().toLowerCase();
                        if (!"text".equals(normalizedType) && !"latex".equals(normalizedType)) {
                            continue;
                        }

                        if (block.value().isBlank()) {
                            continue;
                        }

                        Map<String, Object> blockMap = new HashMap<>();
                        blockMap.put("type", normalizedType);
                        blockMap.put("value", block.value());
                        content.add(blockMap);
                    }
                }
                stepMap.put("content", content);

                Map<String, Object> stepGraph = new HashMap<>();
                stepGraph.put("expressions", step.graph() != null ? step.graph().expressions() : List.of());
                stepGraph.put("points", toPointMaps(step.graph() != null ? step.graph().points() : List.of()));
                stepGraph.put("shades", toShadeMaps(step.graph() != null ? step.graph().shades() : List.of()));
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

    private static List<Map<String, Object>> toShadeMaps(List<GraphShade> shades) {

        List<Map<String, Object>> mappedShades = new ArrayList<>();
        if (shades == null) {
            return mappedShades;
        }

        for (GraphShade shade : shades) {
            if (shade == null || shade.expression() == null || shade.expression().isBlank()) {
                continue;
            }

            Map<String, Object> mappedShade = new HashMap<>();
            mappedShade.put("leftEndpoint", shade.leftEndpoint());
            mappedShade.put("rightEndpoint", shade.rightEndpoint());
            mappedShade.put("expression", shade.expression());
            mappedShades.add(mappedShade);
        }

        return mappedShades;

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

    private static GraphShade parseShade(Object rawShade) {

        if (!(rawShade instanceof Map<?, ?> shadeMap)) {
            return null;
        }

        Object leftRaw = shadeMap.get("leftEndpoint");
        Object rightRaw = shadeMap.get("rightEndpoint");
        Object expressionRaw = shadeMap.get("expression");

        if (!(leftRaw instanceof Number left)
                || !(rightRaw instanceof Number right)
                || !(expressionRaw instanceof String expression)
                || expression.isBlank()) {
            return null;
        }

        return new GraphShade(left.doubleValue(), right.doubleValue(), expression);

    }

}
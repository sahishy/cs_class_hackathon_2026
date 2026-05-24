package com.sahishpeter.cs_class_hackathon_2026.features.ai.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.sahishpeter.cs_class_hackathon_2026.features.ai.types.LessonSchema;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.features.math.types.Point;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AIService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final String systemPrompt = "You are an expert STEM curriculum builder. Break down complex math topics into highly logical, bite-sized instructional sequences with math expressions or coordinate points.";
    private final String userPrompt = "Create a detailed structured lesson module about: ";

    public CompletableFuture<String> generateLesson(String input) {

        return CompletableFuture.supplyAsync(() -> {
            try {

                Part systemPromptPart = Part.builder().text(systemPrompt).build();
                Content systemPromptContent = Content.builder().role("system").parts(List.of(systemPromptPart)).build();

                GenerateContentConfig config = GenerateContentConfig.builder()
                        .responseMimeType("application/json")
                        .responseSchema(LessonSchema.LESSON_OUTPUT_SCHEMA)
                        .temperature(0.2f) 
                        .systemInstruction(systemPromptContent)
                        .build();

                String apiKey = resolveApiKey();
                Client client = Client.builder().apiKey(apiKey).build();

                GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash-lite", userPrompt + input, config);

                System.out.println(response.text());

                return response.text();

            } catch (Exception e) {
                throw new RuntimeException("gemini error: " + e.getMessage(), e);
            }
        });
    }

    private String resolveApiKey() {
        Dotenv dotenv = Dotenv.load();
        return dotenv.get("GOOGLE_API_KEY");
    }

    public Lesson parseLessonFromJson(String json, String lessonId, String userId, String question, long timestamp) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(json);

            String title = root.path("title").asText("Untitled Lesson");
            String topic = root.path("topic").asText("");
            Lesson.LessonGraph thumbnailGraph = parseGraph(root.path("thumbnailGraph"));
            List<Lesson.LessonStep> steps = parseSteps(root.path("steps"));

            return new Lesson(
                    lessonId,
                    userId,
                    question,
                    title,
                    topic,
                    timestamp,
                    timestamp,
                    thumbnailGraph,
                    steps);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI lesson JSON: " + e.getMessage(), e);
        }
    }

    private List<Lesson.LessonStep> parseSteps(JsonNode stepsNode) {
        List<Lesson.LessonStep> steps = new ArrayList<>();

        if (!stepsNode.isArray()) {
            return steps;
        }

        for (JsonNode stepNode : stepsNode) {

            String stepTitle = stepNode.path("title").asText("Step");
            String explanation = stepNode.path("explanation").asText("");
            String latex = stepNode.path("latex").asText("");
            Lesson.LessonGraph graph = parseGraph(stepNode.path("graph"));
            
            steps.add(new Lesson.LessonStep(stepTitle, explanation, latex, graph));
        }

        return steps;
    }

    private Lesson.LessonGraph parseGraph(JsonNode graphNode) {
        List<String> expressions = new ArrayList<>();
        List<Point> points = new ArrayList<>();

        JsonNode expressionsNode = graphNode.path("expressions");
        if (expressionsNode.isArray()) {
            for (JsonNode expressionNode : expressionsNode) {
                if (expressionNode.isTextual()) {
                    expressions.add(expressionNode.asText());
                }
            }
        }

        JsonNode pointsNode = graphNode.path("points");
        if (pointsNode.isArray()) {
            for (JsonNode pointNode : pointsNode) {
                if (pointNode.isArray() && pointNode.size() >= 2) {
                    JsonNode xNode = pointNode.get(0);
                    JsonNode yNode = pointNode.get(1);
                    if (xNode != null && yNode != null && xNode.isNumber() && yNode.isNumber()) {
                        points.add(new Point(xNode.asDouble(), yNode.asDouble()));
                    }
                }
            }
        }

        return new Lesson.LessonGraph(expressions, points);
    }

}
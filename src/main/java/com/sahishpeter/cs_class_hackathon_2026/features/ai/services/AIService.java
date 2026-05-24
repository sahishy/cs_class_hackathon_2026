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

    private final String systemPrompt = """
                You are an expert STEM curriculum builder. You must create a detailed structured lesson module about the user's question.

                You must output JSON matching the schema exactly.

                Each lesson step has an ordered content array:
                {"type":"text","value":"..."}
                {"type":"latex","value":"..."}

                ABSOLUTE RULES:
                1. Text blocks must contain words only.
                2. Text blocks must never contain mathematical notation.
                3. Text blocks must never contain:
                    - dollar signs: $
                    - backslash LaTeX commands: \\int, \\frac, \\sqrt, etc.
                    - variables written as symbols: x, y, n, f(x), dx
                    - equations or operators: =, +, -, *, /, ^, <, >
                    - coordinate pairs like (1, 2)
                    - inline math of any kind
                4. If a sentence needs math, split it into:
                    - one text block explaining the idea
                    - one latex block containing the math
                    - another text block continuing the explanation

                BAD text block:
                "We use the rule $\\int x^n dx = \\frac{x^{n+1}}{n+1}+C$, where C is constant."

                GOOD content blocks:
                [
                    {"type":"text","value":"We use the general power rule for integration."},
                    {"type":"latex","value":"\\int x^n\\,dx = \\frac{x^{n+1}}{n+1}+C"},
                    {"type":"text","value":"The extra constant accounts for all possible antiderivatives."}
                ]

                BAD text block:
                "We want to find the integral of f(x)=3x^2."

                GOOD content blocks:
                [
                    {"type":"text","value":"We want to find the antiderivative of the given function."},
                    {"type":"latex","value":"f(x)=3x^2"}
                ]

                Latex block rules:
                1. All formulas, equations, integrals, derivatives, functions, variables, symbolic expressions, coordinate points, and algebraic work must go in latex blocks.
                2. Latex blocks must contain valid LaTeX only.
                3. Do not put explanatory English sentences in latex blocks.

                Before returning JSON, silently check every text block. If it contains math notation, rewrite it by moving the math into a latex block.
            """;

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

                GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash-lite", input, config);

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
            List<Lesson.LessonContentBlock> content = parseContentBlocks(stepNode.path("content"));
            Lesson.LessonGraph graph = parseGraph(stepNode.path("graph"));

            steps.add(new Lesson.LessonStep(stepTitle, content, graph));
        }

        return steps;
    }

    private List<Lesson.LessonContentBlock> parseContentBlocks(JsonNode contentNode) {

        List<Lesson.LessonContentBlock> contentBlocks = new ArrayList<>();
        if (!contentNode.isArray()) {
            return contentBlocks;
        }

        for (JsonNode contentBlockNode : contentNode) {
            String type = contentBlockNode.path("type").asText("").trim().toLowerCase();
            String value = contentBlockNode.path("value").asText("");

            if (("text".equals(type) || "latex".equals(type)) && !value.isBlank()) {
                contentBlocks.add(new Lesson.LessonContentBlock(type, value));
            }
        }

        return contentBlocks;

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
package com.sahishpeter.cs_class_hackathon_2026.features.lessons.types;

import java.util.List;

public record Lesson(
    String id,
    String userId,
    String question,
    String title,
    String topic,
    long createdAt,
    long updatedAt,
    LessonGraph thumbnailGraph,
    List<LessonStep> steps
) {

    public record LessonStep(
        String title,
        String explanation,
        LessonGraph graph
    ) {}

    public record LessonGraph(
        String type,
        List<String> expressions,
        List<List<Double>> points
    ) {}
    
}
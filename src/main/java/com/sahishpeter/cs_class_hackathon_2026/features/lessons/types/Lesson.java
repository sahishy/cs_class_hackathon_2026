package com.sahishpeter.cs_class_hackathon_2026.features.lessons.types;

import java.util.List;

import com.sahishpeter.cs_class_hackathon_2026.features.math.types.Point;

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
        String latex,
        LessonGraph graph
    ) {}

    public record LessonGraph(
        List<String> expressions,
        List<Point> points
    ) {}
    
}
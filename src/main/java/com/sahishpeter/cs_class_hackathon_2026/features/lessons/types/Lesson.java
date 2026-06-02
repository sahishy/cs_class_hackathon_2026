package com.sahishpeter.cs_class_hackathon_2026.features.lessons.types;

import java.util.List;

import com.sahishpeter.cs_class_hackathon_2026.features.math.types.GraphShade;
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
        List<LessonContentBlock> content,
        LessonGraph graph
    ) {}

    public record LessonContentBlock(
        String type,
        String value
    ) {}

    public record LessonGraph(
        List<String> expressions,
        List<Point> points,
        List<GraphShade> shades
    ) {}
    
}


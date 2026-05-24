package com.sahishpeter.cs_class_hackathon_2026.features.lessons.types;

import java.util.List;

public record LessonMessage(
    String id, 
    String sender, 
    String title,
    String text, 
    List<String> latexSnippets,
    long createdAt
) {}

package com.sahishpeter.cs_class_hackathon_2026.features.lessons.types;

public record LessonMessage(
    String id, 
    String sender, 
    String text, 
    String latex,
    long createdAt
) {}

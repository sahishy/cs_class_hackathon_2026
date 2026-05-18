package com.sahishpeter.cs_class_hackathon_2026.features.lessons.services;

import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;

public class LessonService {

    //hi sahish
    //hi peter

    public static Lesson[] getLessons() {

        int n = 4;
        Lesson[] lessons = new Lesson[n];

        for(int i = 0; i < n; i++) {
            
            Lesson newLesson = new Lesson("user_123", "Lesson " + i);
            lessons[i] = newLesson;

        }

        return lessons;

    }
    
}

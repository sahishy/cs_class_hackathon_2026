package com.sahishpeter.cs_class_hackathon_2026.features.lessons.contexts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.google.cloud.firestore.ListenerRegistration;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.services.LessonService;
import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson;
import com.sahishpeter.cs_class_hackathon_2026.features.user.contexts.UserContext;
import com.sahishpeter.cs_class_hackathon_2026.shared.contexts.BaseContext;

import javafx.scene.Node;

public final class LessonContext extends BaseContext {

    private static final LessonContext INSTANCE = new LessonContext();

    private final LessonService lessonService = new LessonService();
    private final List<Lesson> lessons = new ArrayList<>();

    private ListenerRegistration lessonListener;
    private boolean initialized;

    public static LessonContext getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized void initialize() {

        if (initialized) {
            return;
        }

        String userId = UserContext.getCurrentUserId();
        lessonListener = lessonService.subscribeToLessons(userId, updatedLessons -> {

            synchronized (this) {
                lessons.clear();
                lessons.addAll(updatedLessons);
            }

            notifyListeners();

        });

        initialized = true;

    }

    @Override
    public synchronized void dispose() {

        if (lessonListener != null) {
            lessonListener.remove();
            lessonListener = null;
        }
        
        initialized = false;

    }

    public static List<Lesson> getLessons() {
        return INSTANCE.getLessonsInternal();
    }

    private synchronized List<Lesson> getLessonsInternal() {
        return Collections.unmodifiableList(new ArrayList<>(lessons));
    }

    public static void useLessons(Node node, Consumer<List<Lesson>> onLessonsChanged) {
        INSTANCE.useContext(node, () -> onLessonsChanged.accept(getLessons()));
    }

}
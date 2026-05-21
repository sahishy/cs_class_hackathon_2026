package com.sahishpeter.cs_class_hackathon_2026.features.user.contexts;

import com.sahishpeter.cs_class_hackathon_2026.shared.contexts.BaseContext;

import javafx.scene.Node;

import java.util.function.Consumer;

public final class UserContext extends BaseContext {

    private static final UserContext INSTANCE = new UserContext();
    private static final String DEFAULT_USER_ID = "hwee123";

    private String currentUserId = DEFAULT_USER_ID;
    private boolean initialized;

    public static UserContext getInstance() {
        return INSTANCE;
    }

    public static String getCurrentUserId() {
        return INSTANCE.currentUserId;
    }

    @Override
    public synchronized void initialize() {

        if (initialized) {
            return;
        }

        initialized = true;
        notifyListeners();

    }

    @Override
    public synchronized void dispose() {
        initialized = false;
    }

    public static void useUser(Node node, Runnable onUserChanged) {
        INSTANCE.useContext(node, onUserChanged);
    }

    public static void useUser(Node node, Consumer<String> onUserChanged) {
        INSTANCE.useContext(node, () -> onUserChanged.accept(getCurrentUserId()));
    }

}
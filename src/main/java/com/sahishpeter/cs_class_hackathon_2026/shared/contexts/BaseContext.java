package com.sahishpeter.cs_class_hackathon_2026.shared.contexts;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.application.Platform;
import javafx.scene.Node;

public abstract class BaseContext {

    private final List<Runnable> listeners = new CopyOnWriteArrayList<>();
    private final Map<Node, Runnable> nodeListeners = new IdentityHashMap<>();

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    public void removeListener(Runnable listener) {
        listeners.remove(listener);
    }

    protected void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    protected void useContext(Node node, Runnable onChange) {

        if (nodeListeners.containsKey(node)) {
            return;
        }

        Runnable sceneSafeOnChange = () -> Platform.runLater(onChange);
        nodeListeners.put(node, sceneSafeOnChange);

        node.sceneProperty().addListener((observable, oldScene, newScene) -> {

            if (oldScene == null && newScene != null) {
                if (!listeners.contains(sceneSafeOnChange)) {
                    addListener(sceneSafeOnChange);
                }
                sceneSafeOnChange.run();
            }

            if (oldScene != null && newScene == null) {
                removeListener(sceneSafeOnChange);
            }

        });

        if (!listeners.contains(sceneSafeOnChange)) {
            addListener(sceneSafeOnChange);
        }
        sceneSafeOnChange.run();

    }

    public void initialize() {}
    public void dispose() {}

}
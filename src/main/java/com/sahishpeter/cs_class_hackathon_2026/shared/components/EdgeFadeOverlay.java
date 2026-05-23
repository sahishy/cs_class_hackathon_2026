package com.sahishpeter.cs_class_hackathon_2026.shared.components;

import javafx.scene.layout.Region;

public class EdgeFadeOverlay extends Region {

    public enum Direction {
        TOP,
        BOTTOM
    }

    private static final double DEFAULT_HEIGHT = 36;

    public EdgeFadeOverlay(Direction direction) {
        this(direction, DEFAULT_HEIGHT);
    }

    public EdgeFadeOverlay(Direction direction, double height) {

        getStyleClass().add("edge-fade");
        getStyleClass().add(direction == Direction.TOP ? "edge-fade-top" : "edge-fade-bottom");

        setMouseTransparent(true);

        setMinHeight(Region.USE_PREF_SIZE);
        setPrefHeight(height);
        setMaxHeight(Region.USE_PREF_SIZE);
        setMaxWidth(Double.MAX_VALUE);

    }

}
package com.sahishpeter.cs_class_hackathon_2026.features.math.types;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class GraphFunction {

    private List<Point> points;
    private Color color;

    public GraphFunction() {
        this.points = new ArrayList<>();
        this.color = getRandomColor();
    }

    public List<Point> getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    private Color getRandomColor() {

        // Color[] colors = { Color.RED, Color.BLUE, Color.PURPLE, Color.GREEN, Color.ORANGE }; 
        Color[] colors = { Color.RED };
        int randomIndex = (int)(Math.random() * colors.length);

        return colors[randomIndex];

    }

}
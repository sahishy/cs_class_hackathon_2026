package com.sahishpeter.cs_class_hackathon_2026.features.calculator.components;

import com.sahishpeter.cs_class_hackathon_2026.features.calculator.types.Point;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

public class Calculator extends VBox {

    private Pane graph;
    private Pane pointHolder;

    private String function = "";
    private final int graphSizeCartesian = 10;
    private final int graphSizePixels = 300;

    public Calculator() {

        Label label = new Label("hwee");
        label.getStyleClass().add("test4");

        TextField input = new TextField("y = x");
        input.textProperty().addListener((observable, oldValue, newValue) -> {
            function = normalizeFunction(newValue);
            updateGraph();
        });

        function = normalizeFunction(input.getText());

        buildGraph();
        updateGraph();

        getChildren().addAll(label, input, graph);

    }

    private void updateGraph() {

        pointHolder.getChildren().clear();

        if(function.isBlank()) {
            return;
        }

        Expression expression;
        try {
            expression = new ExpressionBuilder(function).variable("x").build().setVariable("x", 0);
        } catch (RuntimeException ex) {
            return;
        }

        ValidationResult result = expression.validate();

        // dont graph anything if expression not valid
        if(!result.isValid()) return;

        double xMin = -(graphSizeCartesian / 2.0);
        double xMax = graphSizeCartesian / 2.0;
        double step = 0.01;

        for (double x = xMin; x <= xMax; x += step) {
            graphPoint(expression, x);
        }

    }

    private void buildGraph() {

        graph = new Pane();
        graph.setPrefSize(graphSizePixels, graphSizePixels);
        graph.setMaxSize(graphSizePixels, graphSizePixels);
        graph.getStyleClass().add("graph");

        pointHolder = new Pane();
        pointHolder.setPrefSize(graphSizePixels, graphSizePixels);
        pointHolder.setMaxSize(graphSizePixels, graphSizePixels);
        pointHolder.setClip(new Rectangle(graphSizePixels, graphSizePixels));
        pointHolder.getStyleClass().add("point-holder");

        Rectangle xAxis = new Rectangle(graphSizePixels, 1);
        xAxis.relocate(0, graphSizePixels / 2.0);
        xAxis.getStyleClass().add("axis");

        Rectangle yAxis = new Rectangle(1, graphSizePixels);
        yAxis.relocate(graphSizePixels / 2.0, 1);
        yAxis.getStyleClass().add("axis");

        double scalingFactor = (double) graphSizePixels / graphSizeCartesian;

        for (double line = scalingFactor; line < graphSizePixels; line += scalingFactor) {

            Rectangle xLine = new Rectangle(graphSizePixels - 2, 1);
            xLine.relocate(1, line);
            xLine.getStyleClass().add("gridline");

            Rectangle yLine = new Rectangle(1, graphSizePixels - 2);
            yLine.relocate(line, 1);
            yLine.getStyleClass().add("gridline");

            graph.getChildren().addAll(xLine, yLine);

        }

        graph.getChildren().addAll(xAxis, yAxis, pointHolder);

    }

    private void graphPoint(Expression expression, double x) {

        double y = evaluateFunction(expression, x);

        if (!Double.isFinite(y)) {
            return;
        }

        double scalingFactor = (double) graphSizePixels / graphSizeCartesian;

        double xPixels = (x * scalingFactor) + (graphSizePixels / 2.0);
        double yPixels = (graphSizePixels / 2.0) - (y * scalingFactor);

        if (xPixels < 0 || xPixels > graphSizePixels || yPixels < 0 || yPixels > graphSizePixels) {
            return;
        }

        double radius = 1;

        Circle circle = new Circle(radius);
        circle.getStyleClass().add("circle");
        circle.relocate(xPixels - radius, yPixels - radius);

        pointHolder.getChildren().add(circle);

    }

    private double evaluateFunction(Expression expression, double x) {

        try {

            expression.setVariable("x", x);
            return expression.evaluate();

        } catch (RuntimeException ex) {
            return Double.NaN;
        }

    }

    private String normalizeFunction(String func) {

        String normalized = func.toLowerCase().trim();
        normalized = normalized.replaceAll("^y\\s*=\\s*", "");
        normalized = normalized.replaceAll("\\s+", "");

        // support stuff like 2x, x2, x(x+1), (x+1)(x-1), 2(x+1)
        normalized = normalized.replaceAll("(?<=[0-9)])(?=[a-z(])", "*");
        normalized = normalized.replaceAll("(?<=[a-z])(?=[0-9(])", "*");

        return normalized;

    }

    private void connectPoint(Point a, Point b){
        
        return;
    }

}

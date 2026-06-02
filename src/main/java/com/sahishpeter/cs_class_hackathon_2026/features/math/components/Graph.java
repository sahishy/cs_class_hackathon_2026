package com.sahishpeter.cs_class_hackathon_2026.features.math.components;

import java.util.ArrayList;
import java.util.List;

import com.sahishpeter.cs_class_hackathon_2026.features.lessons.types.Lesson.LessonGraph;
import com.sahishpeter.cs_class_hackathon_2026.features.math.types.GraphFunction;
import com.sahishpeter.cs_class_hackathon_2026.features.math.types.GraphShade;
import com.sahishpeter.cs_class_hackathon_2026.features.math.types.Point;
import com.sahishpeter.cs_class_hackathon_2026.features.math.utils.GraphUtils;
import com.sahishpeter.cs_class_hackathon_2026.features.math.utils.MathUtils;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

public class Graph extends VBox {

    private Pane graph;
    private Pane pointHolder;

    private LessonGraph lessonGraph;
    private List<String> normalizedFunctions = new ArrayList<>();

    private final int graphSizeCartesian = 10;
    private final double radius = 1;
    private final Color lineColor = Color.RED;

    private int graphSizePixels;
    private boolean border;

    List<GraphFunction> graphFunctions = new ArrayList<>();

    public Graph(LessonGraph lessonGraph, int graphSizePixels, boolean border) {
        
        this.graphSizePixels = graphSizePixels;
        this.border = border;

        setAlignment(Pos.CENTER);

        buildGraph();
        updateGraph(lessonGraph);

        getChildren().addAll(graph);

    }

    private void normalizeAllExpressions() {

        normalizedFunctions.clear();

        for (String s : lessonGraph.expressions()) {
            String function = MathUtils.normalizeFunction(s);
            normalizedFunctions.add(function);
        }

    }

    public void updateGraph(LessonGraph lessonGraph) {

        this.lessonGraph = lessonGraph;

        normalizeAllExpressions();
        updateGraph();

    }

    public void updateGraph() {

        clearGraphFunctions();

        for (String function : normalizedFunctions) {

            if (function.isBlank()) {
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
            if (!result.isValid())
                return;

            graphFunction(expression);

        }

        if (lessonGraph.shades() == null)
            return;
        for (GraphShade shade : lessonGraph.shades()) {

            String function = MathUtils.normalizeFunction(shade.expression());

            if (function.isBlank()) {
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
            if (!result.isValid())
                return;

            shadeAreaUnderFunction(expression, shade);

        }

    }

    private void buildGraph() {

        graph = new Pane();
        graph.setPrefSize(graphSizePixels, graphSizePixels);
        graph.setMaxSize(graphSizePixels, graphSizePixels);
        graph.getStyleClass().add("graph");
        if(border) {
            graph.getStyleClass().add("border");
        }

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

    private void graphPoint(List<Point> points, Color color, Expression expression, double x) {

        double y = MathUtils.evaluateFunction(expression, x);

        if (!Double.isFinite(y)) {
            return;
        }

        Point pixelsPoint = GraphUtils.cartesianToPixels(new Point(x, y), graphSizePixels, graphSizeCartesian);

        Circle circle = new Circle(radius);
        circle.setFill(lineColor);
        circle.relocate(pixelsPoint.x() - radius, pixelsPoint.y() - radius);
        pointHolder.getChildren().add(circle);

        Point point = new Point(pixelsPoint.x(), pixelsPoint.y());
        points.add(point);

    }

    private Line connectPoint(Point a, Point b) {
        Line line = new Line(a.x(), a.y(), b.x(), b.y());
        return line;
    }

    private void connectFunctionPoints(List<Point> points, Color color) {

        for (int i = 0; i < points.size() - 1; i++) {

            Line connectedLine = connectPoint(points.get(i), points.get(i + 1));
            connectedLine.setStrokeWidth(2 * radius);
            connectedLine.setStroke(color);
            pointHolder.getChildren().add(connectedLine);

        }

    }

    private void graphFunction(Expression expression) {

        GraphFunction graphFunction = new GraphFunction();
        graphFunctions.add(graphFunction);

        List<Point> points = graphFunction.getPoints();
        Color color = graphFunction.getColor();

        double xMin = -(graphSizeCartesian / 2.0);
        double xMax = graphSizeCartesian / 2.0;
        double step = 0.01;

        for (double x = xMin; x <= xMax; x += step) {
            graphPoint(points, color, expression, x);
        }

        connectFunctionPoints(points, color);

    }

    private void clearGraphFunctions() {
        pointHolder.getChildren().clear();
        graphFunctions.clear();
    }

    private void shadeAreaUnderFunction(Expression expression, GraphShade shade) {

        double step = 0.001;
        double xMin = -(graphSizeCartesian / 2.0);
        double xMax = (graphSizeCartesian / 2.0);
        double axisY = graphSizePixels / 2.0;

        for (double x = xMin; x <= xMax - step; x += step) {

            if (x < shade.leftEndpoint() || x > shade.rightEndpoint()) {
                continue;
            }

            double y = MathUtils.evaluateFunction(expression, x);
            if (!Double.isFinite(y)) {
                continue;
            }

            Point pixelsPoint = GraphUtils.cartesianToPixels(new Point(x, y), graphSizePixels, graphSizeCartesian);

            double scalingFactor = (double) graphSizePixels / graphSizeCartesian;
            double rectHeight = Math.abs(axisY - pixelsPoint.y());
            double rectWidth = step * scalingFactor;
            double rectY = Math.min(axisY, pixelsPoint.y());

            Rectangle rectangle = new Rectangle();
            rectangle.relocate(pixelsPoint.x(), rectY);
            rectangle.setWidth(rectWidth);
            rectangle.setHeight(rectHeight);

            Color color = graphFunctions.size() > 0 ? graphFunctions.getFirst().getColor() : Color.RED;
            rectangle.setFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.5));

            pointHolder.getChildren().add(rectangle);

        }

    }

}
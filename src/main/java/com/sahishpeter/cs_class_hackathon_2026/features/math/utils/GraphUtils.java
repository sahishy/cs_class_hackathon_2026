package com.sahishpeter.cs_class_hackathon_2026.features.math.utils;

import com.sahishpeter.cs_class_hackathon_2026.features.math.types.Point;

public class GraphUtils {
    
    public static Point cartesianToPixels(Point cartesianPoint, int graphSizePixels, int graphSizeCartesian) {

        double x = cartesianPoint.x();
        double y = cartesianPoint.y();

        double scalingFactor = (double) graphSizePixels / graphSizeCartesian;

        double xPixels = (x * scalingFactor) + (graphSizePixels / 2.0);
        double yPixels = (graphSizePixels / 2.0) - (y * scalingFactor);

        return new Point(xPixels, yPixels);

    }

}

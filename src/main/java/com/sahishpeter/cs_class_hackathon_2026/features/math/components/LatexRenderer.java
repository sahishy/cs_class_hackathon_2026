package com.sahishpeter.cs_class_hackathon_2026.features.math.components;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class LatexRenderer {

    public static ImageView render(String latex, float size) {

        int scale = 3;

        TeXFormula formula = new TeXFormula(latex);

        java.awt.Image awtImage = formula.createBufferedImage(
            TeXConstants.STYLE_DISPLAY,
            size * scale,
            java.awt.Color.BLACK,
            null
        );

        BufferedImage bufferedImage = new BufferedImage(
            awtImage.getWidth(null),
            awtImage.getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = bufferedImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.drawImage(awtImage, 0, 0, null);
        g2.dispose();

        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));

        imageView.setFitWidth(bufferedImage.getWidth() / scale);
        imageView.setFitHeight(bufferedImage.getHeight() / scale);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        return imageView;

    }
    
}
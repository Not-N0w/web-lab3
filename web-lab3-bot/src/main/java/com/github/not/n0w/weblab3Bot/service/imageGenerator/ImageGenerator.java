package com.github.not.n0w.weblab3Bot.service.imageGenerator;

import com.github.not.n0w.weblab3Bot.model.Hit;

import javax.imageio.ImageIO;
import java.awt.geom.Arc2D;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageGenerator {
    private int width = 1000;
    private int height = 1000;
    private double localR = 400;

    private int hatchLength = 12;
    private int letterHeight = 20;

    private Color backgroundColor = new Color(26, 26, 29);
    private Color lightColor = new Color(245, 245, 245);
    private Color figureColor = new Color(195, 7, 63);
    private Color hitColor = Color.GREEN;
    private Color missColor = Color.RED;

    private static void drawHatchX(Graphics2D g, double x, int len) {
        g.drawLine((int)x, -len/2, (int)x, len/2);
    }

    private static void drawHatchY(Graphics2D g, double y, int len) {
        g.drawLine(-len/2, (int)y, len/2, (int)y);
    }
    private int getStringWidth(Graphics2D graphics, String str) {
        String text = String.valueOf(str);
        FontMetrics fm = graphics.getFontMetrics();
        return fm.stringWidth(text);
    }


    private void drawAxisCaptions(Graphics2D graphics, double localR, int cx, int cy, double globalR) {
        graphics.setFont(new Font("Courier New", Font.BOLD, letterHeight));

        String negRStr = String.valueOf(-globalR);
        String negHRStr = String.valueOf(-globalR/2);
        String hRStr = String.valueOf(globalR/2);
        String rStr = String.valueOf(globalR);

        graphics.scale(1, -1);

        graphics.drawString(negRStr, (int)-(localR + getStringWidth(graphics, negRStr)/2), (letterHeight + 4));
        graphics.drawString(negHRStr, (int)-(localR/2 + getStringWidth(graphics, negHRStr)/2), (letterHeight + 4));
        graphics.drawString(hRStr, (int)localR/2 - getStringWidth(graphics, hRStr)/2, (letterHeight + 4));
        graphics.drawString(rStr, (int)localR - getStringWidth(graphics, rStr)/2, (letterHeight + 4));

        graphics.drawString("X", cx - graphics.getFontMetrics().stringWidth("X") - 5, (letterHeight + 4));

        graphics.drawString(negRStr, -(getStringWidth(graphics, negRStr) + hatchLength/2+ 4), (int)-localR + letterHeight/2 - 2);
        graphics.drawString(negHRStr, -(getStringWidth(graphics, negHRStr) + hatchLength/2+ 4), (int)-localR/2 + letterHeight/2 - 2);
        graphics.drawString(hRStr, -(getStringWidth(graphics, hRStr) + hatchLength/2+ 4), (int)localR/2 + letterHeight/2 - 2);
        graphics.drawString(rStr, -(getStringWidth(graphics, rStr) + hatchLength/2+ 4), (int)localR + letterHeight/2 - 2);

        graphics.drawString("Y", -(letterHeight + 10), -(cy - letterHeight));
        graphics.scale(1, -1);
    }

    private void drawAxis(Graphics2D graphics, double localR, int cx, int cy, double globalR) {
        graphics.setColor(lightColor);
        graphics.setStroke(new BasicStroke(2));
        graphics.drawLine(-cx, 0, cx, 0);

        drawHatchX(graphics, localR/2, hatchLength);
        drawHatchX(graphics, localR, hatchLength);
        drawHatchX(graphics, -localR/2, hatchLength);
        drawHatchX(graphics, -localR, hatchLength);

        Polygon arrowX = new Polygon();
        arrowX.addPoint(cx - hatchLength, hatchLength/2);
        arrowX.addPoint(cx, 0);
        arrowX.addPoint(cx - hatchLength, -hatchLength/2);
        graphics.fillPolygon(arrowX);

        graphics.drawLine(0, -cy, 0, cy);

        drawHatchY(graphics, localR/2, hatchLength);
        drawHatchY(graphics, localR, hatchLength);
        drawHatchY(graphics, -localR/2, hatchLength);
        drawHatchY(graphics, -localR, hatchLength);

        Polygon arrowY = new Polygon();
        arrowY.addPoint(-hatchLength/2, cy - hatchLength);
        arrowY.addPoint(0, cy);
        arrowY.addPoint(hatchLength/2, cy - hatchLength);
        graphics.fillPolygon(arrowY);

        drawAxisCaptions(graphics, localR, cx, cy, globalR);
    }

    private void drawFigure(Graphics2D graphics, double localR) {
        graphics.setColor(figureColor);

        Path2D figure = new Path2D.Double();
        figure.moveTo(0, 0);

        double r = localR / 2;
        figure.append(new Arc2D.Double(-r, -r, 2*r, 2*r, 0, -90, Arc2D.OPEN), true);

        figure.moveTo(r, 0);
        figure.lineTo(0, -localR);
        figure.lineTo(0, -r);

        figure.lineTo(-localR, -r);
        figure.lineTo(-localR, 0);
        figure.lineTo(0, 0);

        figure.closePath();
        graphics.fill(figure);
    }



    public void drawHitPoints(Graphics2D graphics,  List<Double> globalX, Double globalY, Double globalR) {
        if (globalX == null || globalY == null || globalR == null) {
            return;
        }

        graphics.setColor(hitColor);
        graphics.setStroke(new BasicStroke(5));

        for (Double x : globalX) {
            int scaledX = (int) (x * localR / globalR);
            int scaledY = (int) (globalY * localR / globalR);

            graphics.drawLine(scaledX - 5, scaledY, scaledX + 5, scaledY);
            graphics.drawLine(scaledX, scaledY - 5, scaledX, scaledY + 5);
        }
    }

    public void drawHistoryPoints(Graphics2D graphics, List<Hit> hits) {
        if (hits == null || hits.isEmpty()) {
            return;
        }

        graphics.setStroke(new BasicStroke(5));

        for (Hit hit : hits) {
            int scaledX = (int) (hit.getX().doubleValue() * localR / hit.getR().doubleValue());
            int scaledY = (int) (hit.getY().doubleValue() * localR / hit.getR().doubleValue());

            graphics.setColor(hit.getHit() ? hitColor : missColor);

            graphics.drawOval(
                    scaledX - 3,
                    scaledY - 3,
                    6,
                    6
            );
        }
    }

    private File generateImageFile(List<Double> globalX, Double globalY, Double globalR, List<Hit> hits) {
        int cx = width / 2;
        int cy = height / 2;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = img.createGraphics();

        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, width, height);

        graphics.translate(cx, cy);
        graphics.scale(1, -1);

        drawFigure(graphics, localR);
        drawAxis(graphics, localR, cx, cy, globalR);
        drawHitPoints(graphics, globalX, globalY, globalR);
        drawHistoryPoints(graphics, hits);

        graphics.dispose();

        File file = new File("figure.png");
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
    public File generateAimImageFile(List<Double> globalX, Double globalY, Double globalR) {
        return generateImageFile(globalX, globalY, globalR, null);
    }
    public File generateHistoryImageFile(List<Hit> hits) {
        if (hits == null || hits.isEmpty()) { return null; }
        return generateImageFile(null, null, hits.get(0).getR().doubleValue(), hits);
    }


}

package binchat.graphing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class GraphingManager{

    public final int SCREEN_WIDTH = 1000;
    public final int SCREEN_HEIGHT = 600;
    int INTERVALS = 8;

    // gets the x double value for a give pixel
    public double xValue(int x_pixel_index, double lower_x, double upper_x) {
        return lower_x + x_pixel_index * ((upper_x - lower_x) / SCREEN_WIDTH);
    }

    // gets the y double value of a given pixel
    public double yValue(int y_pixel_index, double lower_y, double upper_y) {
        return upper_y - y_pixel_index * ((upper_y - lower_y) / SCREEN_HEIGHT);
    }

    public int xIndex(double x_value, double lower_x, double upper_x) {
        return (int) ((x_value - lower_x) / ((upper_x - lower_x) / SCREEN_WIDTH));
    }

    public int yIndex(double y_value, double lower_y, double upper_y) {
        return (int) ((upper_y - y_value) / ((upper_y - lower_y) / SCREEN_HEIGHT));
    }

    // was written for graphing rational functions
    /*
    public BufferedImage plotFunction(Rational rational, double xmin, double xmax, double ymin, double ymax) {
        DecimalFormat df = new DecimalFormat("#.##");
        BufferedImage buffimg = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) buffimg.getGraphics();
        g.setBackground(Color.white);
        g.setColor(Color.white);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.PLAIN, 20));

        return buffimg;
    }
*/
    // Creates a buffered image of a given graph
    public BufferedImage plotFunction(Polynomial poly, double xmin, double xmax, double ymin, double ymax) {
        DecimalFormat df = new DecimalFormat("#.##");
        BufferedImage buffimg = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) buffimg.getGraphics();
        g.setBackground(Color.white);
        g.setColor(Color.white);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.PLAIN, 20));

        // draw x-axis
        g.drawLine(0, yIndex(0, ymin, ymax), SCREEN_WIDTH - 20, yIndex(0, ymin, ymax));
        g.drawString("X", SCREEN_WIDTH - 20, yIndex(0, ymin, ymax));

        // x axis ticks and scale labelling
        int y_position;
        // if axis is off screen, set y-position along bottom of the screen
        if (yIndex(0, ymin, ymax) < 0 || SCREEN_HEIGHT < yIndex(0, ymin, ymax))
            y_position = SCREEN_HEIGHT;
            // else set y position along the axis
        else
            y_position = yIndex(0, ymin, ymax);
        // draws ticks and labels them
        for (int i = 0; i < INTERVALS; i++) {
            double x = xValue(i * SCREEN_WIDTH / INTERVALS, xmin, xmax);
            x = Math.round(x * 100);
            x = x / 100;
            int x_position = i * SCREEN_WIDTH / INTERVALS;
            g.drawString(String.valueOf(df.format(x)), x_position + 5, y_position - 5);
            g.drawLine(x_position, y_position - 5, x_position, y_position + 5);
        }
        // draw y-axis
        g.drawLine(xIndex(0, xmin, xmax), 20, xIndex(0, xmin, xmax), SCREEN_HEIGHT);
        g.drawString("Y", xIndex(0, xmin, xmax), 20);

        int x_position;
        // if axis is off screen, set x-position along left of the screen
        if (xIndex(0, xmin, xmax) < 0 || SCREEN_WIDTH < xIndex(0, xmin, xmax))
            x_position = 0;
            // else set x position along the axis
        else
            x_position = xIndex(0, xmin, xmax);

        // draws ticks and labels them
        for (int i = 1; i <= INTERVALS; i++) {
            double y = yValue(i * SCREEN_HEIGHT / INTERVALS, ymin, ymax);
            y = Math.round(y * 100);
            y = y / 100;
            y_position = i * SCREEN_HEIGHT / INTERVALS;
            g.drawString(String.valueOf(df.format(y)), x_position + 10, y_position + 5);
            g.drawLine(x_position - 5, y_position, x_position + 5, y_position);
        }

        // Graphs the function
        g.setColor(Color.RED);
        double previous_x = xValue(0, xmin, xmax);
        double previous_y = poly.evaluate(previous_x);
        for (int i = 1; i < SCREEN_WIDTH; i += 1) {
            double current_x = xValue(i, xmin, xmax);
            double current_y = poly.evaluate(current_x);
            g.drawLine(i - 1, yIndex(previous_y, ymin, ymax), i, yIndex(current_y, ymin, ymax));
            previous_x = current_x;
            previous_y = current_y;
        }
        // labels the function at the top of the screen
        g.drawString(poly.getEquation(), 10, 40);
        return buffimg;
    }
}
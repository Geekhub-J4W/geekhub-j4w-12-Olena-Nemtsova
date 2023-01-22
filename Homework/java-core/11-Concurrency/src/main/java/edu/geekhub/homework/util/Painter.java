package edu.geekhub.homework.util;

import edu.geekhub.homework.track.Block;
import edu.geekhub.homework.track.Field;
import edu.geekhub.homework.track.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.util.List;

public class Painter extends JComponent {
    private static Dimension dimension;
    private static Field field;

    @Override
    public void paint(Graphics g) {
        List<Point> startBlockPoints = field.getStartBlock().getPoints();
        drawRectanglesByPoints(g, startBlockPoints, java.awt.Color.GREEN);

        List<Point> trackBlockPoints = field.getTrackBlocks().stream().map(Block::getPoints).flatMap(List::stream).toList();
        drawRectanglesByPoints(g, trackBlockPoints, java.awt.Color.GRAY);

        List<Point> finishBlockPoints = field.getFinishBlock().getPoints();
        drawRectanglesByPoints(g, finishBlockPoints, java.awt.Color.BLACK);
    }

    private void drawRectanglesByPoints(Graphics g, List<Point> points, java.awt.Color color) {
        g.setColor(color);
        int i = 0;
        for (Point point : points) {
            point = new Point(point.i() * 24 + (dimension.height - 150) / 2, point.j() * 24 + (dimension.height - 150) / 2);

            if (color.equals(Color.BLACK) && i % 2 == 0) {
                g.drawRect(point.i(), point.j(), 20, 20);
            } else {
                g.fillRect(point.i(), point.j(), 20, 20);
            }
            i++;
        }
    }

    public static JFrame getJFrame() {
        JFrame jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        dimension = toolkit.getScreenSize();
        jFrame.setBounds((dimension.width - dimension.height) / 2, 50, dimension.height - 100, dimension.height - 100);
        return jFrame;
    }

    public static void paintTrack(Field f) {
        field = f;
        JFrame jFrame = getJFrame();
        jFrame.add(new Painter());
    }
}

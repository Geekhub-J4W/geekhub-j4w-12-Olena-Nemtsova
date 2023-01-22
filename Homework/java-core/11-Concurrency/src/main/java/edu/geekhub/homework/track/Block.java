package edu.geekhub.homework.track;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Block {
    private final List<Point> points;

    private Block(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public static Block generateBlock(Point centralPoint) {
        Point firstPoint = new Point(centralPoint.i() - 1, centralPoint.j() - 1);
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                points.add(new Point(firstPoint.i() + i, firstPoint.j() + j));
            }
        }
        return new Block(points);
    }

    public Point getCentralPoint() {
        return points.get(4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Block block = (Block) o;
        return points.equals(block.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }
}

package edu.geekhub.homework.track;

import edu.geekhub.homework.util.Course;

public record Point(int i, int j) {
    public Point generateNewPointByCourse(Course course) {
        switch (course) {
            case DOWN -> {
                return new Point(i + 3, j);
            }
            case UP -> {
                return new Point(i - 3, j);
            }
            case LEFT -> {
                return new Point(i, j - 3);
            }
            default -> {
                return new Point(i, j + 3);
            }
        }
    }

    public Point generateNewPointByCourse(Course course, int movesCount) {
        switch (course) {
            case DOWN -> {
                return new Point(i + movesCount, j);
            }
            case UP -> {
                return new Point(i - movesCount, j);
            }
            case LEFT -> {
                return new Point(i, j - movesCount);
            }
            default -> {
                return new Point(i, j + movesCount);
            }
        }
    }
}

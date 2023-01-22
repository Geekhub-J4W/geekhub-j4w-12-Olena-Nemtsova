package edu.geekhub.homework.track;

import edu.geekhub.homework.util.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    private Point point;

    @BeforeEach
    void setUp() {
        point = new Point(0, 0);
    }

    @Test
    void can_generate_new_point_by_course() {
        Point pointUP = point.generateNewPointByCourse(Course.UP);
        Point pointDOWN = point.generateNewPointByCourse(Course.DOWN);
        Point pointLEFT = point.generateNewPointByCourse(Course.LEFT);
        Point pointRIGHT = point.generateNewPointByCourse(Course.RIGHT);

        Point expectedPointUP = new Point(-3, 0);
        Point expectedPointDOWN = new Point(3, 0);
        Point expectedPointLEFT = new Point(0, -3);
        Point expectedPointRIGHT = new Point(0, 3);

        assertEquals(expectedPointUP, pointUP);
        assertEquals(expectedPointDOWN, pointDOWN);
        assertEquals(expectedPointLEFT, pointLEFT);
        assertEquals(expectedPointRIGHT, pointRIGHT);
    }

    @Test
    void can_generate_new_point_by_course_and_moves_count() {
        Point pointUP = point.generateNewPointByCourse(Course.UP, 1);
        Point pointDOWN = point.generateNewPointByCourse(Course.DOWN, 1);
        Point pointLEFT = point.generateNewPointByCourse(Course.LEFT, 2);
        Point pointRIGHT = point.generateNewPointByCourse(Course.RIGHT, 2);

        Point expectedPointUP = new Point(-1, 0);
        Point expectedPointDOWN = new Point(1, 0);
        Point expectedPointLEFT = new Point(0, -2);
        Point expectedPointRIGHT = new Point(0, 2);

        assertEquals(expectedPointUP, pointUP);
        assertEquals(expectedPointDOWN, pointDOWN);
        assertEquals(expectedPointLEFT, pointLEFT);
        assertEquals(expectedPointRIGHT, pointRIGHT);

    }

    @Test
    void can_get_coordinate_i() {
        int i = point.i();
        int expected_i = 0;

        assertEquals(expected_i, i);
    }

    @Test
    void can_get_coordinate_j() {
        int j = point.j();
        int expected_j = 0;

        assertEquals(expected_j, j);
    }
}

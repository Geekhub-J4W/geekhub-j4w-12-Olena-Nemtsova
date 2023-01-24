package edu.geekhub.homework.track;

import edu.geekhub.homework.model.Car;
import edu.geekhub.homework.model.Transport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static org.junit.jupiter.api.Assertions.*;

class FieldControllerTest {
    private FieldController controller;
    private Field field;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        FieldGenerator generator = new FieldGenerator();
        field = generator.generateField(2);
        controller = new FieldController(field);
    }

    @Test
    void can_check_is_finish_point() {
        Point finishPoint = field.finishBlock().getPoints().get(0);
        Point notFinishPoint = field.startBlock().getPoints().get(0);

        boolean finishPointFlag = controller.isFinishPoint(finishPoint);
        boolean notFinishPointFlag = controller.isFinishPoint(notFinishPoint);

        assertTrue(finishPointFlag);
        assertFalse(notFinishPointFlag);
    }

    @Test
    void can_check_is_field_point() {
        Point fieldPoint = field.startBlock().getPoints().get(0);
        Point notFieldPoint = new Point(100, 100);

        boolean finishPointFlag = controller.isFieldPoint(fieldPoint);
        boolean notFinishPointFlag = controller.isFieldPoint(notFieldPoint);

        assertTrue(finishPointFlag);
        assertFalse(notFinishPointFlag);
    }

    @Test
    void can_check_is_point_occupied() {
        Point occupyPoint = new Point(0, 0);
        Point freePoint = new Point(1, 0);
        controller.occupyPoint(occupyPoint, null, null);

        assertTrue(controller.isPointOccupied(occupyPoint));
        assertFalse(controller.isPointOccupied(freePoint));
    }

    @Test
    void can_release_point() {
        Point point = new Point(0, 0);
        controller.occupyPoint(point, null, null);

        controller.releasePoint(point);

        assertFalse(controller.isPointOccupied(point));
    }

    @Test
    void can_get_free_start_point() {
        Point expectedFreePoint = new Point(0, 0);
        List<Point> occupiedPoints = field.startBlock().getPoints().stream()
            .filter(point -> !point.equals(expectedFreePoint)).toList();
        for (Point point : occupiedPoints) {
            controller.occupyPoint(point, null, null);
        }

        Point freePoint = controller.occupyRandomFreeStartPoint(null);

        assertEquals(expectedFreePoint, freePoint);
    }

    @Test
    void can_synchronized_get_free_start_point() throws ExecutionException, InterruptedException {
        List<Future<Point>> futureList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            FutureTask<Point> future = new FutureTask<>(getPoint());
            Thread thread = new Thread(future);
            thread.start();
            futureList.add(future);
        }

        List<Point> occupiedPoints = new ArrayList<>();
        for (Future<Point> f : futureList) {
            occupiedPoints.add(f.get());
        }
        occupiedPoints.sort((o1, o2) -> {
            int isEqual = o1.i() - o2.i();
            if (isEqual == 0) {
                return o1.j() - o2.j();
            }
            return isEqual;
        });

        assertEquals(field.startBlock().getPoints(), occupiedPoints);
    }

    private Callable<Point> getPoint() {
        Point point = controller.occupyRandomFreeStartPoint(null);
        controller.occupyPoint(point, null, null);
        return () -> point;
    }

    @Test
    void can_get_transport_at_occupy_point() throws NoSuchAlgorithmException {
        Point occupyPoint = new Point(0, 0);
        Transport car = new Car(controller);
        controller.occupyPoint(occupyPoint, null, car);

        assertEquals(car, controller.getTransportAtPoint(occupyPoint));
    }
}

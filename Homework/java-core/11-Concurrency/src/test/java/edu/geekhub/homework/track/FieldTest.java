package edu.geekhub.homework.track;

import edu.geekhub.homework.model.Car;
import edu.geekhub.homework.model.Transport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class FieldTest {
    private Field field;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        field = new Field(1);
    }

    @Test
    void can_not_create_field_with_track_blocks_count_less_than_1() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> new Field(0)
        );

        assertEquals("Count of track blocks must be more than 1", thrown.getMessage());
    }

    @Test
    void can_not_create_field_with_track_blocks_count_more_than_10() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> new Field(100)
        );

        assertEquals("Count of track blocks must be less than 10", thrown.getMessage());
    }

    @Test
    void can_generate_field() throws NoSuchAlgorithmException {
        Field field = spy(this.field);
        doNothing().when(field).generateStartBlock();
        doNothing().when(field).generateTrackBlocks(anyInt());
        doNothing().when(field).generateFinishBlock();

        field.generateField(2);

        verify(field, times(1)).generateStartBlock();
        verify(field, times(1)).generateTrackBlocks(anyInt());
        verify(field, times(1)).generateFinishBlock();
    }

    @Test
    void can_generate_start_block() {
        Block expectedStartBlock = Block.generateBlock(new Point(0, 0));

        assertEquals(expectedStartBlock, field.getStartBlock());
    }

    @Test
    void can_generate_track_blocks() throws NoSuchAlgorithmException {
        field.generateTrackBlocks(2);
        int trackBlocksCount = field.getTrackBlocks().size();

        assertEquals(2, trackBlocksCount);
    }

    @Test
    void can_generate_track_blocks_not_over_start_block() throws NoSuchAlgorithmException {
        field.generateTrackBlocks(10);
        List<Block> trackBlocks = field.getTrackBlocks();

        Block startBlock = field.getStartBlock();

        assertFalse(trackBlocks.contains(startBlock));
    }

    @Test
    void can_generate_finish_block() {
        Block finishBlock = field.getFinishBlock();

        assertNotNull(finishBlock);
    }

    @Test
    void can_check_is_finish_point() {
        Point finishPoint = field.getFinishBlock().getPoints().get(0);
        Point notFinishPoint = field.getStartBlock().getPoints().get(0);

        boolean finishPointFlag = field.isFinishPoint(finishPoint);
        boolean notFinishPointFlag = field.isFinishPoint(notFinishPoint);

        assertTrue(finishPointFlag);
        assertFalse(notFinishPointFlag);
    }

    @Test
    void can_check_is_field_point() {
        Point fieldPoint = field.getStartBlock().getPoints().get(0);
        Point notFieldPoint = new Point(100, 100);

        boolean finishPointFlag = field.isFieldPoint(fieldPoint);
        boolean notFinishPointFlag = field.isFieldPoint(notFieldPoint);

        assertTrue(finishPointFlag);
        assertFalse(notFinishPointFlag);
    }

    @Test
    void can_generate_finish_block_not_over_start_or_track_blocks() throws NoSuchAlgorithmException {
        field.generateTrackBlocks(10);
        field.generateFinishBlock();
        Block finishBlock = field.getFinishBlock();

        List<Block> trackBlocks = field.getTrackBlocks();
        Block startBlock = field.getStartBlock();

        assertFalse(trackBlocks.contains(finishBlock));
        assertNotEquals(startBlock, finishBlock);
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
        List<Point> points = new ArrayList<>();
        for (Future<Point> f : futureList) {
            points.add(f.get());
        }
        points.sort((o1, o2) -> {
            int isEqual = o1.i() - o2.i();
            if (isEqual == 0) {
                return o1.j() - o2.j();
            }
            return isEqual;
        });

        assertEquals(field.getStartBlock().getPoints(), points);
    }

    private Callable<Point> getPoint() {
        Point point = field.getRandomFreeStartPoint();
        field.occupyPoint(point, null, null);
        return () -> point;
    }

    @Test
    void can_check_is_point_occupied() {
        Point occupyPoint = new Point(0, 0);
        Point freePoint = new Point(1, 0);
        field.occupyPoint(occupyPoint, null, null);

        assertTrue(field.isPointOccupied(occupyPoint));
        assertFalse(field.isPointOccupied(freePoint));
    }

    @Test
    void can_get_transport_at_occupy_point() throws NoSuchAlgorithmException {
        Point occupyPoint = new Point(0, 0);
        Transport car = new Car(field);
        field.occupyPoint(occupyPoint, null, car);

        assertEquals(car, field.getTransportAtPoint(occupyPoint));
    }
}

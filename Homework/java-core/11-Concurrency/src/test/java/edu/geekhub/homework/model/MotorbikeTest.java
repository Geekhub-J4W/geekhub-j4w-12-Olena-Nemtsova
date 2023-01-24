package edu.geekhub.homework.model;

import edu.geekhub.homework.track.FieldController;
import edu.geekhub.homework.track.Point;
import edu.geekhub.homework.util.WinnerChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MotorbikeTest {
    @Mock
    private FieldController fieldController;
    @Captor
    private ArgumentCaptor<Point> pointCaptor;

    @BeforeEach
    void setUp() {
        when(fieldController.occupyRandomFreeStartPoint(any())).thenReturn(new Point(0, 0));
        WinnerChecker.resetWinner();
    }

    @Test
    void can_win_the_race() throws NoSuchAlgorithmException, InterruptedException {
        when(fieldController.isFieldPoint(any())).thenReturn(true);
        when(fieldController.isFinishPoint(any())).thenReturn(true);

        Thread thread = new Thread(new Motorbike(fieldController));
        thread.start();
        thread.join();

        assertTrue(WinnerChecker.getValue());
    }

    @Test
    void can_left_the_road() throws NoSuchAlgorithmException, InterruptedException {
        when(fieldController.isFieldPoint(any())).thenReturn(false);

        Thread thread = new Thread(new Motorbike(fieldController));
        thread.start();
        thread.join();

        assertFalse(WinnerChecker.getValue());
    }

    @Test
    void can_crashed_into_another_car_and_win() throws NoSuchAlgorithmException, InterruptedException {
        when(fieldController.isFieldPoint(any())).thenReturn(true);
        when(fieldController.isFinishPoint(any())).thenReturn(true);
        when(fieldController.isPointOccupied(any())).thenReturn(true);

        Transport anotherCar = new Motorbike(fieldController);
        when(fieldController.getTransportAtPoint(any())).thenReturn(anotherCar);

        Thread thread = new Thread(new Motorbike(fieldController));
        thread.start();
        thread.join();

        assertTrue(WinnerChecker.getValue());
    }

    @Test
    void can_stop_when_winner_already_got() throws NoSuchAlgorithmException, InterruptedException {
        WinnerChecker.setWinner();

        Thread thread = new Thread(new Motorbike(fieldController));
        thread.start();
        thread.join();

        verify(fieldController, times(0)).isFieldPoint(any());
    }

    @Test
    void can_move_for_two_points() throws NoSuchAlgorithmException, InterruptedException {
        when(fieldController.isFieldPoint(any())).thenReturn(false);
        Thread thread = new Thread(new Motorbike(fieldController));
        thread.start();
        thread.join();
        verify(fieldController, atLeastOnce()).isFieldPoint(pointCaptor.capture());
        Point capturedPoint = pointCaptor.getValue();

        List<Point> variationsForMove = List.of(new Point(0, -2),
            new Point(0, 2),
            new Point(-2, 0),
            new Point(2, 0));

        assertTrue(variationsForMove.contains(capturedPoint));
    }

}

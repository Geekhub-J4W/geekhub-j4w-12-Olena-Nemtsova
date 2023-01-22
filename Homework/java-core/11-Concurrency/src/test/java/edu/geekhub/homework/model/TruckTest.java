package edu.geekhub.homework.model;

import edu.geekhub.homework.track.Field;
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
import static org.mockito.Mockito.atLeastOnce;

@ExtendWith(MockitoExtension.class)
class TruckTest {
    @Mock
    private Field field;
    @Captor
    private ArgumentCaptor<Point> pointCaptor;

    @BeforeEach
    void setUp() {
        when(field.getRandomFreeStartPoint()).thenReturn(new Point(0, 0));
        WinnerChecker.resetWinner();
    }

    @Test
    void can_win_the_race() throws NoSuchAlgorithmException, InterruptedException {
        when(field.isFieldPoint(any())).thenReturn(true);
        when(field.isFinishPoint(any())).thenReturn(true);

        Thread thread = new Thread(new Truck(field));
        thread.start();
        thread.join();

        assertTrue(WinnerChecker.getValue());
    }

    @Test
    void can_left_the_road() throws NoSuchAlgorithmException, InterruptedException {
        when(field.isFieldPoint(any())).thenReturn(false);

        Thread thread = new Thread(new Truck(field));
        thread.start();
        thread.join();

        assertFalse(WinnerChecker.getValue());
    }

    @Test
    void can_waiting_for_the_way_be_clear_and_win() throws NoSuchAlgorithmException, InterruptedException {
        when(field.isFieldPoint(any())).thenReturn(true);
        when(field.isFinishPoint(any())).thenReturn(true);
        when(field.isPointOccupied(any())).thenReturn(true);

        Transport anotherCar = new Car(field);
        when(field.getTransportAtPoint(any())).thenReturn(anotherCar);

        Thread thread = new Thread(new Truck(field));
        thread.start();
        Thread.sleep(1000);
        when(field.isPointOccupied(any())).thenReturn(false);
        thread.join();

        assertTrue(WinnerChecker.getValue());
    }

    @Test
    void can_be_evacuated_two_trucks_got_deadlock() throws NoSuchAlgorithmException, InterruptedException {
        when(field.isFieldPoint(any())).thenReturn(true);
        when(field.isPointOccupied(any())).thenReturn(true);

        Transport anotherCar = new Truck(field);
        when(field.getTransportAtPoint(any())).thenReturn(anotherCar);

        Thread thread = new Thread(new Truck(field));
        thread.start();
        thread.join();

        assertFalse(WinnerChecker.getValue());
    }

    @Test
    void can_stop_when_winner_already_got() throws NoSuchAlgorithmException, InterruptedException {
        WinnerChecker.setWinner();

        Thread thread = new Thread(new Truck(field));
        thread.start();
        thread.join();

        verify(field, times(0)).isFieldPoint(any());
    }

    @Test
    void can_move_for_one_point() throws NoSuchAlgorithmException, InterruptedException {
        when(field.isFieldPoint(any())).thenReturn(false);
        Thread thread = new Thread(new Truck(field));
        thread.start();
        thread.join();
        verify(field, atLeastOnce()).isFieldPoint(pointCaptor.capture());

        Point capturedPoint = pointCaptor.getValue();
        List<Point> variationsForMove = List.of(new Point(0, -1),
            new Point(0, 1),
            new Point(-1, 0),
            new Point(1, 0));

        assertTrue(variationsForMove.contains(capturedPoint));
    }
}

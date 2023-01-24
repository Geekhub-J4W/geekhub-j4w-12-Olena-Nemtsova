package edu.geekhub.homework;

import edu.geekhub.homework.model.Car;
import edu.geekhub.homework.model.Motorbike;
import edu.geekhub.homework.model.Truck;
import edu.geekhub.homework.track.Field;
import edu.geekhub.homework.track.FieldController;
import edu.geekhub.homework.track.FieldGenerator;
import edu.geekhub.homework.util.Painter;
import edu.geekhub.homework.util.WinnerChecker;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationStarter {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {
        FieldGenerator generator = new FieldGenerator();
        Field field = generator.generateField(2);
        FieldController fieldController = new FieldController(field);

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

        Random rand = SecureRandom.getInstanceStrong();
        while (!WinnerChecker.getValue()) {
            Runnable runnable;
            switch (rand.nextInt(3)) {
                case 0 -> runnable = new Car(fieldController);
                case 1 -> runnable = new Motorbike(fieldController);
                default -> runnable = new Truck(fieldController);
            }
            fixedThreadPool.submit(runnable);

            Thread.sleep(rand.nextInt(500, 1000));
        }
        fixedThreadPool.shutdownNow();

        Painter.paintTrack(field);
    }
}

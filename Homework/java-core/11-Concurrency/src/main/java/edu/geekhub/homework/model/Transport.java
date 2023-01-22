package edu.geekhub.homework.model;

import edu.geekhub.homework.track.Field;
import edu.geekhub.homework.track.Point;
import edu.geekhub.homework.util.Color;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.logging.Logger;

public abstract class Transport implements Runnable {
    protected String type;
    protected Color color;
    protected int speed;
    protected Point point;
    protected Field field;
    protected static Logger logger = Logger.getGlobal();

    protected Transport(Field field) throws NoSuchAlgorithmException {
        this.field = field;
        while (point == null) {
            point = this.field.getRandomFreeStartPoint();
        }
        color = Color.getRandomColor();
        Random rand = SecureRandom.getInstanceStrong();
        speed = rand.nextInt(200, 1000);
    }
}

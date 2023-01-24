package edu.geekhub.homework.model;

import edu.geekhub.homework.track.FieldController;
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
    protected FieldController fieldController;
    protected static Logger logger = Logger.getGlobal();

    protected Transport(FieldController fieldController) throws NoSuchAlgorithmException {
        this.fieldController = fieldController;
        while (point == null) {
            point = this.fieldController.occupyRandomFreeStartPoint(this);
        }
        color = Color.getRandomColor();
        Random rand = SecureRandom.getInstanceStrong();
        speed = rand.nextInt(200, 1000);
    }
}

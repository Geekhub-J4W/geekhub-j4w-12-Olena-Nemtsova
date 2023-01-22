package edu.geekhub.homework.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public enum Color {
    BLACK,
    WHITE,
    RED,
    GREEN,
    YELLOW,
    ORANGE,
    BLUE,
    PURPLE,
    SILVER,
    BROWN;

    public static Color getRandomColor() throws NoSuchAlgorithmException {
        Random rand = SecureRandom.getInstanceStrong();
        return Color.values()[rand.nextInt(Course.values().length)];
    }
}

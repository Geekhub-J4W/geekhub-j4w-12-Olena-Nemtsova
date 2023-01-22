package edu.geekhub.homework.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public enum Course {
    DOWN,
    UP,
    LEFT,
    RIGHT;

    public static Course getRandomCourse() throws NoSuchAlgorithmException {
        Random rand = SecureRandom.getInstanceStrong();
        return Course.values()[rand.nextInt(Course.values().length)];
    }
}

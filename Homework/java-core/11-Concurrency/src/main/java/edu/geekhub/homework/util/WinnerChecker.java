package edu.geekhub.homework.util;


public class WinnerChecker {
    private static volatile boolean hasWinner = false;

    private WinnerChecker() {
    }

    public static synchronized void setWinner() {
        hasWinner = true;
    }

    public static synchronized boolean getValue() {
        return hasWinner;
    }

    public static synchronized void resetWinner() {
        hasWinner = false;
    }
}

package edu.geekhub.homework.logging;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyLogger {
    private static final List<MyLog> loggerList = new ArrayList<>();
    static File logFile = new File("Homework/java-core/10-IO-NIO/src/main/resources/logList.txt");

    private MyLogger() {
    }

    public static void log(Level level, String message) {
        loggerList.add(new MyLog(LocalDateTime.now(), level, message));
    }

    public static List<MyLog> getAll() {
        return loggerList;
    }

    public static List<MyLog> getOnlyINFO() {
        return loggerList.stream()
            .filter(logger -> logger.level() == Level.INFO)
            .toList();
    }

    public static List<MyLog> getOnlyERROR() {
        return loggerList.stream()
            .filter(logger -> logger.level() == Level.ERROR)
            .toList();
    }

    public static void sortByDate() {
        loggerList.sort((Comparator.comparing(MyLog::dateTime)));
    }

    public static void sortByLevel() {
        loggerList.sort((Comparator.comparing(MyLog::level)));
    }

    public static void saveToFile() {
        try (FileOutputStream fos = new FileOutputStream(logFile, true);
             PrintStream printStream = new PrintStream(fos)) {

            for (MyLog log : loggerList) {
                printStream.println(log.toString());
            }
        } catch (IOException ex) {
            MyLogger.log(Level.ERROR, ex.getMessage());
        }
    }

    public static void cleanAll() {
        loggerList.clear();
    }
}

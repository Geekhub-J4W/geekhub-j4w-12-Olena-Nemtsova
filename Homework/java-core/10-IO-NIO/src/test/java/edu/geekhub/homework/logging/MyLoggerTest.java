package edu.geekhub.homework.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MyLoggerTest {
    private static final String message = "Some message";
    MyLog expectedMyLog;

    @BeforeEach
    void setUp() {
        MyLogger.cleanAll();
    }

    @Test
    void can_log_INFO() {
        MyLogger.log(Level.INFO, message);
        MyLog myLog = MyLogger.getAll().get(0);

        expectedMyLog = new MyLog(myLog.dateTime(), Level.INFO, message);

        assertEquals(expectedMyLog, myLog);
    }

    @Test
    void can_log_ERROR() {
        MyLogger.log(Level.ERROR, message);
        MyLog myLog = MyLogger.getAll().get(0);

        expectedMyLog = new MyLog(myLog.dateTime(), Level.ERROR, message);

        assertEquals(expectedMyLog, myLog);
    }

    @Test
    void can_get_all_logs() {
        MyLogger.log(Level.ERROR, message);
        MyLogger.log(Level.INFO, message);
        List<MyLog> myLogList = MyLogger.getAll();

        List<MyLog> expectedMyLogList = List.of(new MyLog(myLogList.get(0).dateTime(), Level.ERROR, message),
            new MyLog(myLogList.get(1).dateTime(), Level.INFO, message));

        assertEquals(expectedMyLogList, myLogList);
    }

    @Test
    void can_get_only_INFO_logs() {
        MyLogger.log(Level.ERROR, message);
        MyLogger.log(Level.INFO, message);
        List<MyLog> myLogList = MyLogger.getOnlyINFO();

        List<MyLog> expectedMyLogList = List.of(new MyLog(myLogList.get(0).dateTime(), Level.INFO, message));

        assertEquals(expectedMyLogList, myLogList);
    }

    @Test
    void can_get_only_ERROR_logs() {
        MyLogger.log(Level.ERROR, message);
        MyLogger.log(Level.INFO, message);
        List<MyLog> myLogList = MyLogger.getOnlyERROR();

        List<MyLog> expectedMyLogList = List.of(new MyLog(myLogList.get(0).dateTime(), Level.ERROR, message));

        assertEquals(expectedMyLogList, myLogList);
    }

    @Test
    void can_sort_by_level() {
        MyLogger.log(Level.ERROR, message);
        MyLogger.log(Level.INFO, message);
        List<MyLog> myLogList = MyLogger.getAll();
        MyLogger.sortByLevel();
        List<MyLog> myLogListSorted = MyLogger.getAll();

        List<MyLog> expectedMyLogList = List.of(new MyLog(myLogList.get(1).dateTime(), Level.INFO, message),
            new MyLog(myLogList.get(0).dateTime(), Level.ERROR, message));

        assertEquals(expectedMyLogList, myLogListSorted);
    }

    @Test
    void can_sort_by_dateTime() {
        MyLogger.log(Level.ERROR, message);
        MyLogger.log(Level.INFO, message);
        List<MyLog> expectedMyLogList = MyLogger.getAll();

        MyLogger.sortByLevel();
        MyLogger.sortByDate();
        List<MyLog> myLogListSorted = MyLogger.getAll();

        assertEquals(expectedMyLogList, myLogListSorted);
    }

    @Test
    void can_not_save_to_wrong_file() {
        MyLogger.logFile = new File("some", "logList.txt");
        MyLogger.saveToFile();

        assertEquals(MyLogger.logFile.toString() + " (The system cannot find the path specified)", MyLogger.getAll().get(0).message());
    }

    @Test
    void can_clear_all_logs() {
        MyLogger.log(Level.ERROR, message);
        MyLogger.log(Level.INFO, message);

        MyLogger.cleanAll();

        assertEquals(new ArrayList<MyLog>(), MyLogger.getAll());
    }

    @Test
    void can_save_to_file() throws IOException {
        MyLogger.log(Level.ERROR, message);
        MyLogger.log(Level.INFO, message);

        MyLogger.logFile = new File("./src/main/resources/logList.txt");
        MyLogger.saveToFile();
        String logs = String.join("", readFile());

        String expectedLogs = MyLogger.getAll().get(0).toString() + MyLogger.getAll().get(1);
        deleteTestLogs();
        assertEquals(expectedLogs, logs);
    }

    List<String> readFile() throws IOException {
        File logFile = new File("./src/main/resources/logList.txt");

        try (Stream<String> lineStream = Files.lines(logFile.toPath())) {
            List<String> logList = lineStream.toList();
            return List.of(logList.get(logList.size() - 2), logList.get(logList.size() - 1));

        }
    }

    void deleteTestLogs() throws IOException {
        File logFile = new File("./src/main/resources/logList.txt");
        List<String> logList;
        try (Stream<String> lineStream = Files.lines(logFile.toPath())) {
            logList = lineStream.toList();
        }

        try (FileOutputStream fos = new FileOutputStream(logFile);
             PrintStream printStream = new PrintStream(fos)) {
            for (int i = 0; i < logList.size() - 2; i++) {
                String s = new String(logList.get(i).getBytes(StandardCharsets.UTF_8));
                printStream.println(s);
            }
        }
    }
}

package com.geekhub;

import java.util.Arrays;
import java.util.List;

public class StudentConsoleParser {

    private StudentConsoleParser() {
    }

    public static Student fromConsoleInput(String input) {
        List<String> userData = List.of(input.split(","));
        validateUserData(userData);
        return new Student(userData.get(0), userData.get(1));
    }

    public static Student fromStringFormat(String studentStr) {
        List<String> userData = Arrays.stream(studentStr.split(" "))
            .filter(s -> s.contains("firstName") || s.contains("lastName") || s.contains("id"))
            .map(s -> s.replace("firstName=", ""))
            .map(s -> s.replace("lastName=", ""))
            .map(s -> s.replace("id=", ""))
            .map(s -> s.replace("'", ""))
            .map(s -> s.replace(",", ""))
            .toList();

        validateStringData(userData);
        return new Student(Integer.parseInt(userData.get(0)), userData.get(1), userData.get(2));
    }

    private static void validateUserData(List<String> userData) {
        if (userData.size() != 2) {
            throw new IllegalArgumentException("Invalid console input");
        }
    }

    private static void validateStringData(List<String> userData) {
        if (userData.size() != 3) {
            throw new IllegalArgumentException("Invalid console input");
        }
    }
}

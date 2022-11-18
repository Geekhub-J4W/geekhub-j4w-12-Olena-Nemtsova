package edu.geekhub.homework.task2;


import static java.util.Objects.requireNonNull;

public class LosesInWarParser {

    private static final int LOSES_STATISTIC_INPUT_LINE_NUMBER = 13;
    private LosesStatistic losesStatistic;

    LosesStatistic parseLosesStatistic(String input) {
        input = removeHtmlElementsFromInput(input);

        if (!validateLosesStatisticState(input)) {
            return LosesStatistic.empty();
        }

        String[] inputByLines = input.split("\n");

        losesStatistic = LosesStatistic.newStatistic()
            .withTanks(getNumberOfLosesFromLine(inputByLines[0]))
            .withArmouredFightingVehicles(getNumberOfLosesFromLine(inputByLines[1]))
            .withCannons(getNumberOfLosesFromLine(inputByLines[2]))
            .withMultipleRocketLaunchers(getNumberOfLosesFromLine(inputByLines[3]))
            .withAntiAirDefenseDevices(getNumberOfLosesFromLine(inputByLines[4]))
            .withPlanes(getNumberOfLosesFromLine(inputByLines[5]))
            .withHelicopters(getNumberOfLosesFromLine(inputByLines[6]))
            .withDrones(getNumberOfLosesFromLine(inputByLines[7]))
            .withCruiseMissiles(getNumberOfLosesFromLine(inputByLines[8]))
            .withShipsOrBoats(getNumberOfLosesFromLine(inputByLines[9]))
            .withCarsAndTankers(getNumberOfLosesFromLine(inputByLines[10]))
            .withSpecialEquipment(getNumberOfLosesFromLine(inputByLines[11]))
            .withPersonnel(getNumberOfLosesFromLine(inputByLines[12]))
            .build();

        return losesStatistic;
    }

    private boolean validateLosesStatisticState(String losesStatisticState) {
        checkLosesStatisticIsPresent(losesStatisticState);
        try {
            checkLosesStatisticStateLinesNumber(losesStatisticState);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static void checkLosesStatisticIsPresent(String losesStatisticState) {
        requireNonNull(losesStatisticState);

        if (losesStatisticState.isBlank()) {
            throw new IllegalArgumentException("Cant process empty loses statistic state");
        }
    }

    private static void checkLosesStatisticStateLinesNumber(String losesStatisticState) {
        if (losesStatisticState.split("\n").length != LOSES_STATISTIC_INPUT_LINE_NUMBER) {
            throw new IllegalArgumentException(
                "Loses statistic number of lines: " + losesStatisticState.length()
                    + " is not equal allowed number of lines: " + LOSES_STATISTIC_INPUT_LINE_NUMBER
            );
        }
    }

    private int getNumberOfLosesFromLine(String line) {
        StringBuilder clipboard = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ' && i != line.length() - 1) {
                i++;
                if (Character.isDigit(line.charAt(i))) {
                    while (Character.isDigit(line.charAt(i))) {
                        clipboard.append(line.charAt(i));
                        if (i != line.length() - 1) i++;
                        else break;
                    }
                    return Integer.parseInt(clipboard.toString());
                }
            }
        }
        return 0;
    }

    private String removeHtmlElementsFromInput(String input) {
        if (!input.contains("<li>")) {
            return input;
        }

        String[] correctLines = input.replace("\n", " ")
            .split("<li>");

        StringBuilder correctInput = new StringBuilder();
        StringBuilder clipboard;
        for (String line : correctLines) {
            clipboard = new StringBuilder();
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '<') {
                    while (line.charAt(i) != '>') {
                        i++;
                    }
                } else {
                    clipboard.append(line.charAt(i));
                }
            }
            if (!clipboard.toString().isBlank()) {
                correctInput.append(clipboard.append('\n'));
            }
        }

        return correctInput.toString();
    }
}

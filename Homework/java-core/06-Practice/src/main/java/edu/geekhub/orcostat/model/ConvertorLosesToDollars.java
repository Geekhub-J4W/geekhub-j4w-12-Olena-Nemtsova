package edu.geekhub.orcostat.model;


import java.util.Objects;

public class ConvertorLosesToDollars {
    public static int convert(DailyLossStatistic dailyLossStatistic) {
        if (Objects.isNull(dailyLossStatistic)) {
            throw new IllegalArgumentException("dailyLossStatistic cannot be null");
        }

        int result = 0;

        result += countOrcs(dailyLossStatistic);
        result += countTanks(dailyLossStatistic);
        result += countDrones(dailyLossStatistic);
        result += countMissiles(dailyLossStatistic);
        return result;
    }

    private static int countOrcs(DailyLossStatistic dailyLossStatistic) {
        int orcsSum = 0;
        for (Object orc : dailyLossStatistic.getNegativelyAliveOrcs().getData()) {
            orcsSum += ((Orc) orc).getPrice();
        }
        return orcsSum;
    }

    private static int countTanks(DailyLossStatistic dailyLossStatistic) {
        int tanksSum = 0;
        for (Object tank : dailyLossStatistic.getDestroyedTanks().getData()) {
            tanksSum += ((Tank) tank).getPrice();
        }
        return tanksSum;
    }

    private static int countDrones(DailyLossStatistic dailyLossStatistic) {
        int dronesSum = 0;
        for (Object drone : dailyLossStatistic.getDestroyedDrones().getData()) {
            dronesSum += ((Drone) drone).getPrice();
        }
        return dronesSum;
    }

    private static int countMissiles(DailyLossStatistic dailyLossStatistic) {
        int missilesSum = 0;
        for (Object missile : dailyLossStatistic.getDestroyedMissiles().getData()) {
            missilesSum += ((Missile) missile).getPrice();
        }
        return missilesSum;
    }
}

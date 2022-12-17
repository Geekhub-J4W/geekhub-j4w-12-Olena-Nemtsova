package edu.geekhub.homework.analytics;

import edu.geekhub.homework.domain.LosesStatistic;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

/**
 * Service shows interesting analytics information
 */
public class AnalyticsService {

    public LosesStatistic findStatisticWithMaxLosesAmounts(List<LosesStatistic> losesStatistics) {
        return losesStatistics.stream()
            .max(Comparator.comparing(this::totalCountOfLosesForStatistic))
            .orElseThrow(() -> new IllegalArgumentException("Statistic with max loses amounts cannot be found"));
    }

    public LosesStatistic findStatisticWithMinLosesAmounts(List<LosesStatistic> losesStatistics) {
        return losesStatistics.stream()
            .min(Comparator.comparing(this::totalCountOfLosesForStatistic))
            .orElseThrow(() -> new IllegalArgumentException("Statistic with min loses amounts cannot be found"));
    }

    public int totalCountOfLosesForStatistic(LosesStatistic losesStatistic) {
        losesStatistic = validateLosesStatistic(losesStatistic);
        return (int) LongStream.of(
                losesStatistic.tanks(),
                losesStatistic.armouredFightingVehicles(),
                losesStatistic.cannons(),
                losesStatistic.multipleRocketLaunchers(),
                losesStatistic.antiAirDefenseDevices(),
                losesStatistic.planes(),
                losesStatistic.helicopters(),
                losesStatistic.drones(),
                losesStatistic.cruiseMissiles(),
                losesStatistic.shipsOrBoats(),
                losesStatistic.carsAndTankers(),
                losesStatistic.specialEquipment(),
                losesStatistic.personnel()
            )
            .sum();
    }

    private LosesStatistic validateLosesStatistic(LosesStatistic losesStatistic) {
        return Optional.ofNullable(losesStatistic)
            .orElseThrow(() -> new IllegalArgumentException("LosesStatistic was null"));
    }

    public int totalCountOfLosesForAllStatistics(List<LosesStatistic> losesStatistics) {
        return losesStatistics.stream()
            .mapToInt(this::totalCountOfLosesForStatistic)
            .sum();
    }
}

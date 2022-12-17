package edu.geekhub.homework.domain;

import edu.geekhub.homework.client.LosesStatisticHttpClient;

/**
 * This class should contain data received through {@link LosesStatisticHttpClient}
 * via <a href="https://en.wikipedia.org/wiki/JSON">JSON</a> String
 */
public record LosesStatistic(int tanks,
                             int armouredFightingVehicles,
                             int cannons,
                             int multipleRocketLaunchers,
                             int antiAirDefenseDevices,
                             int planes,
                             int helicopters,
                             int drones,
                             int cruiseMissiles,
                             int shipsOrBoats,
                             int carsAndTankers,
                             int specialEquipment,
                             int personnel,
                             int id) {

    public LosesStatistic(LosesStatistic losesStatistic) {
        this(losesStatistic.tanks(),
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
            losesStatistic.personnel(),
            losesStatistic.id());
    }
}

package edu.geekhub.homework.client;

import edu.geekhub.homework.domain.LosesStatistic;

import java.util.*;

/**
 * This class is responsible for conversion of {@link String} objects into a
 * {@link LosesStatistic} objects and otherwise
 */
public class JsonConverter {

    public List<LosesStatistic> convertToEntities(String losesStatisticsJson) {
        List<String> separateLosesStatistics = getSeparateLosesStatistics(losesStatisticsJson);

        List<LosesStatistic> losesStatisticList = new ArrayList<>();
        for (var it : separateLosesStatistics) {
            losesStatisticList.add(convertToEntity(it));
        }
        return losesStatisticList;
    }

    private List<String> getSeparateLosesStatistics(String losesStatistics) {
        losesStatistics = losesStatistics.substring(1, losesStatistics.length() - 1);

        return Arrays.stream(losesStatistics.replace("\\{", "")
                .split("}")
            )
            .toList();
    }

    private int getInt(String key, String losesStatistic) {
        String elementByKey = Arrays.stream(losesStatistic.split(","))
            .toList()
            .stream()
            .filter((String s) -> s.contains(key))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Nothing found by key: " + key));

        return Integer.parseInt(elementByKey
            .chars()
            .filter(Character::isDigit)
            .mapToObj(it -> Character.toString((char) it))
            .reduce("", (a, b) -> a + b));
    }

    public LosesStatistic convertToEntity(String losesStatisticJson) {
        return new LosesStatistic(getInt("tanks", losesStatisticJson),
            getInt("armouredFightingVehicles", losesStatisticJson),
            getInt("cannons", losesStatisticJson),
            getInt("multipleRocketLaunchers", losesStatisticJson),
            getInt("antiAirDefenseDevices", losesStatisticJson),
            getInt("planes", losesStatisticJson),
            getInt("helicopters", losesStatisticJson),
            getInt("drones", losesStatisticJson),
            getInt("cruiseMissiles", losesStatisticJson),
            getInt("shipsOrBoats", losesStatisticJson),
            getInt("carsAndTankers", losesStatisticJson),
            getInt("specialEquipment", losesStatisticJson),
            getInt("personnel", losesStatisticJson),
            getInt("id", losesStatisticJson));
    }

    public String convertToJson(LosesStatistic losesStatistic) {
        if (losesStatistic == null) {
            throw new IllegalArgumentException("LosesStatistic was null");
        }

        Map<String, Integer> losesStatisticMap = new LinkedHashMap<>();
        losesStatisticMap.put("tanks", losesStatistic.tanks());
        losesStatisticMap.put("armouredFightingVehicles", losesStatistic.armouredFightingVehicles());
        losesStatisticMap.put("cannons", losesStatistic.cannons());
        losesStatisticMap.put("multipleRocketLaunchers", losesStatistic.multipleRocketLaunchers());
        losesStatisticMap.put("antiAirDefenseDevices", losesStatistic.antiAirDefenseDevices());
        losesStatisticMap.put("planes", losesStatistic.planes());
        losesStatisticMap.put("helicopters", losesStatistic.helicopters());
        losesStatisticMap.put("drones", losesStatistic.drones());
        losesStatisticMap.put("cruiseMissiles", losesStatistic.cruiseMissiles());
        losesStatisticMap.put("shipsOrBoats", losesStatistic.shipsOrBoats());
        losesStatisticMap.put("carsAndTankers", losesStatistic.carsAndTankers());
        losesStatisticMap.put("specialEquipment", losesStatistic.specialEquipment());
        losesStatisticMap.put("personnel", losesStatistic.personnel());
        losesStatisticMap.put("id", losesStatistic.id());

        return getJsonFromMap(losesStatisticMap);
    }

    private String getJsonFromMap(Map<String, Integer> losesStatisticMap) {
        StringBuilder s = new StringBuilder();
        for (Map.Entry<String, Integer> it : losesStatisticMap.entrySet()) {
            s.append(getValueIntoBrackets('\"', '\"', it.getKey()))
                .append(':')
                .append(getValueIntoBrackets('\"', '\"', it.getValue()));

            if (!it.getKey().equals("id")) {
                s.append(',');
            }
        }

        return getValueIntoBrackets('{', '}', s.toString());
    }

    private <T> String getValueIntoBrackets(char bracket1, char bracket2, T value) {
        return String.valueOf(bracket1) + value + bracket2;
    }

}

package edu.geekhub.homework;

import java.util.*;

import java.util.stream.Collectors;

public class Exercises {
    private static final Cities citiesRepo = new Cities();

    public Map<String, Long> getCountryCitiesCount() {
        var cities = citiesRepo.getAllCities();

        return cities.values().stream()
            .collect(Collectors.groupingBy((City city) -> {
                        city = validateCity(city);
                        return Optional.ofNullable(city.getCountryCode()).orElse("Unknown");
                    },
                    Collectors.counting()
                )
            );
    }

    private City validateCity(City city) {
        return Optional.ofNullable(city)
            .orElseThrow(() -> new IllegalArgumentException("City was null"));
    }

    public City mostPopulatedCity() {
        var cities = citiesRepo.getAllCities();

        return cities.values().stream()
            .max(Comparator.comparing((City city) -> {
                        city = validateCity(city);
                        return city.getPopulation();
                    }
                )
            )
            .orElseThrow(() -> new IllegalArgumentException("Most populated city cannot be found"));
    }

    public City minPopulatedCity() {
        var cities = citiesRepo.getAllCities();

        return cities.values().stream()
            .min(Comparator.comparing((City city) -> {
                        city = validateCity(city);
                        return city.getPopulation();
                    }
                )
            )
            .orElseThrow(() -> new IllegalArgumentException("Min populated city cannot be found"));
    }

    public String mostPopulatedCountry() {
        Map.Entry<String, Integer> entry = populationOfEachCountry()
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .orElseThrow(() -> new IllegalArgumentException("Most populated country cannot be found"));

        return entry.getKey();
    }

    public String minPopulatedCountry() {
        Map.Entry<String, Integer> entry = populationOfEachCountry()
            .entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .orElseThrow(() -> new IllegalArgumentException("Min populated country cannot be found"));

        return entry.getKey();
    }

    public Long totalPopulation() {
        var cities = citiesRepo.getAllCities();

        return cities.values().stream()
            .mapToLong((City city) -> {
                    city = validateCity(city);
                    return city.getPopulation();
                }
            )
            .sum();
    }

    public Map<String, Integer> populationOfEachCountry() {
        var cities = citiesRepo.getAllCities();

        return cities.values().stream()
            .collect(Collectors.groupingBy((City city) -> {
                        city = validateCity(city);
                        return Optional.ofNullable(city.getCountryCode()).orElse("Unknown");
                    },
                    Collectors.summingInt(City::getPopulation)
                )
            );
    }

    public Integer populationOfSpecificCountry(String countryCode) {
        Map<String, Integer> populationOfAll = populationOfEachCountry();

        boolean isCountryPresent = populationOfAll.keySet().stream()
            .anyMatch(key -> key.equals(countryCode));
        if (isCountryPresent) {
            return populationOfAll.get(countryCode);
        } else {
            throw new IllegalArgumentException("Nothing found for country code: " + countryCode);
        }
    }

    public City specificCity(String city) {
        var cities = citiesRepo.getAllCities();

        return cities.values().stream()
            .filter(o -> o.getName().equals(city))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(city + " city wasn't found"));
    }
}

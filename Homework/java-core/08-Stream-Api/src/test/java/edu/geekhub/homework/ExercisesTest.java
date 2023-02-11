package edu.geekhub.homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExercisesTest {
    private Exercises exercises;
    private Map<Integer, City> cities;

    @BeforeEach
    void setUp() {
        exercises = new Exercises();
        cities = new Cities().getAllCities();
    }

    @Test
    void can_get_most_populated_city() {
        City city = exercises.mostPopulatedCity();

        int max_population = 0;
        int max_i = 1;
        for (Map.Entry<Integer, City> entry : cities.entrySet()) {
            int i = entry.getKey();
            int population = cities.get(i).getPopulation();
            if (population > max_population) {
                max_population = population;
                max_i = i;
            }
        }

        assertEquals(cities.get(max_i), city);
    }

    @Test
    void can_get_min_populated_city() {
        City city = exercises.minPopulatedCity();

        int min_i = 1;
        int min_population = cities.get(min_i).getPopulation();
        for (Map.Entry<Integer, City> entry : cities.entrySet()) {
            int i = entry.getKey();
            int population = cities.get(i).getPopulation();
            if (population < min_population) {
                min_population = population;
                min_i = i;
            }
        }

        assertEquals(cities.get(min_i), city);
    }

    @Test
    void can_get_max_populated_country() {
        String country = exercises.mostPopulatedCountry();

        Map<String, Integer> countriesPopulation = getPopulationForEachCountry();
        String expectedCountry = null;
        for (String it : countriesPopulation.keySet()) {
            if (expectedCountry == null || countriesPopulation.get(it) > countriesPopulation.get(expectedCountry)) {
                expectedCountry = it;
            }
        }

        assertEquals(expectedCountry, country);
    }

    @Test
    void can_get_min_populated_country() {
        String country = exercises.minPopulatedCountry();

        Map<String, Integer> countriesPopulation = getPopulationForEachCountry();
        String expectedCountry = null;
        for (String it : countriesPopulation.keySet()) {
            if (expectedCountry == null || countriesPopulation.get(it) < countriesPopulation.get(expectedCountry)) {
                expectedCountry = it;
            }
        }

        assertEquals(expectedCountry, country);
    }

    @Test
    void can_get_total_populated() {
        Long sum = exercises.totalPopulation();

        Long expectedSum = 0L;
        for (Map.Entry<Integer, City> entry : cities.entrySet()) {
            int i = entry.getKey();
            expectedSum += cities.get(i).getPopulation();
        }

        assertEquals(expectedSum, sum);
    }

    @Test
    void can_get_population_each_country() {
        Map<String, Integer> countriesPopulation = exercises.populationOfEachCountry();

        Map<String, Integer> expectedCountriesPopulation = getPopulationForEachCountry();

        assertEquals(expectedCountriesPopulation, countriesPopulation);
    }

    @Test
    void can_get_specific_city() {
        String cityName = "Wollongong";
        City city = exercises.specificCity(cityName);

        City expectedCity = null;
        for (Map.Entry<Integer, City> entry : cities.entrySet()) {
            int i = entry.getKey();
            if (cities.get(i).getName().equals(cityName)) {
                expectedCity = cities.get(i);
                break;
            }
        }

        assertEquals(expectedCity, city);
    }

    @Test
    void can_not_get_wrong_specific_city() {
        String cityName = "Non-existent city";
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> exercises.specificCity(cityName)
        );

        assertEquals(cityName + " city wasn't found", thrown.getMessage());
    }

    @Test
    void can_get_population_of_specific_country() {
        String countryCode = "AUS";
        Integer populationOfCountry = exercises.populationOfSpecificCountry(countryCode);

        Integer expectedPopulationOfCountry = 0;
        for (Map.Entry<Integer, City> entry : cities.entrySet()) {
            int i = entry.getKey();
            if (cities.get(i).getCountryCode().equals(countryCode)) {
                expectedPopulationOfCountry += cities.get(i).getPopulation();
            }
        }

        assertEquals(expectedPopulationOfCountry, populationOfCountry);
    }

    @Test
    void can_not_get_population_of_wrong_specific_country() {
        String countryCode = "Non-existent country";
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> exercises.populationOfSpecificCountry(countryCode)
        );

        assertEquals("Nothing found for country code: " + countryCode, thrown.getMessage());
    }

    @Test
    void can_get_country_cities_count() {
        Map<String, Long> countryCitiesCount = exercises.getCountryCitiesCount();

        Map<String, Long> expectedCountryCitiesCount = new HashMap<>();
        for (Map.Entry<Integer, City> entry : cities.entrySet()) {
            int i = entry.getKey();
            String key = cities.get(i).getCountryCode();
            if (expectedCountryCitiesCount.containsKey(key)) {
                expectedCountryCitiesCount.put(key, expectedCountryCitiesCount.get(key) + 1);
            } else {
                expectedCountryCitiesCount.put(key, 1L);
            }
        }

        assertEquals(expectedCountryCitiesCount, countryCitiesCount);
    }

    Map<String, Integer> getPopulationForEachCountry() {
        Map<String, Integer> countriesPopulation = new HashMap<>();

        for (Map.Entry<Integer, City> entry : cities.entrySet()) {
            int i = entry.getKey();
            String key = cities.get(i).getCountryCode();
            if (countriesPopulation.containsKey(key)) {
                countriesPopulation.put(key, countriesPopulation.get(key) + cities.get(i).getPopulation());
            } else {
                countriesPopulation.put(key, cities.get(i).getPopulation());
            }
        }

        return countriesPopulation;
    }
}

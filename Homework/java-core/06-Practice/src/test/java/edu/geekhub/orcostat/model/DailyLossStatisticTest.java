package edu.geekhub.orcostat.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DailyLossStatisticTest {
    private DailyLossStatistic dailyLossStatistic;

    @BeforeEach
    void setUp() {
        dailyLossStatistic = new DailyLossStatistic("2022-11-26");
    }

    @Test
    void cannot_create_DailyLossStatistic_with_wrong_format_date() {
        assertThrows(
            IllegalArgumentException.class,
            () -> dailyLossStatistic = new DailyLossStatistic("some date")
        );
    }

    @Test
    void cannot_create_DailyLossStatistic_with_future_date() {
        assertThrows(
            IllegalArgumentException.class,
            () -> dailyLossStatistic = new DailyLossStatistic("2025-01-01")
        );
    }

    @Test
    void cannot_create_DailyLossStatistic_with_date_less_start_war() {
        assertThrows(
            IllegalArgumentException.class,
            () -> dailyLossStatistic = new DailyLossStatistic("2000-01-01")
        );
    }

    @Test
    void can_get_date() {
        assertEquals("2022-11-26", dailyLossStatistic.getDate());
    }

    @Test
    void can_get_negatively_alive_orc_count() {
        assertEquals(0, dailyLossStatistic.getNegativelyAliveOrcCount());
    }

    @Test
    void can_add_negatively_alive_orc() {
        dailyLossStatistic.addNegativelyAliveOrc(new Orc());

        assertEquals(1, dailyLossStatistic.getNegativelyAliveOrcCount());
    }

    @Test
    void can_get_negatively_alive_orc_collection() {
        dailyLossStatistic.addNegativelyAliveOrc(new Orc());

        TrivialCollection orcs = dailyLossStatistic.getNegativelyAliveOrcs();

        assertEquals(1, orcs.count());
    }

    @Test
    void can_get_destroyed_tanks_count() {
        assertEquals(0, dailyLossStatistic.getDestroyedTanksCount());
    }

    @Test
    void can_add_destroyed_tank_without_equipage() {
        dailyLossStatistic.addDestroyedTank(new Tank());

        assertEquals(1, dailyLossStatistic.getDestroyedTanksCount());
    }

    @Test
    void can_add_destroyed_tank_with_equipage() {
        TrivialCollection equipage = new TrivialCollection();
        equipage.add(new Orc());
        equipage.add(new Orc());
        dailyLossStatistic.addDestroyedTank(new Tank(equipage));

        assertEquals(1, dailyLossStatistic.getDestroyedTanksCount());
        assertEquals(2, dailyLossStatistic.getNegativelyAliveOrcCount());
    }

    @Test
    void can_get_destroyed_tanks_collection() {
        dailyLossStatistic.addDestroyedTank(new Tank());

        TrivialCollection tanks = dailyLossStatistic.getDestroyedTanks();

        assertEquals(1, tanks.count());
    }

    @Test
    void can_get_destroyed_drones_count() {
        assertEquals(0, dailyLossStatistic.getDestroyedDronesCount());
    }

    @Test
    void can_add_destroyed_drone() {
        dailyLossStatistic.addDestroyedDrone(new Drone());

        assertEquals(1, dailyLossStatistic.getDestroyedDronesCount());
    }

    @Test
    void can_get_destroyed_drones_collection() {
        dailyLossStatistic.addDestroyedDrone(new Drone());

        TrivialCollection drones = dailyLossStatistic.getDestroyedDrones();

        assertEquals(1, drones.count());
    }

    @Test
    void can_get_destroyed_missiles_count() {
        assertEquals(0, dailyLossStatistic.getDestroyedMissilesCount());
    }

    @Test
    void can_add_destroyed_missile() {
        dailyLossStatistic.addDestroyedMissile(new Missile());

        assertEquals(1, dailyLossStatistic.getDestroyedMissilesCount());
    }

    @Test
    void can_get_destroyed_missiles_collection() {
        dailyLossStatistic.addDestroyedMissile(new Missile());

        TrivialCollection missiles = dailyLossStatistic.getDestroyedMissiles();

        assertEquals(1, missiles.count());
    }

    @Test
    void can_get_dailyLossStatistic_in_dollars() {
        dailyLossStatistic.addNegativelyAliveOrc(new Orc());
        dailyLossStatistic.addDestroyedTank(new Tank());
        dailyLossStatistic.addDestroyedDrone(new Drone());
        dailyLossStatistic.addDestroyedMissile(new Missile());

        int expectedDamage = 10_000 + 3_000_000 + 50_000 + 4_000_000;

        assertEquals(expectedDamage, dailyLossStatistic.getLosesInDollars());
    }


}

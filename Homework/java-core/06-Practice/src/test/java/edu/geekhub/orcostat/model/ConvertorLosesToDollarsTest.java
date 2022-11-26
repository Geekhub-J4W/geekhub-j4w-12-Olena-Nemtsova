package edu.geekhub.orcostat.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ConvertorLosesToDollarsTest {
    private DailyLossStatistic dailyLossStatistic;

    @BeforeEach
    void setUp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        dailyLossStatistic = new DailyLossStatistic(formatter.format(now));
    }

    @Test
    void cannot_convert_null_DailyLossStatistic() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ConvertorLosesToDollars.convert(null)
        );
    }

    @Test
    void can_convert_without_loses_to_dollars() {
        int damage = ConvertorLosesToDollars.convert(dailyLossStatistic);

        assertEquals(0, damage);
    }

    @Test
    void can_convert_orc_loses_to_dollars() {
        dailyLossStatistic.addNegativelyAliveOrc(new Orc());

        int damage = ConvertorLosesToDollars.convert(dailyLossStatistic);

        assertEquals(10_000, damage);
    }

    @Test
    void can_convert_tanks_loses_to_dollars() {
        dailyLossStatistic.addDestroyedTank(new Tank());

        int damage = ConvertorLosesToDollars.convert(dailyLossStatistic);

        assertEquals(3_000_000, damage);
    }

    @Test
    void can_convert_drones_loses_to_dollars() {
        dailyLossStatistic.addDestroyedDrone(new Drone());

        int damage = ConvertorLosesToDollars.convert(dailyLossStatistic);

        assertEquals(50_000, damage);
    }

    @Test
    void can_convert_missiles_loses_to_dollars() {
        dailyLossStatistic.addDestroyedMissile(new Missile());

        int damage = ConvertorLosesToDollars.convert(dailyLossStatistic);

        assertEquals(4_000_000, damage);
    }
}

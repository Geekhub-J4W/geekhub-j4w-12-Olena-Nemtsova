package edu.geekhub.orcostat;

import edu.geekhub.orcostat.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrcoStatTest {
    private OrcoStatService orcoStatService;

    @BeforeEach
    void setUp() {
        orcoStatService = new OrcoStatService();
    }

    @Test
    void can_get_date() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        String expectedDate = formatter.format(now);

        assertEquals(expectedDate, orcoStatService.getDate());
    }

    @Test
    void can_set_date() {
        String date = "2022-11-23";
        orcoStatService.setDate(date);

        assertEquals(date, orcoStatService.getDate());
    }

    @Test
    void can_get_dailyLosses_collection() {
        TrivialCollection dailyLoses = orcoStatService.getDailyLosses();

        assertEquals(1, dailyLoses.count());
    }

    @Test
    void can_count_negatively_alive_orc() {
        int count = orcoStatService.getNegativelyAliveOrcCount();

        assertEquals(0, count);
    }

    @Test
    void can_count_negatively_alive_orc_by_concrete_date() {
        int count = orcoStatService.getNegativelyAliveOrcCountByConcreteDate(0);

        assertEquals(0, count);
    }

    @Test
    void can_add_negatively_alive_orc() {
        orcoStatService.addNegativelyAliveOrc(new Orc());

        int count = orcoStatService.getNegativelyAliveOrcCount();

        assertEquals(1, count);
    }

    @Test
    void can_add_multiple_negatively_alive_orcs() {
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addNegativelyAliveOrc(new Orc());

        int count = orcoStatService.getNegativelyAliveOrcCount();

        assertEquals(2, count);
    }

    @Test
    void can_count_destroyed_tanks() {
        int count = orcoStatService.getDestroyedTanksCount();

        assertEquals(0, count);
    }

    @Test
    void can_count_destroyed_tanks_by_concrete_date() {
        int count = orcoStatService.getDestroyedTanksCountByConcreteDate(0);

        assertEquals(0, count);
    }

    @Test
    void can_add_destroyed_tank_without_orc() {
        orcoStatService.addDestroyedTank(new Tank());

        int count = orcoStatService.getDestroyedTanksCount();

        assertEquals(1, count);
    }

    @Test
    void can_add_destroyed_tank_with_orcs() {
        TrivialCollection equipage = new TrivialCollection();
        equipage.add(new Orc());
        equipage.add(new Orc());
        equipage.add(new Orc());
        equipage.add(new Orc());
        orcoStatService.addDestroyedTank(new Tank(equipage));

        int tanksCount = orcoStatService.getDestroyedTanksCount();
        int orcCount = orcoStatService.getNegativelyAliveOrcCount();

        assertEquals(1, tanksCount);
        assertEquals(4, orcCount);
    }

    @Test
    void can_count_destroyed_drones() {
        int count = orcoStatService.getDestroyedDronesCount();

        assertEquals(0, count);
    }

    @Test
    void can_count_destroyed_drones_by_concrete_date() {
        int count = orcoStatService.getDestroyedDronesCountByConcreteDate(0);

        assertEquals(0, count);
    }

    @Test
    void can_add_destroyed_drone() {
        orcoStatService.addDestroyedDrone(new Drone());

        int count = orcoStatService.getDestroyedDronesCount();

        assertEquals(1, count);
    }

    @Test
    void can_count_destroyed_missiles() {
        int count = orcoStatService.getDestroyedMissilesCount();

        assertEquals(0, count);
    }

    @Test
    void can_count_destroyed_missiles_by_concrete_date() {
        int count = orcoStatService.getDestroyedMissilesCountByConcreteDate(0);

        assertEquals(0, count);
    }

    @Test
    void can_add_destroyed_missile() {
        orcoStatService.addDestroyedMissile(new Missile());

        int count = orcoStatService.getDestroyedMissilesCount();

        assertEquals(1, count);
    }

    @Test
    void can_count_orcs_loses_in_dollars() {
        int damage = orcoStatService.getLosesInDollars();

        assertEquals(0, damage);
    }

    @Test
    void can_sum_orc_loses_in_dollars() {
        orcoStatService.addNegativelyAliveOrc(new Orc());

        int damage = orcoStatService.getLosesInDollars();

        assertEquals(10_000, damage);
    }

    @Test
    void can_sum_tank_loses_in_dollars() {
        orcoStatService.addDestroyedTank(new Tank());

        int losesInDollars = orcoStatService.getLosesInDollars();

        assertEquals(3_000_000, losesInDollars);
    }

    @Test
    void can_sum_tank_with_orc_as_equipage_loses_cost_in_dollars() {
        TrivialCollection equipage = new TrivialCollection();
        equipage.add(new Orc());
        orcoStatService.addDestroyedTank(new Tank(equipage));

        int losesInDollars = orcoStatService.getLosesInDollars();

        assertEquals(3_010_000, losesInDollars);
    }

    @Test
    void can_sum_tank_with_orc_as_equipage_and_lost_orc_loses_cost_in_dollars() {
        TrivialCollection equipage = new TrivialCollection();
        equipage.add(new Orc());
        orcoStatService.addDestroyedTank(new Tank(equipage));
        orcoStatService.addNegativelyAliveOrc(new Orc());

        int losesInDollars = orcoStatService.getLosesInDollars();

        assertEquals(3_020_000, losesInDollars);
    }

    @Test
    void can_sum_drone_loses_in_dollars() {
        orcoStatService.addDestroyedDrone(new Drone());

        int losesInDollars = orcoStatService.getLosesInDollars();

        assertEquals(50_000, losesInDollars);
    }

    @Test
    void can_sum_missile_loses_in_dollars() {
        orcoStatService.addDestroyedMissile(new Missile());

        int losesInDollars = orcoStatService.getLosesInDollars();

        assertEquals(4_000_000, losesInDollars);
    }

    @Test
    void can_get_max_loses_count() {
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addDestroyedTank(new Tank());

        int expectedMax = 2;

        assertEquals(expectedMax, orcoStatService.getMaxLosesCount());
    }

    @Test
    void can_get_max_count_for_negative_alive_orcs() {
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.setDate("2022-11-21");
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addNegativelyAliveOrc(new Orc());

        int expectedMax = 2;

        assertEquals(expectedMax, orcoStatService.getMaxCountFor(0));
    }

    @Test
    void can_get_max_count_for_destroyed_tanks() {
        orcoStatService.addDestroyedTank(new Tank());
        orcoStatService.setDate("2022-11-21");
        orcoStatService.addDestroyedTank(new Tank());
        orcoStatService.addDestroyedTank(new Tank());

        int expectedMax = 2;

        assertEquals(expectedMax, orcoStatService.getMaxCountFor(1));
    }

    @Test
    void can_get_max_count_for_destroyed_drones() {
        orcoStatService.addDestroyedDrone(new Drone());
        orcoStatService.setDate("2022-11-21");
        orcoStatService.addDestroyedDrone(new Drone());
        orcoStatService.addDestroyedDrone(new Drone());

        int expectedMax = 2;

        assertEquals(expectedMax, orcoStatService.getMaxCountFor(2));
    }

    @Test
    void can_get_max_count_for_destroyed_missiles() {
        orcoStatService.addDestroyedMissile(new Missile());
        orcoStatService.setDate("2022-11-21");
        orcoStatService.addDestroyedMissile(new Missile());
        orcoStatService.addDestroyedMissile(new Missile());

        int expectedMax = 2;

        assertEquals(expectedMax, orcoStatService.getMaxCountFor(3));
    }

    @Test
    void can_get_max_count_for_non_existent_data() {
        assertEquals(-1, orcoStatService.getMaxCountFor(5));
    }

    @Test
    void can_get_count_for_dailyLossStatistic_of_negative_alive_orcs() {
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.setDate("2022-11-21");
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addNegativelyAliveOrc(new Orc());

        Object dailyLossStatistic = orcoStatService.getDailyLosses().getDataByIndex(1);

        assertEquals(2, orcoStatService.getCountFor(dailyLossStatistic, 0));
    }

    @Test
    void can_get_count_for_dailyLossStatistic_of_destroyed_tanks() {
        orcoStatService.addDestroyedTank(new Tank());
        orcoStatService.setDate("2022-11-21");
        orcoStatService.addDestroyedTank(new Tank());
        orcoStatService.addDestroyedTank(new Tank());

        Object dailyLossStatistic = orcoStatService.getDailyLosses().getDataByIndex(1);

        assertEquals(2, orcoStatService.getCountFor(dailyLossStatistic, 1));
    }

    @Test
    void can_get_count_for_dailyLossStatistic_of_destroyed_drones() {
        orcoStatService.addDestroyedDrone(new Drone());
        orcoStatService.setDate("2022-11-21");
        orcoStatService.addDestroyedDrone(new Drone());
        orcoStatService.addDestroyedDrone(new Drone());

        Object dailyLossStatistic = orcoStatService.getDailyLosses().getDataByIndex(1);

        assertEquals(2, orcoStatService.getCountFor(dailyLossStatistic, 2));
    }

    @Test
    void can_get_count_for_dailyLossStatistic_of_destroyed_missiles() {
        orcoStatService.addDestroyedMissile(new Missile());
        orcoStatService.setDate("2022-11-21");
        orcoStatService.addDestroyedMissile(new Missile());
        orcoStatService.addDestroyedMissile(new Missile());

        Object dailyLossStatistic = orcoStatService.getDailyLosses().getDataByIndex(1);

        assertEquals(2, orcoStatService.getCountFor(dailyLossStatistic, 3));
    }

    @Test
    void can_get_count_for_dailyLossStatistic_of_non_existent_data() {
        Object dailyLossStatistic = orcoStatService.getDailyLosses().getDataByIndex(0);

        assertEquals(-1, orcoStatService.getCountFor(dailyLossStatistic, 10));
    }

    @Test
    void can_get_total_count_for_negative_alive_orcs() {
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.setDate("2022-11-21");
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addNegativelyAliveOrc(new Orc());

        int expectedMax = 3;

        assertEquals(expectedMax, orcoStatService.getTotalCountFor(0));
    }

    @Test
    void can_get_total_count_for_destroyed_tanks() {
        orcoStatService.addDestroyedTank(new Tank());
        orcoStatService.setDate("2022-11-21");
        orcoStatService.addDestroyedTank(new Tank());
        orcoStatService.addDestroyedTank(new Tank());

        int expectedMax = 3;

        assertEquals(expectedMax, orcoStatService.getTotalCountFor(1));
    }

    @Test
    void can_get_total_count_for_destroyed_drones() {
        orcoStatService.addDestroyedDrone(new Drone());
        orcoStatService.setDate("2022-11-21");
        orcoStatService.addDestroyedDrone(new Drone());
        orcoStatService.addDestroyedDrone(new Drone());

        int expectedMax = 3;

        assertEquals(expectedMax, orcoStatService.getTotalCountFor(2));
    }

    @Test
    void can_get_total_count_for_destroyed_missiles() {
        orcoStatService.addDestroyedMissile(new Missile());
        orcoStatService.setDate("2022-11-21");
        orcoStatService.addDestroyedMissile(new Missile());
        orcoStatService.addDestroyedMissile(new Missile());

        int expectedMax = 3;

        assertEquals(expectedMax, orcoStatService.getTotalCountFor(3));
    }

    @Test
    void can_get_total_count_for_non_existent_data() {
        assertEquals(-1, orcoStatService.getTotalCountFor(10));
    }

    @Test
    void can_get_max_count_in_dollars() {
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addDestroyedTank(new Tank());
        orcoStatService.setDate("2022-11-21");
        orcoStatService.addDestroyedTank(new Tank());

        assertEquals(3_010_000, orcoStatService.getMaxLosesCountInDollars());
    }

    @Test
    void can_get_daily_loses_as_print_version() {
        String table = orcoStatService.dailyLosesAsPrintVersion();

        ConvertorLosesToPrintVersion convertor = new ConvertorLosesToPrintVersion(orcoStatService);
        String expectedTable = "Daily loses count:" + convertor.convertDailyLosesToPrintVersion();

        assertEquals(expectedTable, table);
    }

    @Test
    void can_get_total_loses_as_print_version() {
        String table = orcoStatService.totalLosesAsPrintVersion();

        ConvertorLosesToPrintVersion convertor = new ConvertorLosesToPrintVersion(orcoStatService);
        String expectedTable = "Total loses count:" + convertor.convertTotalLosesToPrintVersion();

        assertEquals(expectedTable, table);
    }

    @Test
    void can_get_daily_loses_in_dollars_as_print_version() {
        String table = orcoStatService.dailyLosesInDollarsAsPrintVersion();

        ConvertorLosesToPrintVersion convertor = new ConvertorLosesToPrintVersion(orcoStatService);
        String expectedTable = "Daily loses in dollars:" + convertor.convertDailyDollarsLosesToPrintVersion();

        assertEquals(expectedTable, table);
    }

    @Test
    void can_get_total_loses_in_dollars_as_print_version() {
        String table = orcoStatService.totalLosesInDollarsAsPrintVersion();

        ConvertorLosesToPrintVersion convertor = new ConvertorLosesToPrintVersion(orcoStatService);
        String expectedTable = "Total loses in dollars:" + convertor.convertTotalDollarsLosesToPrintVersion();

        assertEquals(expectedTable, table);
    }
}

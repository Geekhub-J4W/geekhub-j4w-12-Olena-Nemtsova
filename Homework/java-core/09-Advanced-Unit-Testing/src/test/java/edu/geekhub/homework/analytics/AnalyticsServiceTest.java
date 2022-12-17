package edu.geekhub.homework.analytics;

import edu.geekhub.homework.domain.LosesStatistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    private AnalyticsService analyticsService;

    @Mock
    private LosesStatistic losesStatistic;

    @BeforeEach
    void setUp() {
        analyticsService = new AnalyticsService();
    }

    @Test
    void can_find_total_count_of_loses_for_statistic() {
        when(losesStatistic.tanks()).thenReturn(5);
        when(losesStatistic.cannons()).thenReturn(2);

        int totalSum = analyticsService.totalCountOfLosesForStatistic(losesStatistic);

        assertEquals(7, totalSum);
    }

    @Test
    void can_not_find_total_count_of_loses_for_null_statistic() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> analyticsService.totalCountOfLosesForStatistic(null)
        );

        assertEquals("LosesStatistic was null", thrown.getMessage());
    }

    @Test
    void can_find_total_count_of_loses_for_all_statistics() {
        when(losesStatistic.tanks()).thenReturn(5);
        when(losesStatistic.cannons()).thenReturn(2);
        List<LosesStatistic> losesStatisticList = List.of(losesStatistic, losesStatistic);

        assertEquals(14, analyticsService.totalCountOfLosesForAllStatistics(losesStatisticList));
    }

    @Test
    void can_not_find_total_count_of_loses_for_all_statistics_contains_null_object() {
        List<LosesStatistic> losesStatisticList = new ArrayList<>();
        losesStatisticList.add(null);
        losesStatisticList.add(losesStatistic);

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> analyticsService.totalCountOfLosesForAllStatistics(losesStatisticList)
        );

        assertEquals("LosesStatistic was null", thrown.getMessage());
    }

    @Test
    void can_find_max_loses_amounts() {
        LosesStatistic losesStatistic2 = losesStatistic;
        when(losesStatistic.tanks()).thenReturn(9);
        when(losesStatistic.cannons()).thenReturn(2);
        List<LosesStatistic> losesStatisticList = List.of(losesStatistic2, losesStatistic);

        assertEquals(losesStatistic, analyticsService.findStatisticWithMaxLosesAmounts(losesStatisticList));
    }

    @Test
    void can_not_find_max_loses_amounts_with_empty_list() {
        List<LosesStatistic> losesStatisticList = List.of();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> analyticsService.findStatisticWithMaxLosesAmounts(losesStatisticList)
        );
        assertEquals("Statistic with max loses amounts cannot be found", thrown.getMessage());
    }

    @Test
    void can_not_find_max_loses_amounts_with_list_contains_null_object() {
        List<LosesStatistic> losesStatisticList = new ArrayList<>();
        losesStatisticList.add(null);
        losesStatisticList.add(losesStatistic);

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> analyticsService.findStatisticWithMaxLosesAmounts(losesStatisticList)
        );
        assertEquals("LosesStatistic was null", thrown.getMessage());
    }

    @Test
    void can_find_min_loses_amounts() {
        LosesStatistic losesStatistic2 = losesStatistic;
        when(losesStatistic.tanks()).thenReturn(9);
        when(losesStatistic.cannons()).thenReturn(2);
        List<LosesStatistic> losesStatisticList = List.of(losesStatistic2, losesStatistic);

        assertEquals(losesStatistic2, analyticsService.findStatisticWithMinLosesAmounts(losesStatisticList));
    }

    @Test
    void can_not_find_min_loses_amounts_with_empty_list() {
        List<LosesStatistic> losesStatisticList = List.of();

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> analyticsService.findStatisticWithMinLosesAmounts(losesStatisticList)
        );
        assertEquals("Statistic with min loses amounts cannot be found", thrown.getMessage());
    }

    @Test
    void can_not_find_min_loses_amounts_with_list_contains_null_object() {
        List<LosesStatistic> losesStatisticList = new ArrayList<>();
        losesStatisticList.add(null);
        losesStatisticList.add(losesStatistic);

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> analyticsService.findStatisticWithMinLosesAmounts(losesStatisticList)
        );
        assertEquals("LosesStatistic was null", thrown.getMessage());
    }
}

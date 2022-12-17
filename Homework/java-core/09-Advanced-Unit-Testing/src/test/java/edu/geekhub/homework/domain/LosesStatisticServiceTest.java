package edu.geekhub.homework.domain;

import edu.geekhub.homework.client.LosesStatisticHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LosesStatisticServiceTest {
    private LosesStatisticService losesStatisticService;

    private final String losesStatisticJson = "{\"tanks\":\"5\"," +
        "\"armouredFightingVehicles\":\"5\"," +
        "\"cannons\":\"0\"," +
        "\"multipleRocketLaunchers\":\"0\"," +
        "\"antiAirDefenseDevices\":\"0\"," +
        "\"planes\":\"0\"," +
        "\"helicopters\":\"0\"," +
        "\"drones\":\"0\"," +
        "\"cruiseMissiles\":\"0\"," +
        "\"shipsOrBoats\":\"0\"," +
        "\"carsAndTankers\":\"0\"," +
        "\"specialEquipment\":\"0\"," +
        "\"personnel\":\"0\"," +
        "\"id\":\"1\"}";

    @Mock
    private LosesStatisticHttpClient losesStatisticHttpClient;

    @Mock
    private LosesStatistic losesStatistic;

    @Captor
    private ArgumentCaptor<String> losesStatisticJsonCaptor;

    @Captor
    private ArgumentCaptor<Integer> idCaptor;

    @BeforeEach
    void setUp() {
        losesStatisticService = new LosesStatisticService(losesStatisticHttpClient);
    }

    private void setBehaviorOfLosesStatisticMock() {
        when(losesStatistic.tanks()).thenReturn(5);
        when(losesStatistic.armouredFightingVehicles()).thenReturn(5);
        when(losesStatistic.id()).thenReturn(1);
    }

    @Test
    void can_get_all_loses_statistics() throws IOException, InterruptedException {
        setBehaviorOfLosesStatisticMock();
        String losesStatisticJson = String.join("",
            "[",
            this.losesStatisticJson,
            ",",
            this.losesStatisticJson.replaceAll("1", "2"),
            "]");
        when(losesStatisticHttpClient.getAll()).thenReturn(losesStatisticJson);
        List<LosesStatistic> losesStatisticList = losesStatisticService.getAll();

        verify(losesStatisticHttpClient, atLeastOnce()).getAll();

        LosesStatistic losesStatistic1 = new LosesStatistic(losesStatistic);
        when(losesStatistic.id()).thenReturn(2);
        List<LosesStatistic> expectedLosesStatisticList = Arrays.asList(losesStatistic1, new LosesStatistic(losesStatistic));

        assertEquals(expectedLosesStatisticList, losesStatisticList);
    }

    @Test
    void can_delete_loses_statistic_by_id() throws IOException, InterruptedException {
        losesStatisticService.deleteById(1);

        verify(losesStatisticHttpClient, atLeastOnce()).deleteById(idCaptor.capture());

        Integer capturedId = idCaptor.getValue();
        assertThat(capturedId).isEqualTo(1);
    }

    @Test
    void can_not_delete_loses_statistic_by_wrong_id() throws IOException, InterruptedException {
        doThrow(new IOException()).when(losesStatisticHttpClient).deleteById(1);
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> losesStatisticService.deleteById(1)
        );

        verify(losesStatisticHttpClient, atLeastOnce()).deleteById(1);

        assertEquals("Nothing found by id: 1", thrown.getMessage());
    }

    @Test
    void can_get_loses_statistic_by_id() throws IOException, InterruptedException {
        setBehaviorOfLosesStatisticMock();
        when(losesStatisticHttpClient.getById(1)).thenReturn(losesStatisticJson);
        LosesStatistic gotLosesStatistic = losesStatisticService.getById(1);

        verify(losesStatisticHttpClient, atLeastOnce()).getById(1);

        assertEquals(new LosesStatistic(losesStatistic), gotLosesStatistic);
    }

    @Test
    void can_not_get_loses_statistic_by_wrong_id() throws IOException, InterruptedException {
        when(losesStatisticHttpClient.getById(1)).thenThrow(new IOException());
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> losesStatisticService.getById(1)
        );

        verify(losesStatisticHttpClient, atLeastOnce()).getById(1);

        assertEquals("Nothing found by id: 1", thrown.getMessage());
    }

    @Test
    void can_create_loses_statistic() throws IOException, InterruptedException {
        setBehaviorOfLosesStatisticMock();
        LosesStatisticService losesStatisticService = spy(this.losesStatisticService);
        doReturn(new ArrayList<>()).when(losesStatisticService).getAll();
        losesStatisticService.create(losesStatistic);

        verify(losesStatisticHttpClient, atLeastOnce()).create(losesStatisticJsonCaptor.capture());

        String capturedLosesStatisticJson = losesStatisticJsonCaptor.getValue();
        assertThat(capturedLosesStatisticJson).isEqualTo("[" + losesStatisticJson + "]");
    }

    @Test
    void can_delete_all_loses_statistics() throws IOException, InterruptedException {
        losesStatisticService.deleteAll();

        verify(losesStatisticHttpClient, atLeastOnce()).deleteAll();
    }
}

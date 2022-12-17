package edu.geekhub.homework.client;

import edu.geekhub.homework.domain.LosesStatistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonConverterTest {
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
    private JsonConverter jsonConverter;

    @Mock
    private LosesStatistic losesStatistic;

    @BeforeEach
    void setUp() {
        jsonConverter = new JsonConverter();
    }

    private void setBehaviorOfLosesStatisticMock() {
        when(losesStatistic.tanks()).thenReturn(5);
        when(losesStatistic.armouredFightingVehicles()).thenReturn(5);
        when(losesStatistic.id()).thenReturn(1);
    }

    @Test
    void can_convert_to_Json() {
        setBehaviorOfLosesStatisticMock();
        String gotLosesStatisticJson = jsonConverter.convertToJson(losesStatistic);

        assertEquals(losesStatisticJson, gotLosesStatisticJson);
    }

    @Test
    void can_not_convert_null_LosesStatistic_to_Json() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> jsonConverter.convertToJson(null)
        );

        assertEquals("LosesStatistic was null", thrown.getMessage());
    }

    @Test
    void can_convert_to_entity() {
        setBehaviorOfLosesStatisticMock();
        LosesStatistic gotLosesStatistic = jsonConverter.convertToEntity(losesStatisticJson);

        assertEquals(new LosesStatistic(losesStatistic), gotLosesStatistic);
    }

    @Test
    void can_not_convert_to_entity_wrong_Json() {
        String losesStatisticJson = this.losesStatisticJson.substring(0, this.losesStatisticJson.length() / 2);
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> jsonConverter.convertToEntity(losesStatisticJson)
        );

        assertTrue(thrown.getMessage().contains("Nothing found by key: "));
    }

    @Test
    void can_convert_to_entities() {
        String losesStatisticJson = String.join("",
            "[",
            this.losesStatisticJson,
            ",",
            this.losesStatisticJson.replaceAll("1", "2"),
            "]");
        List<LosesStatistic> losesStatisticList = jsonConverter.convertToEntities(losesStatisticJson);

        setBehaviorOfLosesStatisticMock();
        LosesStatistic losesStatistic1 = new LosesStatistic(losesStatistic);
        when(losesStatistic.id()).thenReturn(2);
        List<LosesStatistic> expectedLosesStatisticList = Arrays.asList(losesStatistic1, new LosesStatistic(losesStatistic));

        assertEquals(expectedLosesStatisticList, losesStatisticList);
    }
}

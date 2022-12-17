package edu.geekhub.homework.domain;

import edu.geekhub.homework.client.JsonConverter;
import edu.geekhub.homework.client.LosesStatisticHttpClient;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * Service should fetch loses statistic data as a {@link String} object, then convert it into a
 * {@link LosesStatistic} by using {@link } class and return a result if possible.
 * <br/>
 * <br/>
 * IMPORTANT: DO NOT CHANGE METHODS SIGNATURE
 */
public class LosesStatisticService {

    private final LosesStatisticHttpClient losesStatisticHttpClient;

    public LosesStatisticService() {
        this(new LosesStatisticHttpClient());
    }

    public LosesStatisticService(LosesStatisticHttpClient losesStatisticHttpClient) {
        this.losesStatisticHttpClient = losesStatisticHttpClient;
    }

    public List<LosesStatistic> getAll() {
        String jsonData;
        try {
            jsonData = losesStatisticHttpClient.getAll();
            JsonConverter converter = new JsonConverter();
            return converter.convertToEntities(jsonData);
        } catch (IOException | InterruptedException e) {
            return getAll();
        }
    }

    public LosesStatistic getById(Integer id) {
        JsonConverter converter = new JsonConverter();
        try {
            String losesStatisticJson = losesStatisticHttpClient.getById(id);
            return converter.convertToEntity(losesStatisticJson);
        } catch (InterruptedException e) {
            return getById(id);
        } catch (IOException e) {
            throw new IllegalArgumentException("Nothing found by id: " + id);
        }
    }

    public void deleteAll() {
        try {
            losesStatisticHttpClient.deleteAll();
        } catch (IOException | InterruptedException e) {
            deleteAll();
        }
    }

    public void deleteById(int id) {
        try {
            losesStatisticHttpClient.deleteById(id);
        } catch (InterruptedException e) {
            deleteById(id);
        } catch (IOException e) {
            throw new IllegalArgumentException("Nothing found by id: " + id);
        }
    }

    public void create(LosesStatistic losesStatistic) {
        JsonConverter converter = new JsonConverter();

        List<LosesStatistic> losesStatisticList = getAll();
        losesStatisticList.add(losesStatistic);
        losesStatisticList.sort(Comparator.comparingInt(LosesStatistic::id));
        StringBuilder jsonData = new StringBuilder();
        jsonData.append('[');
        for (var it : losesStatisticList) {
            jsonData.append(converter.convertToJson(it));
            jsonData.append(',');
        }
        jsonData.deleteCharAt(jsonData.length() - 1);
        jsonData.append(']');

        try {
            losesStatisticHttpClient.create(jsonData.toString());
        } catch (IOException | InterruptedException e) {
            create(losesStatistic);
        }
    }
}

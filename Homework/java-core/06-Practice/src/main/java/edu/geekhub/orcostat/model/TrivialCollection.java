package edu.geekhub.orcostat.model;

import java.util.Arrays;
import java.util.Objects;

public class TrivialCollection {
    private Object[] data;

    public TrivialCollection() {
        data = new Object[0];
    }

    public void add(Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        data = Arrays.copyOf(data, data.length + 1);
        data[data.length - 1] = object;
    }

    public int count() {
        return data.length;
    }

    public Object[] getData() {
        return data;
    }

    public void changeValue(int index, Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (index > data.length - 1 || index < 0) {
            throw new IllegalArgumentException("Index is out of array");
        }

        data[index] = object;
    }

    public Object getDataByIndex(int index) {
        if (index > data.length - 1 || index < 0) {
            throw new IllegalArgumentException("Index is out of array");
        }

        return data[index];
    }
}

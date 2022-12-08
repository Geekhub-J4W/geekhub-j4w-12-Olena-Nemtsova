package edu.geekhub.homework.hw1;

import java.util.Iterator;
import java.util.List;

public class OddIndexIterator<E> implements Iterator<E> {
    private final List<E> data;
    int position = 1;

    public OddIndexIterator(List<E> data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return validatePosition();
    }

    @Override
    public E next() {
        if (!validatePosition()) {
            throw new IllegalArgumentException("Position is wrong");
        }

        E nextValue = data.get(position);
        position += 2;
        return nextValue;
    }

    private boolean validatePosition() {
        return validateSizePosition() && validateOddPosition();
    }

    private boolean validateOddPosition() {
        return position % 2 != 0;
    }

    private boolean validateSizePosition() {
        return position < data.size() && position > 0;
    }

}

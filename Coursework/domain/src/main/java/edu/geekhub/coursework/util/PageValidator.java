package edu.geekhub.coursework.util;

import org.springframework.stereotype.Component;

@Component
public class PageValidator {

    public void validatePageLimit(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("Minimum page items limit is 1 but was: " + limit);
        }
    }

    public void validatePageNumber(int pageNumber, int maxPageNumber) {
        if (pageNumber < 1) {
            throw new IllegalArgumentException("Minimum page number is 1 but was: " + pageNumber);
        }

        if (pageNumber > maxPageNumber) {
            throw new IllegalArgumentException("Maximum page number is " + maxPageNumber
                                               + " but was: " + pageNumber);
        }
    }
}

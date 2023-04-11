package edu.geekhub.homework;

import edu.geekhub.homework.categories.Category;
import org.tinylog.Logger;

public class CategoryConsoleParser {
    public Category parse(String consoleInput) {
        try {
            validateConsoleInput(consoleInput);

            return new Category(consoleInput);
        } catch (IllegalArgumentException exception) {
            Logger.warn(exception.getMessage());
        }
        return null;
    }

    private void validateConsoleInput(String consoleInput) {
        if (consoleInput == null) {
            throw new IllegalArgumentException("Wrong category input '"
                                               + null
                                               + "'");
        }
    }
}

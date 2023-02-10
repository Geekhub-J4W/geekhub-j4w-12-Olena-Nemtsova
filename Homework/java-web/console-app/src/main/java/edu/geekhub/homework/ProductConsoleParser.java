package edu.geekhub.homework;

import edu.geekhub.homework.domain.Product;
import org.tinylog.Logger;

public class ProductConsoleParser {

    public Product parse(String consoleInput) {
        String[] consoleInputParts = consoleInput.split(",");
        try {
            validateConsoleInput(consoleInputParts);

            String name = consoleInputParts[0].trim();
            double price = Double.parseDouble(consoleInputParts[1].trim());

            return new Product(name, price);
        } catch (NumberFormatException exception) {
            Logger.warn("Wrong price input: " + exception.getMessage());
        } catch (IllegalArgumentException exception) {
            Logger.warn(exception.getMessage());
        }
        return null;
    }

    private void validateConsoleInput(String[] consoleInputParts) {
        if (consoleInputParts.length != 2) {
            throw new IllegalArgumentException("Wrong input arguments count at '"
                + String.join(", ", consoleInputParts)
                + "'");
        }
    }
}

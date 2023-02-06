package edu.geekhub.homework.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesFileReader {
    private static Path propertiesFilePath = Path.of("Homework/java-core/13-Reflection-Api/src/main/resources/application.properties");

    private PropertiesFileReader() {
    }

    public static Map<String, String> readDataFromFile() {
        Map<String, String> properties = new HashMap<>();
        if (Files.exists(propertiesFilePath)) {
            try (java.io.FileReader fr = new java.io.FileReader(propertiesFilePath.toFile());
                 BufferedReader reader = new BufferedReader(fr)) {
                String line = reader.readLine();

                while (line != null) {
                    if (validFileLine(line)) {
                        properties.put(getPropertyName(line), getPropertyValue(line));
                    }
                    line = reader.readLine();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return properties;
    }

    private static boolean validFileLine(String line) {
        boolean isValid = !line.isBlank() && line.split("=").length == 2;
        if (!isValid) {
            Logger logger = Logger.getGlobal();
            String message = "Wrong file line: " + line;
            logger.log(Level.WARNING, message);
        }
        return isValid;
    }

    private static String getPropertyValue(String line) {
        return line.split("=")[1].trim();
    }

    private static String getPropertyName(String line) {
        return Arrays.stream(line.split("=")[0]
                .split("\\."))
            .reduce((e1, e2) -> e2)
            .orElse("")
            .trim();
    }
}

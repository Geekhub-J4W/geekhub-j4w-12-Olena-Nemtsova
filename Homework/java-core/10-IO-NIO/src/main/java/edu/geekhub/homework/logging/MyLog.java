package edu.geekhub.homework.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record MyLog(LocalDateTime dateTime, Level level, String message) {

    @Override
    public String toString() {
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            + "\t"
            + level
            + ":\t"
            + message;
    }
}

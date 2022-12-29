package edu.geekhub.homework.downloading;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public record Song(Path path, String name, String link) {
    public String correctEncodingName() {
        return new String(name.getBytes(StandardCharsets.UTF_8));
    }
}

package edu.geekhub.homework.downloading;

import java.nio.charset.StandardCharsets;

public record Song(String path, String name, String link) {
    public String correctEncodingName() {
        return new String(name.getBytes(StandardCharsets.UTF_8));
    }
}

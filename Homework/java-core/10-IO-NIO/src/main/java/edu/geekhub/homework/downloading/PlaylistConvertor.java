package edu.geekhub.homework.downloading;

import edu.geekhub.homework.logging.Level;
import edu.geekhub.homework.logging.MyLogger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class PlaylistConvertor {
    private static final File PLAYLIST_FILE = new File("Homework/java-core/10-IO-NIO/src/main/resources/playlist.txt");
    private final Path playlistFile;

    public PlaylistConvertor(Path playlistFile) {
        this.playlistFile = Optional.ofNullable(playlistFile)
            .orElseGet(PLAYLIST_FILE::toPath);
    }

    public PlaylistConvertor() {
        this(PLAYLIST_FILE.toPath());
    }

    public List<Song> convertAll() {
        List<Song> songs = new ArrayList<>();
        List<String> linesOfFile = getSeparatedFileLines();

        for (String line : linesOfFile) {
            try {
                songs.add(convertLineToSong(line));

                String lastSongName = songs.get(songs.size() - 1).correctEncodingName();
                MyLogger.log(Level.INFO, "Song converted from line successfully: " + lastSongName);
            } catch (IllegalArgumentException ex) {
                MyLogger.log(Level.ERROR, ex.getMessage());
            }
        }
        return songs;
    }

    protected Song convertLineToSong(String line) throws IllegalArgumentException {
        List<String> separatedInfo = Arrays.stream(line.split("\\|"))
            .map(String::strip)
            .toList();
        if (separatedInfo.size() != 5) {
            throw new IllegalArgumentException("Wrong song entry: " + line);
        }

        String musicFolderPath = getMusicFolderPath();
        Path path = Path.of(musicFolderPath, separatedInfo.get(0), separatedInfo.get(1), separatedInfo.get(2));
        String name = String.join("", separatedInfo.get(3), ".mp3");
        return new Song(path, name, separatedInfo.get(4));
    }

    protected String getMusicFolderPath() {
        String userFolderPath = System.getProperty("user.home");
        return Path.of(userFolderPath, "Music").toString();
    }

    protected List<String> getSeparatedFileLines() {
        try (Stream<String> lineStream = Files.lines(playlistFile)) {
            return lineStream.toList();
        } catch (IOException ex) {
            MyLogger.log(Level.ERROR, ex.getMessage());
            return new ArrayList<>();
        }
    }
}

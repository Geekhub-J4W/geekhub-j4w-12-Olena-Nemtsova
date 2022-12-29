package edu.geekhub.homework.downloading;

import edu.geekhub.homework.logging.Level;
import edu.geekhub.homework.logging.MyLogger;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class SongDownloader {
    private final PlaylistConvertor playlistConvertor;

    public SongDownloader() {
        this(new PlaylistConvertor());
    }

    public SongDownloader(PlaylistConvertor playlistConvertor) {
        this.playlistConvertor = playlistConvertor;
    }

    public void downloadAll() {
        List<Song> songs = playlistConvertor.convertAll();
        if (songs.isEmpty()) {
            MyLogger.log(Level.ERROR, "No songs found");
        }

        for (Song song : songs) {
            downloadSong(song);
        }
    }

    void downloadSong(Song song) {
        try {
            validateSong(song);

            URL url = new URL(song.link());
            validateFileSize(url, song.correctEncodingName());

            createDirectoriesForSong(song.path());
            Path fullPath = getFullPath(song);
            validateFileIsNew(fullPath, song.correctEncodingName());

            int maxUnitUpload5KB = 5 * 1024;
            try (FileOutputStream out = new FileOutputStream(fullPath.toFile());
                 InputStream in = url.openStream()) {
                byte[] buffer = new byte[maxUnitUpload5KB];
                int bytesRead;

                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                    buffer = new byte[maxUnitUpload5KB];
                }
                MyLogger.log(Level.INFO, "Song downloaded successfully: " + song.correctEncodingName());
            } catch (IOException ex) {
                MyLogger.log(Level.ERROR, ex.getMessage());
            }
        } catch (Exception ex) {
            MyLogger.log(Level.ERROR, ex.getMessage());
        }
    }

    void createDirectoriesForSong(Path songPath) throws IOException {
        Files.createDirectories(songPath);
    }

    Path getFullPath(Song song) {
        return Path.of(song.path().toString(), song.name());
    }

    private void validateFileSize(URL url, String songName) throws IOException {
        int songSize = url.openConnection().getContentLength();
        int maxSize10MB = 10 * 1024 * 1024;

        if (songSize > maxSize10MB) {
            throw new IllegalArgumentException("File is too long: " + songName);
        }
    }

    void validateFileIsNew(Path filePath, String songName) throws IllegalArgumentException {
        if (Files.exists(filePath)) {
            throw new IllegalArgumentException("File already exist: " + songName);
        }
    }

    private void validateSong(Song song) throws IllegalArgumentException {
        Song song1 = Optional.ofNullable(song).orElseThrow(() -> new IllegalArgumentException("Song was null"));

        if (song1.path().toString().isBlank() || song1.name().isBlank() || song1.link().isBlank()) {
            throw new IllegalArgumentException("Song contains blank data:" + song);
        }
        if (!song1.link().endsWith(".mp3")) {
            throw new IllegalArgumentException("Song link must ends with .mp3: " + song1.correctEncodingName());
        }
        if (!song1.name().endsWith(".mp3")) {
            throw new IllegalArgumentException("Song name must ends with .mp3:" + song1.correctEncodingName());
        }
    }
}

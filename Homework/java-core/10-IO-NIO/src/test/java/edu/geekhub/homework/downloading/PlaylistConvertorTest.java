package edu.geekhub.homework.downloading;

import edu.geekhub.homework.logging.MyLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaylistConvertorTest {
    private PlaylistConvertor playlistConvertor;
    private static final String FILE_LINE = "Dir 1 | Dir 2 | Dir 3 | Name | Link";
    private Song song;

    @BeforeEach
    void setUp() {
        MyLogger.cleanAll();
        playlistConvertor = new PlaylistConvertor();
        song = new Song(Path.of("Dir 1", "Dir 2", "Dir 3"), "Name.mp3", "Link");
    }

    @Test
    void can_convert_all() {
        List<String> separatedFileLines = List.of(FILE_LINE, FILE_LINE);
        PlaylistConvertor playlistConvertor = spy(this.playlistConvertor);

        doReturn(separatedFileLines).when(playlistConvertor).getSeparatedFileLines();
        doReturn(song).when(playlistConvertor).convertLineToSong(FILE_LINE);
        List<Song> songs = playlistConvertor.convertAll();

        List<Song> expectedSongs = List.of(song, song);

        assertThat(songs).isEqualTo(expectedSongs);
        assertEquals("Song converted from line successfully: " + song.correctEncodingName(), MyLogger.getAll().get(0).message());
        assertEquals("Song converted from line successfully: " + song.correctEncodingName(), MyLogger.getAll().get(1).message());
    }

    @Test
    void can_convert_all_when_file_contains_wrong_line() {
        String wrongFileLine = "Dir 1 | Dir 3 | Name | Link";
        List<String> separatedFileLines = List.of(FILE_LINE, wrongFileLine);
        PlaylistConvertor playlistConvertor = spy(this.playlistConvertor);

        doReturn(separatedFileLines).when(playlistConvertor).getSeparatedFileLines();
        doReturn(song).when(playlistConvertor).convertLineToSong(FILE_LINE);
        doThrow(new IllegalArgumentException("Wrong song entry: " + wrongFileLine)).when(playlistConvertor).convertLineToSong(wrongFileLine);
        List<Song> songs = playlistConvertor.convertAll();

        List<Song> expectedSongs = List.of(song);

        assertThat(songs).isEqualTo(expectedSongs);
        assertEquals("Song converted from line successfully: " + song.correctEncodingName(), MyLogger.getAll().get(0).message());
        assertEquals("Wrong song entry: " + wrongFileLine, MyLogger.getAll().get(1).message());
    }

    @Test
    void can_convert_file_line_to_Song() {
        PlaylistConvertor playlistConvertor = spy(this.playlistConvertor);
        doReturn("").when(playlistConvertor).getMusicFolderPath();
        Song song = playlistConvertor.convertLineToSong(FILE_LINE);

        Song expectedSong = this.song;
        assertThat(song).isEqualTo(expectedSong);
    }

    @Test
    void can_not_convert_wrong_file_line_to_Song() {
        String wrongFileLine = "Dir 1 | Dir 3 | Name | Link";

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> playlistConvertor.convertLineToSong(wrongFileLine)
        );

        assertEquals("Wrong song entry: " + wrongFileLine, thrown.getMessage());
    }

    @Test
    void can_get_separated_file_lines() throws IOException {
        Path tempFile = createTempFile();
        playlistConvertor = new PlaylistConvertor(tempFile);
        List<String> separatedFileLines = playlistConvertor.getSeparatedFileLines();
        tempFile.toFile().deleteOnExit();

        List<String> expectedSeparatedFileLines = List.of(FILE_LINE, FILE_LINE);

        assertThat(separatedFileLines).isEqualTo(expectedSeparatedFileLines);
    }

    private Path createTempFile() throws IOException {
        Path tempFile = Files.createTempFile("playlist", ".txt");
        Files.writeString(tempFile, String.join("\n", FILE_LINE, FILE_LINE));
        return tempFile;
    }

    @Test
    void can_get_empty_list_from_empty_file() throws IOException {
        Path tempFile = Files.createTempFile("playlist", ".txt");
        playlistConvertor = new PlaylistConvertor(tempFile);
        List<String> separatedFileLines = playlistConvertor.getSeparatedFileLines();
        tempFile.toFile().deleteOnExit();

        List<String> expectedSeparatedFileLines = new ArrayList<>();

        assertThat(separatedFileLines).isEqualTo(expectedSeparatedFileLines);
    }

    @Test
    void can_get_empty_list_from_null_file() {
        playlistConvertor = new PlaylistConvertor(null);
        List<String> separatedFileLines = playlistConvertor.getSeparatedFileLines();

        List<String> expectedSeparatedFileLines = new ArrayList<>();

        assertThat(separatedFileLines).isEqualTo(expectedSeparatedFileLines);
    }

    @Test
    void can_get_empty_list_from_not_exist_file() {
        Path path = Path.of("does-not-exist.txt");
        playlistConvertor = new PlaylistConvertor(path);
        List<String> separatedFileLines = playlistConvertor.getSeparatedFileLines();

        List<String> expectedSeparatedFileLines = new ArrayList<>();

        assertThat(separatedFileLines).isEqualTo(expectedSeparatedFileLines);
        assertFalse(MyLogger.getAll().isEmpty());
    }

    @Test
    void can_get_music_folder_path() {
        String musicFolderPath = playlistConvertor.getMusicFolderPath();

        String expectedMusicFolderPath = Path.of(System.getProperty("user.home"), "Music").toString();

        assertEquals(expectedMusicFolderPath, musicFolderPath);
    }
}

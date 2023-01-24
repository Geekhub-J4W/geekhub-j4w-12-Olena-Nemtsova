package edu.geekhub.homework.downloading;

import edu.geekhub.homework.logging.MyLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongDownloaderTest {
    @Mock
    private PlaylistConvertor playlistConvertor;
    private SongDownloader songDownloader;
    private Song song;

    @BeforeEach
    void setUp() {
        MyLogger.cleanAll();
        songDownloader = new SongDownloader(playlistConvertor);
        song = new Song(Path.of("Dir 1", "Dir 2", "Dir 3"), "Name.mp3", "https://ia800300.us.archive.org/35/items/MetallicaMasterOfPuppets_0/02__Master_Of_Puppets_64kb.mp3");
    }

    @Test
    void can_create_SongDownloader_without_arguments() {
        songDownloader = new SongDownloader();
        assertNotNull(songDownloader);
    }

    @Test
    void can_download_all_songs() throws InterruptedException, ExecutionException {
        when(playlistConvertor.convertAll()).thenReturn(List.of(song, song));
        SongDownloader songDownloader = spy(this.songDownloader);
        doNothing().when(songDownloader).downloadSong(song);
        songDownloader.downloadAll();

        verify(songDownloader, atLeast(2)).downloadSong(any());
    }

    @Test
    void can_not_download_all_songs_with_empty_list() throws InterruptedException, ExecutionException {
        when(playlistConvertor.convertAll()).thenReturn(new ArrayList<>());
        SongDownloader songDownloader = spy(this.songDownloader);
        songDownloader.downloadAll();

        verify(songDownloader, times(0)).downloadSong(any());
        assertEquals("No songs found", MyLogger.getAll().get(0).message());
    }

    @Test
    void can_download_song() throws IOException {
        SongDownloader songDownloader = spy(this.songDownloader);
        doNothing().when(songDownloader).createDirectoriesForSong(any());
        doNothing().when(songDownloader).validateFileIsNew(any(), any());
        Path tempFile = Files.createTempFile("Name", ".mp3");
        doReturn(tempFile).when(songDownloader).getFullPath(any());

        songDownloader.downloadSong(song);
        tempFile.toFile().deleteOnExit();

        assertEquals("Song downloaded successfully: " + song.correctEncodingName(), MyLogger.getAll().get(0).message());
    }

    @Test
    void can_not_download_null_song() {
        songDownloader.downloadSong(null);

        assertEquals("Song was null", MyLogger.getAll().get(0).message());
    }

    @Test
    void can_not_download_song_contains_empty_path() {
        Song song = spy(this.song);
        doReturn(Path.of("")).when(song).path();
        songDownloader.downloadSong(song);

        assertEquals("Song contains blank data:" + song, MyLogger.getAll().get(0).message());
    }

    @Test
    void can_not_download_song_contains_empty_name() {
        Song song = spy(this.song);
        doReturn("").when(song).name();
        songDownloader.downloadSong(song);

        assertEquals("Song contains blank data:" + song, MyLogger.getAll().get(0).message());
    }

    @Test
    void can_not_download_song_contains_empty_link() {
        Song song = spy(this.song);
        doReturn("").when(song).link();
        songDownloader.downloadSong(song);

        assertEquals("Song contains blank data:" + song, MyLogger.getAll().get(0).message());
    }

    @Test
    void can_not_download_song_name_not_ends_with_mp3() {
        Song song = spy(this.song);
        doReturn("Name").when(song).name();
        songDownloader.downloadSong(song);

        assertEquals("Song name must ends with .mp3:" + song.correctEncodingName(), MyLogger.getAll().get(0).message());
    }

    @Test
    void can_not_download_song_link_not_ends_with_mp3() {
        Song song = spy(this.song);
        doReturn("some link").when(song).link();
        songDownloader.downloadSong(song);

        assertEquals("Song link must ends with .mp3: " + song.correctEncodingName(), MyLogger.getAll().get(0).message());
    }

    @Test
    void can_not_download_song_link_is_wrong() {
        Song song = spy(this.song);
        doReturn("some link.mp3").when(song).link();
        songDownloader.downloadSong(song);

        assertEquals("no protocol: some link.mp3", MyLogger.getAll().get(0).message());
    }

    @Test
    void can_not_download_song_size_more_than_10MB() {
        Song song = spy(this.song);
        doReturn("https://ia903003.us.archive.org/34/items/metallicatheunforgiven/Metallica%20-%20The%20Unforgiven.mp3").when(song).link();
        songDownloader.downloadSong(song);

        assertEquals("File is too long: " + song.correctEncodingName(), MyLogger.getAll().get(0).message());
    }

    @Test
    void can_not_download_song_already_exists() throws IOException {
        SongDownloader songDownloader = spy(this.songDownloader);
        doNothing().when(songDownloader).createDirectoriesForSong(any());
        Path tempFile = Files.createTempFile("Name", ".mp3");
        doReturn(tempFile).when(songDownloader).getFullPath(any());

        songDownloader.downloadSong(song);
        tempFile.toFile().deleteOnExit();
        assertEquals("File already exist: " + song.correctEncodingName(), MyLogger.getAll().get(0).message());
    }

    @Test
    void can_get_full_path_from_Song() {
        Path fullPath = songDownloader.getFullPath(song);

        Path expectedFullPath = Path.of(song.path().toString(), song.name());

        assertEquals(expectedFullPath, fullPath);
    }

    @Test
    void can_create_directories_for_song() throws IOException {
        Path tempDir = Files.createTempDirectory("tempMusic");
        Path fullPath = Path.of(tempDir.toString(), song.path().toString());
        songDownloader.createDirectoriesForSong(fullPath);

        assertTrue(Files.exists(fullPath));
        tempDir.toFile().deleteOnExit();
    }
}

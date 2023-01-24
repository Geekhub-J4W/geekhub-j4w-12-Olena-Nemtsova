package edu.geekhub.homework.track;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class FieldGeneratorTest {
    private FieldGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new FieldGenerator();
    }

    @Test
    void can_not_create_field_with_track_blocks_count_less_than_1() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> generator.generateField(0)
        );

        assertEquals("Count of track blocks must be more than 1", thrown.getMessage());
    }

    @Test
    void can_not_create_field_with_track_blocks_count_more_than_10() {
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> generator.generateField(100)
        );

        assertEquals("Count of track blocks must be less than 10", thrown.getMessage());
    }

    @Test
    void can_generate_field() throws NoSuchAlgorithmException {
        FieldGenerator generator = spy(this.generator);
        doNothing().when(generator).generateStartBlock();
        doNothing().when(generator).generateTrackBlocks(anyInt());
        doNothing().when(generator).generateFinishBlock();

        Field field = generator.generateField(2);

        verify(generator, times(1)).generateStartBlock();
        verify(generator, times(1)).generateTrackBlocks(anyInt());
        verify(generator, times(1)).generateFinishBlock();
        assertNotEquals(null, field);
    }

    @Test
    void can_generate_start_block() throws NoSuchAlgorithmException {
        Field field = generator.generateField(2);

        Block expectedStartBlock = Block.generateBlock(new Point(0, 0));

        assertEquals(expectedStartBlock, field.startBlock());
    }

    @Test
    void can_generate_track_blocks() throws NoSuchAlgorithmException {
        Field field = generator.generateField(2);
        int trackBlocksCount = field.trackBlocks().size();

        assertEquals(2, trackBlocksCount);
    }

    @Test
    void can_generate_track_blocks_not_over_start_block() throws NoSuchAlgorithmException {
        Field field = generator.generateField(10);
        List<Block> trackBlocks = field.trackBlocks();

        Block startBlock = field.startBlock();

        assertFalse(trackBlocks.contains(startBlock));
    }

    @Test
    void can_generate_finish_block() throws NoSuchAlgorithmException {
        Field field = generator.generateField(2);
        Block finishBlock = field.finishBlock();

        assertNotNull(finishBlock);
    }

    @Test
    void can_generate_finish_block_not_over_start_or_track_blocks() throws NoSuchAlgorithmException {
        Field field = generator.generateField(10);
        Block finishBlock = field.finishBlock();

        List<Block> trackBlocks = field.trackBlocks();
        Block startBlock = field.startBlock();

        assertFalse(trackBlocks.contains(finishBlock));
        assertNotEquals(startBlock, finishBlock);
    }
}

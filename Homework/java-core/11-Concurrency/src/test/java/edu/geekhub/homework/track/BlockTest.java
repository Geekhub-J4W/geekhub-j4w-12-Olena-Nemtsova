package edu.geekhub.homework.track;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BlockTest {

    @Test
    void can_generate_block_by_central_point() {
        Point centralPoint = new Point(0, 0);
        Block block = Block.generateBlock(centralPoint);

        List<Point> expectedPointsOfBlock = List.of(new Point(-1, -1),
            new Point(-1, 0),
            new Point(-1, 1),
            new Point(0, -1),
            centralPoint,
            new Point(0, 1),
            new Point(1, -1),
            new Point(1, 0),
            new Point(1, 1));

        assertEquals(expectedPointsOfBlock, block.getPoints());
    }

    @Test
    void can_get_central_point() {
        Point centralPoint = new Point(0, 0);
        Block block = Block.generateBlock(centralPoint);

        assertEquals(centralPoint, block.getCentralPoint());
    }

    @Test
    void can_compared_blocks() {
        Point centralPoint = new Point(0, 0);
        Point anotherCentralPoint = new Point(0, 1);
        Block block1 = Block.generateBlock(centralPoint);
        Block block2 = Block.generateBlock(centralPoint);
        Block block3 = Block.generateBlock(anotherCentralPoint);

        assertEquals(block1, block2);
        assertNotEquals(block1, block3);
    }

    @Test
    void can_compared_by_hashcode() {
        Point centralPoint = new Point(0, 0);
        Point anotherCentralPoint = new Point(0, 1);
        Block block1 = Block.generateBlock(centralPoint);
        Block block2 = Block.generateBlock(centralPoint);
        Block block3 = Block.generateBlock(anotherCentralPoint);

        assertEquals(block1.hashCode(), block2.hashCode());
        assertNotEquals(block1.hashCode(), block3.hashCode());
    }
}

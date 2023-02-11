package edu.geekhub.homework.track;

import edu.geekhub.homework.util.Course;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FieldGenerator {
    private Block startBlock;
    private List<Block> trackBlocks;
    private Block finishBlock;

    public Field generateField(int truckBlocksCount) throws NoSuchAlgorithmException {
        validateTrackBlocksCount(truckBlocksCount);

        generateStartBlock();
        generateTrackBlocks(truckBlocksCount);
        generateFinishBlock();

        return new Field(startBlock, trackBlocks, finishBlock);
    }

    private void validateTrackBlocksCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count of track blocks must be more than 1");
        }
        if (count > 10) {
            throw new IllegalArgumentException("Count of track blocks must be less than 10");
        }
    }

    protected void generateStartBlock() {
        Point startBlockCentralPoint = new Point(0, 0);
        startBlock = Block.generateBlock(startBlockCentralPoint);
    }

    protected void generateTrackBlocks(int truckBlocksCount) throws NoSuchAlgorithmException {
        Point trackBlockPoint = startBlock.getCentralPoint();
        trackBlocks = new ArrayList<>();

        while (trackBlocks.size() != truckBlocksCount) {
            Course course = Course.getRandomCourse();
            Point newTrackBlockPoint = trackBlockPoint.generateNewPointByCourse(course);

            if (!newTrackBlockPoint.equals(startBlock.getCentralPoint())) {
                trackBlockPoint = newTrackBlockPoint;
                trackBlocks.add(Block.generateBlock(trackBlockPoint));
            }
        }
    }

    protected void generateFinishBlock() throws NoSuchAlgorithmException {
        Point lastTrackBlockPoint = trackBlocks.get(trackBlocks.size() - 1).getCentralPoint();

        Point finishBlockCentralPoint = startBlock.getCentralPoint();
        while (!validatedFinishCentralPoint(finishBlockCentralPoint)) {
            Course course = Course.getRandomCourse();
            finishBlockCentralPoint = lastTrackBlockPoint.generateNewPointByCourse(course);
        }

        finishBlock = Block.generateBlock(finishBlockCentralPoint);
    }

    private boolean validatedFinishCentralPoint(Point point) {
        List<Point> trackBlocksCentralPoints = trackBlocks.stream()
            .map(Block::getCentralPoint).toList();

        return !trackBlocksCentralPoints.contains(point)
            && !point.equals(startBlock.getCentralPoint());
    }
}

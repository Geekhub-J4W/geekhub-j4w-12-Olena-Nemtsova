package edu.geekhub.homework.track;

import edu.geekhub.homework.model.Transport;
import edu.geekhub.homework.util.Course;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Field {
    private Block startBlock;
    private Block finishBlock;
    private List<Block> trackBlocks;
    private final Map<Point, Transport> occupiedPoints = new HashMap<>();

    public Field(int truckBlocksCount) throws NoSuchAlgorithmException {
        validateTrackBlocksCount(truckBlocksCount);
        generateField(truckBlocksCount);
    }

    private void validateTrackBlocksCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Count of track blocks must be more than 1");
        }
        if (count > 10) {
            throw new IllegalArgumentException("Count of track blocks must be less than 10");
        }
    }

    public synchronized boolean isPointOccupied(Point point) {
        return occupiedPoints.containsKey(point);
    }

    public boolean isFieldPoint(Point point) {
        return startBlock.getPoints().contains(point) ||
            trackBlocks.stream().anyMatch((Block b) -> b.getPoints().contains(point)) ||
            finishBlock.getPoints().contains(point);
    }

    public boolean isFinishPoint(Point point) {
        return finishBlock.getPoints().contains(point);
    }

    public synchronized void occupyPoint(Point pointToOccupy, Point pointToRelease, Transport transport) {
        occupiedPoints.put(pointToOccupy, transport);
        releasePoint(pointToRelease);
    }

    public synchronized void releasePoint(Point point) {
        occupiedPoints.remove(point);
    }

    public synchronized Transport getTransportAtPoint(Point point) {
        return occupiedPoints.get(point);
    }

    public synchronized Point getRandomFreeStartPoint() {
        return startBlock.getPoints().stream()
            .filter((Point p) -> occupiedPoints.keySet().stream().noneMatch(p::equals))
            .findAny().orElse(null);
    }

    void generateField(int n) throws NoSuchAlgorithmException {
        generateStartBlock();

        generateTrackBlocks(n);

        generateFinishBlock();
    }

    void generateStartBlock() {
        Point startBlockCentralPoint = new Point(0, 0);
        startBlock = Block.generateBlock(startBlockCentralPoint);
    }

    void generateTrackBlocks(int n) throws NoSuchAlgorithmException {
        Point trackBlockPoint = startBlock.getCentralPoint();
        trackBlocks = new ArrayList<>();

        while (trackBlocks.size() != n) {
            Course course = Course.getRandomCourse();
            Point newTrackBlockPoint = trackBlockPoint.generateNewPointByCourse(course);

            if (!newTrackBlockPoint.equals(startBlock.getCentralPoint())) {
                trackBlockPoint = newTrackBlockPoint;
                trackBlocks.add(Block.generateBlock(trackBlockPoint));
            }
        }
    }

    void generateFinishBlock() throws NoSuchAlgorithmException {
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

        return !trackBlocksCentralPoints.contains(point) && !point.equals(startBlock.getCentralPoint());
    }

    public Block getStartBlock() {
        return startBlock;
    }

    public List<Block> getTrackBlocks() {
        return trackBlocks;
    }

    public Block getFinishBlock() {
        return finishBlock;
    }
}

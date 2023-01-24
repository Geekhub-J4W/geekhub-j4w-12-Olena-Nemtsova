package edu.geekhub.homework.track;

import edu.geekhub.homework.model.Transport;

import java.util.HashMap;
import java.util.Map;

public class FieldController {
    private final Field field;
    private final Map<Point, Transport> occupiedPoints = new HashMap<>();

    public FieldController(Field field) {
        this.field = field;
    }

    public synchronized boolean isPointOccupied(Point point) {
        return occupiedPoints.containsKey(point);
    }

    public boolean isFieldPoint(Point point) {
        return field.startBlock().getPoints().contains(point) ||
            field.trackBlocks().stream().anyMatch((Block b) -> b.getPoints().contains(point)) ||
            field.finishBlock().getPoints().contains(point);
    }

    public boolean isFinishPoint(Point point) {
        return field.finishBlock().getPoints().contains(point);
    }

    public synchronized void occupyPoint(Point pointToOccupy, Point pointToRelease, Transport transport) {
        occupiedPoints.put(pointToOccupy, transport);
        releasePoint(pointToRelease);
    }

    public void releasePoint(Point point) {
        occupiedPoints.remove(point);
    }

    public synchronized Transport getTransportAtPoint(Point point) {
        return occupiedPoints.get(point);
    }

    public synchronized Point occupyRandomFreeStartPoint(Transport transport) {
        Point freeStartPoint = field.startBlock().getPoints().stream()
            .filter((Point p) -> occupiedPoints.keySet().stream().noneMatch(p::equals))
            .findAny().orElse(null);

        if (freeStartPoint != null) {
            occupyPoint(freeStartPoint, null, transport);
        }
        return freeStartPoint;
    }
}

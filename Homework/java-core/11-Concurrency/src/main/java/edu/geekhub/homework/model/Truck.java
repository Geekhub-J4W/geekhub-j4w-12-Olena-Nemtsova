package edu.geekhub.homework.model;

import edu.geekhub.homework.track.Field;
import edu.geekhub.homework.track.Point;
import edu.geekhub.homework.util.Course;
import edu.geekhub.homework.util.WinnerChecker;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public class Truck extends Transport {

    public Truck(Field field) throws NoSuchAlgorithmException {
        super(field);
        type = this.getClass().getSimpleName();
    }

    @Override
    public synchronized void run() {
        String message;
        boolean isWinner = false;

        while (!isWinner) {
            try {
                Thread.sleep(speed);
                Course course = Course.getRandomCourse();
                Point pointToMove = point.generateNewPointByCourse(course, 1);

                if (WinnerChecker.getValue()) {
                    break;
                }
                if (!field.isFieldPoint(pointToMove)) {
                    message = String.join(" ", color.name(), type, "left the road");
                    logger.log(Level.INFO, message);
                    field.releasePoint(point);
                    break;
                }

                while (field.isPointOccupied(pointToMove)) {
                    Transport transportAtPoint = field.getTransportAtPoint(pointToMove);
                    message = String.join(" ", color.name(), type, "waiting for the way be clear");
                    logger.log(Level.INFO, message);
                    Thread.sleep(500);

                    if (field.isPointOccupied(pointToMove) && transportAtPoint.type.equals(this.getClass().getSimpleName())) {
                        message = String.join(" ", color.name(), type, "and", transportAtPoint.color.name(), transportAtPoint.type, "were evacuated from the road");
                        logger.log(Level.INFO, message);
                        throw new InterruptedException();
                    }
                }

                field.occupyPoint(pointToMove, point, this);
                point = pointToMove;
                if (field.isFinishPoint(point)) {
                    isWinner = true;
                }
            } catch (NoSuchAlgorithmException | InterruptedException ex) {
                break;
            }
        }
        if (isWinner) {
            WinnerChecker.setWinner();
            message = String.join(" ", color.name(), type, "won the race!");
            logger.log(Level.INFO, message);
        }
    }
}

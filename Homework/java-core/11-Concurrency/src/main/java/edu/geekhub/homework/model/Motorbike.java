package edu.geekhub.homework.model;

import edu.geekhub.homework.track.FieldController;
import edu.geekhub.homework.track.Point;
import edu.geekhub.homework.util.Course;
import edu.geekhub.homework.util.WinnerChecker;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public class Motorbike extends Transport {

    public Motorbike(FieldController fieldController) throws NoSuchAlgorithmException {
        super(fieldController);
        super.type = this.getClass().getSimpleName();
    }

    @Override
    public void run() {
        String message;
        boolean isWinner = false;

        while (!isWinner) {
            try {
                Thread.sleep(speed);
                Course course = Course.getRandomCourse();
                Point pointToMove = point.generateNewPointByCourse(course, 2);

                if (WinnerChecker.getValue()) {
                    break;
                }

                if (!fieldController.isFieldPoint(pointToMove)) {
                    message = String.join(" ", color.name(), type, "left the road");
                    logger.log(Level.INFO, message);
                    fieldController.releasePoint(point);
                    break;
                }
                if (fieldController.isPointOccupied(pointToMove)) {
                    Transport transportAtPoint = fieldController.getTransportAtPoint(pointToMove);
                    message = String.join(" ", color.name(), type, "crashed into", transportAtPoint.color.name(), transportAtPoint.type);
                    logger.log(Level.INFO, message);
                }

                fieldController.occupyPoint(pointToMove, point, this);
                point = pointToMove;
                if (fieldController.isFinishPoint(point)) {
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

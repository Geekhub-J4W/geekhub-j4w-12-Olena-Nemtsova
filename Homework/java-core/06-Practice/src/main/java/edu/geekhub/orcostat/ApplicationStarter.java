package edu.geekhub.orcostat;

import edu.geekhub.orcostat.model.Drone;
import edu.geekhub.orcostat.model.Missile;
import edu.geekhub.orcostat.model.Orc;
import edu.geekhub.orcostat.model.Tank;

public class ApplicationStarter {

    public static void main(String[] args) {
        OrcoStatService orcoStatService = new OrcoStatService();
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addDestroyedTank(new Tank());
        orcoStatService.addDestroyedTank(new Tank());
        orcoStatService.addDestroyedDrone(new Drone());

        orcoStatService.setDate("2022-11-25");
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addDestroyedTank(new Tank());
        orcoStatService.addDestroyedDrone(new Drone());
        orcoStatService.addDestroyedMissile(new Missile());
        orcoStatService.addDestroyedMissile(new Missile());

        orcoStatService.setDate("2022-11-23");
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addDestroyedTank(new Tank());
        orcoStatService.addDestroyedDrone(new Drone());
        orcoStatService.addDestroyedMissile(new Missile());

        System.out.println(orcoStatService.dailyLosesAsPrintVersion());
        System.out.println(orcoStatService.totalLosesAsPrintVersion());
        System.out.println(orcoStatService.dailyLosesInDollarsAsPrintVersion());
        System.out.println(orcoStatService.totalLosesInDollarsAsPrintVersion());
    }
}

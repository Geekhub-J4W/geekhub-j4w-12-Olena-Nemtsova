package edu.geekhub.homework.task2;



public class LosesStatistic {

    private final int tanks;
    private final int armouredFightingVehicles;
    private final int cannons;
    private final int multipleRocketLaunchers;
    private final int antiAirDefenseDevices;
    private final int planes;
    private final int helicopters;
    private final int drones;
    private final int cruiseMissiles;
    private final int shipsOrBoats;
    private final int carsAndTankers;
    private final int specialEquipment;
    private final int personnel;

    private LosesStatistic(
        int tanks,
        int armouredFightingVehicles,
        int cannons,
        int multipleRocketLaunchers,
        int antiAirDefenseDevices,
        int planes,
        int helicopters,
        int drones,
        int cruiseMissiles,
        int shipsOrBoats,
        int carsAndTankers,
        int specialEquipment,
        int personnel
    ) {
        this.tanks = tanks;
        this.armouredFightingVehicles = armouredFightingVehicles;
        this.cannons = cannons;
        this.multipleRocketLaunchers = multipleRocketLaunchers;
        this.antiAirDefenseDevices = antiAirDefenseDevices;
        this.planes = planes;
        this.helicopters = helicopters;
        this.drones = drones;
        this.cruiseMissiles = cruiseMissiles;
        this.shipsOrBoats = shipsOrBoats;
        this.carsAndTankers = carsAndTankers;
        this.specialEquipment = specialEquipment;
        this.personnel = personnel;
    }

    /**
     * User-friendly print version of data
     *
     * @return a user-friendly representation of statistic
     */
    public String asPrintVersion() {
        return "Втрати російської армії в Україні: " +
            "Танки - " + tanks +
            ", Бойові броньовані машини (різних типів) - " + armouredFightingVehicles +
            ", Гармати - " + cannons +
            ", Реактивні системи залпового вогню - " + multipleRocketLaunchers +
            ", Засоби ППО - " + antiAirDefenseDevices +
            ", Літаки - " + planes +
            ", Гелікоптери - " + helicopters +
            ", Безпілотні літальні апарати (оперативно-тактичного рівня) - " + drones +
            ", Крилаті ракети - " + cruiseMissiles +
            ", Кораблі (катери) - " + shipsOrBoats +
            ", Автомобілі та автоцистерни - " + carsAndTankers +
            ", Спеціальна техніка - " + specialEquipment +
            ", Особовий склад - близько " + personnel + " осіб"
            ;
    }

    /**
     * This method should return an "empty object" instance with 0 as default value for fields
     *
     * @return an "empty object" instance
     */
    public static LosesStatistic empty() {
        return null;
    }

    public static LosesStatisticBuilder newStatistic() {
        return new LosesStatisticBuilder();
    }

    public static final class LosesStatisticBuilder {
        private int tanks;
        private int armouredFightingVehicles;
        private int cannons;
        private int multipleRocketLaunchers;
        private int antiAirDefenseDevices;
        private int planes;
        private int helicopters;
        private int drones;
        private int cruiseMissiles;
        private int shipsOrBoats;
        private int carsAndTankers;
        private int specialEquipment;
        private int personnel;

        private LosesStatisticBuilder() {
        }

        public LosesStatisticBuilder withTanks(int tanks) {
            this.tanks = tanks;
            return this;
        }

        public LosesStatisticBuilder withArmouredFightingVehicles(int armouredFightingVehicles) {
            this.armouredFightingVehicles = armouredFightingVehicles;
            return this;
        }

        public LosesStatisticBuilder withCannons(int cannons) {
            this.cannons = cannons;
            return this;
        }

        public LosesStatisticBuilder withMultipleRocketLaunchers(int multipleRocketLaunchers) {
            this.multipleRocketLaunchers = multipleRocketLaunchers;
            return this;
        }

        public LosesStatisticBuilder withAntiAirDefenseDevices(int antiAirDefenseDevices) {
            this.antiAirDefenseDevices = antiAirDefenseDevices;
            return this;
        }

        public LosesStatisticBuilder withPlanes(int planes) {
            this.planes = planes;
            return this;
        }

        public LosesStatisticBuilder withHelicopters(int helicopters) {
            this.helicopters = helicopters;
            return this;
        }

        public LosesStatisticBuilder withDrones(int drones) {
            this.drones = drones;
            return this;
        }

        public LosesStatisticBuilder withCruiseMissiles(int cruiseMissiles) {
            this.cruiseMissiles = cruiseMissiles;
            return this;
        }

        public LosesStatisticBuilder withShipsOrBoats(int shipsOrBoats) {
            this.shipsOrBoats = shipsOrBoats;
            return this;
        }

        public LosesStatisticBuilder withCarsAndTankers(int carsAndTankers) {
            this.carsAndTankers = carsAndTankers;
            return this;
        }

        public LosesStatisticBuilder withSpecialEquipment(int specialEquipment) {
            this.specialEquipment = specialEquipment;
            return this;
        }

        public LosesStatisticBuilder withPersonnel(int personnel) {
            this.personnel = personnel;
            return this;
        }

        public LosesStatistic build() {
            return new LosesStatistic(tanks, armouredFightingVehicles, cannons,
                multipleRocketLaunchers, antiAirDefenseDevices, planes, helicopters,
                drones, cruiseMissiles, shipsOrBoats, carsAndTankers, specialEquipment, personnel);
        }

    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (object == null || object.getClass() != getClass()) {
            return false;
        }

        LosesStatistic losesStatistic = (LosesStatistic) object;
        return losesStatistic.tanks == tanks &&
            losesStatistic.armouredFightingVehicles == armouredFightingVehicles &&
            losesStatistic.cannons == cannons &&
            losesStatistic.multipleRocketLaunchers == multipleRocketLaunchers &&
            losesStatistic.antiAirDefenseDevices == antiAirDefenseDevices &&
            losesStatistic.planes == planes &&
            losesStatistic.helicopters == helicopters &&
            losesStatistic.drones == drones &&
            losesStatistic.cruiseMissiles == cruiseMissiles &&
            losesStatistic.shipsOrBoats == shipsOrBoats &&
            losesStatistic.carsAndTankers == carsAndTankers &&
            losesStatistic.specialEquipment == specialEquipment &&
            losesStatistic.personnel == personnel;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + tanks;
        result = 31 * result + armouredFightingVehicles;
        result = 31 * result + cannons;
        result = 31 * result + multipleRocketLaunchers;
        result = 31 * result + antiAirDefenseDevices;
        result = 31 * result + planes;
        result = 31 * result + helicopters;
        result = 31 * result + drones;
        result = 31 * result + cruiseMissiles;
        result = 31 * result + shipsOrBoats;
        result = 31 * result + carsAndTankers;
        result = 31 * result + specialEquipment;
        result = 31 * result + personnel;

        return result;
    }

    @Override
    public String toString() {
        return "Втрати російської армії в Україні: " + System.lineSeparator() +
            "Танки - " + tanks + System.lineSeparator() +
            "Бойові броньовані машини (різних типів) - " + armouredFightingVehicles + System.lineSeparator() +
            "Гармати - " + cannons + System.lineSeparator() +
            "Реактивні системи залпового вогню - " + multipleRocketLaunchers + System.lineSeparator() +
            "Засоби ППО - " + antiAirDefenseDevices + System.lineSeparator() +
            "Літаки - " + planes + System.lineSeparator() +
            "Гелікоптери - " + helicopters + System.lineSeparator() +
            "Безпілотні літальні апарати (оперативно-тактичного рівня) - " + drones + System.lineSeparator() +
            "Крилаті ракети - " + cruiseMissiles + System.lineSeparator() +
            "Кораблі (катери) - " + shipsOrBoats + System.lineSeparator() +
            "Автомобілі та автоцистерни - " + carsAndTankers + System.lineSeparator() +
            "Спеціальна техніка - " + specialEquipment + System.lineSeparator() +
            "Особовий склад - близько " + personnel + " осіб"
            ;
    }
}

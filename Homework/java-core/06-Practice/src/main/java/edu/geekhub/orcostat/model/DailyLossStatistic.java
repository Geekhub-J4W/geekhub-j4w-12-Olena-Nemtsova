package edu.geekhub.orcostat.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DailyLossStatistic {
    private final String date;
    private final TrivialCollection negativeAliveOrcs;
    private final TrivialCollection destroyedTanks;
    private final TrivialCollection destroyedDrones;
    private final TrivialCollection destroyedMissiles;

    public DailyLossStatistic(String date) {
        validateDate(date);

        this.date = date;
        this.negativeAliveOrcs = new TrivialCollection();
        this.destroyedTanks = new TrivialCollection();
        this.destroyedDrones = new TrivialCollection();
        this.destroyedMissiles = new TrivialCollection();
    }

    private void validateDate(String date) {
        String format="yyyy-MM-dd";
        try {
            validateDateFormat(date, format);
            validateDateIsNotFuture(date, format);
            validateDateIsNotLessThanWarStart(date, format);
        }catch (Exception e) {
            throw new IllegalArgumentException("date has wrong format!");
        }
    }

    private void validateDateFormat(String date, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        formatter.parse(date);
    }
    private void validateDateIsNotFuture(String date, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate tomorrow = LocalDate.now();
        tomorrow = tomorrow.plusDays(1);

        boolean boo = formatter.parse(date).before(formatter.parse(dateFormatter.format(tomorrow)));
        if (!boo) {
            throw new IllegalArgumentException("date cannot be more than today!");
        }
    }

    private void validateDateIsNotLessThanWarStart(String date, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        boolean boo = formatter.parse("2014-04-05").before(formatter.parse(date));
        if (!boo) {
            throw new IllegalArgumentException("date cannot be less than 2014-04-06!");
        }
    }

    public String getDate() {
        return date;
    }

    public int getNegativelyAliveOrcCount() {
        return negativeAliveOrcs.count();
    }

    public void addNegativelyAliveOrc(Orc orc) {
        negativeAliveOrcs.add(orc);
    }

    public TrivialCollection getNegativelyAliveOrcs() {
        return negativeAliveOrcs;
    }

    public int getDestroyedTanksCount() {
        return destroyedTanks.count();
    }

    public void addDestroyedTank(Tank tank) {
        destroyedTanks.add(tank);

        for (Object orc : tank.getEquipage().getData()) {
            negativeAliveOrcs.add(orc);
        }
    }

    public TrivialCollection getDestroyedTanks() {
        return destroyedTanks;
    }

    public void addDestroyedDrone(Drone drone) {
        destroyedDrones.add(drone);
    }

    public int getDestroyedDronesCount() {
        return destroyedDrones.count();
    }

    public TrivialCollection getDestroyedDrones() {
        return destroyedDrones;
    }

    public void addDestroyedMissile(Missile missile) {
        destroyedMissiles.add(missile);
    }

    public int getDestroyedMissilesCount() {
        return destroyedMissiles.count();
    }

    public TrivialCollection getDestroyedMissiles() {
        return destroyedMissiles;
    }

    public int getLosesInDollars() {
        return ConvertorLosesToDollars.convert(this);
    }


}

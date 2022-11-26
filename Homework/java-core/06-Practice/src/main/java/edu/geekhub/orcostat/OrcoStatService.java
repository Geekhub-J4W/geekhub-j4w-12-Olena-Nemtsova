package edu.geekhub.orcostat;

import edu.geekhub.orcostat.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OrcoStatService {
    private final TrivialCollection dailyLosses;
    private String date;
    private int indexOfUsingDailyLossStatistic;

    public OrcoStatService() {
        this.dailyLosses = new TrivialCollection();
        setDate();
    }

    public TrivialCollection getDailyLosses() {
        return dailyLosses;
    }

    public void setDate(String date) {
        this.date = date;

        indexOfUsingDailyLossStatistic = isDailyStatisticsContainsDate(date);
        if (indexOfUsingDailyLossStatistic == -1) {
            dailyLosses.add(new DailyLossStatistic(date));
            indexOfUsingDailyLossStatistic = dailyLosses.count() - 1;
        }
    }

    private void setDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        this.setDate(formatter.format(now));
    }

    public String getDate() {
        return date;
    }

    private int isDailyStatisticsContainsDate(String date) {
        int i = 0;
        for (Object dailyLossStatistic : dailyLosses.getData()) {
            if (((DailyLossStatistic) dailyLossStatistic).getDate().equals(date)) return i;
            i++;
        }
        return -1;
    }

    public int getNegativelyAliveOrcCount() {
        int totalNegativelyAliveOrcCount = 0;
        for (Object dailyLossStatistic : dailyLosses.getData()) {
            totalNegativelyAliveOrcCount += ((DailyLossStatistic) dailyLossStatistic).getNegativelyAliveOrcCount();
        }
        return totalNegativelyAliveOrcCount;
    }

    public int getNegativelyAliveOrcCountByConcreteDate(int index) {
        return ((DailyLossStatistic) dailyLosses.getDataByIndex(index)).getNegativelyAliveOrcCount();
    }

    public void addNegativelyAliveOrc(Orc orc) {
        Object newDailyLossStatistic = dailyLosses.getDataByIndex(indexOfUsingDailyLossStatistic);
        ((DailyLossStatistic) newDailyLossStatistic).addNegativelyAliveOrc(orc);

        dailyLosses.changeValue(indexOfUsingDailyLossStatistic, newDailyLossStatistic);
    }

    public int getDestroyedTanksCount() {
        int totalDestroyedTanksCount = 0;
        for (Object dailyLossStatistic : dailyLosses.getData()) {
            totalDestroyedTanksCount += ((DailyLossStatistic) dailyLossStatistic).getDestroyedTanksCount();
        }
        return totalDestroyedTanksCount;
    }

    public int getDestroyedTanksCountByConcreteDate(int index) {
        return ((DailyLossStatistic) dailyLosses.getDataByIndex(index)).getDestroyedTanksCount();
    }

    public void addDestroyedTank(Tank tank) {
        Object newDailyLossStatistic = dailyLosses.getDataByIndex(indexOfUsingDailyLossStatistic);
        ((DailyLossStatistic) newDailyLossStatistic).addDestroyedTank(tank);

        dailyLosses.changeValue(indexOfUsingDailyLossStatistic, newDailyLossStatistic);
    }

    public int getDestroyedDronesCount() {
        int totalDestroyedDronesCount = 0;
        for (Object dailyLossStatistic : dailyLosses.getData()) {
            totalDestroyedDronesCount += ((DailyLossStatistic) dailyLossStatistic).getDestroyedDronesCount();
        }
        return totalDestroyedDronesCount;
    }

    public int getDestroyedDronesCountByConcreteDate(int index) {
        return ((DailyLossStatistic) dailyLosses.getDataByIndex(index)).getDestroyedDronesCount();
    }

    public void addDestroyedDrone(Drone drone) {
        Object newDailyLossStatistic = dailyLosses.getDataByIndex(indexOfUsingDailyLossStatistic);
        ((DailyLossStatistic) newDailyLossStatistic).addDestroyedDrone(drone);

        dailyLosses.changeValue(indexOfUsingDailyLossStatistic, newDailyLossStatistic);
    }

    public int getDestroyedMissilesCount() {
        int totalDestroyedMissilesCount = 0;
        for (Object dailyLossStatistic : dailyLosses.getData()) {
            totalDestroyedMissilesCount += ((DailyLossStatistic) dailyLossStatistic).getDestroyedMissilesCount();
        }
        return totalDestroyedMissilesCount;
    }

    public int getDestroyedMissilesCountByConcreteDate(int index) {
        return ((DailyLossStatistic) dailyLosses.getDataByIndex(index)).getDestroyedMissilesCount();
    }

    public void addDestroyedMissile(Missile missile) {
        Object newDailyLossStatistic = dailyLosses.getDataByIndex(indexOfUsingDailyLossStatistic);
        ((DailyLossStatistic) newDailyLossStatistic).addDestroyedMissile(missile);

        dailyLosses.changeValue(indexOfUsingDailyLossStatistic, newDailyLossStatistic);
    }

    public int getLosesInDollars() {
        int totalLosesInDollars = 0;
        for (Object dailyLossStatistic : dailyLosses.getData()) {
            totalLosesInDollars += ((DailyLossStatistic) dailyLossStatistic).getLosesInDollars();
        }
        return totalLosesInDollars;
    }

    public int getLosesInDollarsByConcreteDate(int index) {
        return ((DailyLossStatistic) dailyLosses.getDataByIndex(index)).getLosesInDollars();
    }

    public int getMaxNegativelyAliveOrcCount() {
        int max = 0;
        for (int i = 0; i < dailyLosses.count(); i++) {
            if (getNegativelyAliveOrcCountByConcreteDate(i) > max) {
                max = getNegativelyAliveOrcCountByConcreteDate(i);
            }
        }
        return max;
    }

    private int getMaxDestroyedTanksCount() {
        int max = 0;
        for (int i = 0; i < dailyLosses.count(); i++) {
            if (getDestroyedTanksCountByConcreteDate(i) > max) {
                max = getDestroyedTanksCountByConcreteDate(i);
            }
        }
        return max;
    }

    private int getMaxDestroyedDronesCount() {
        int max = 0;
        for (int i = 0; i < dailyLosses.count(); i++) {
            if (getDestroyedDronesCountByConcreteDate(i) > max) {
                max = getDestroyedDronesCountByConcreteDate(i);
            }
        }
        return max;
    }

    private int getMaxDestroyedMissilesCount() {
        int max = 0;
        for (int i = 0; i < dailyLosses.count(); i++) {
            if (getDestroyedMissilesCountByConcreteDate(i) > max) {
                max = getDestroyedMissilesCountByConcreteDate(i);
            }
        }
        return max;
    }

    public int getMaxLosesCount() {
        int max = 0;
        max = Math.max(max, getNegativelyAliveOrcCount());
        max = Math.max(max, getDestroyedTanksCount());
        max = Math.max(max, getDestroyedDronesCount());
        max = Math.max(max, getDestroyedMissilesCount());
        return max;
    }

    public int getMaxCountFor(int index) {
        return switch (index) {
            case 0 -> getMaxNegativelyAliveOrcCount();
            case 1 -> getMaxDestroyedTanksCount();
            case 2 -> getMaxDestroyedDronesCount();
            case 3 -> getMaxDestroyedMissilesCount();
            default -> -1;
        };
    }

    public int getCountFor(Object dailyLossStatistic, int index) {
        return switch (index) {
            case 0 -> ((DailyLossStatistic) dailyLossStatistic).getNegativelyAliveOrcCount();
            case 1 -> ((DailyLossStatistic) dailyLossStatistic).getDestroyedTanksCount();
            case 2 -> ((DailyLossStatistic) dailyLossStatistic).getDestroyedDronesCount();
            case 3 -> ((DailyLossStatistic) dailyLossStatistic).getDestroyedMissilesCount();
            default -> -1;
        };
    }

    public int getTotalCountFor(int index) {
        return switch (index) {
            case 0 -> getNegativelyAliveOrcCount();
            case 1 -> getDestroyedTanksCount();
            case 2 -> getDestroyedDronesCount();
            case 3 -> getDestroyedMissilesCount();
            default -> -1;
        };
    }

    public int getMaxLosesCountInDollars() {
        int max = 0;
        for (int i = 0; i < dailyLosses.count(); i++) {
            max = Math.max(max, getLosesInDollarsByConcreteDate(i));
        }

        return max;
    }

    public String dailyLosesAsPrintVersion() {
        ConvertorLosesToPrintVersion convertor = new ConvertorLosesToPrintVersion(this);
        return "Daily loses count:" + convertor.convertDailyLosesToPrintVersion();
    }

    public String totalLosesAsPrintVersion() {
        ConvertorLosesToPrintVersion convertor = new ConvertorLosesToPrintVersion(this);
        return "Total loses count:" + convertor.convertTotalLosesToPrintVersion();
    }

    public String dailyLosesInDollarsAsPrintVersion() {
        ConvertorLosesToPrintVersion convertor = new ConvertorLosesToPrintVersion(this);
        return "Daily loses in dollars:" + convertor.convertDailyDollarsLosesToPrintVersion();
    }

    public String totalLosesInDollarsAsPrintVersion() {
        ConvertorLosesToPrintVersion convertor = new ConvertorLosesToPrintVersion(this);
        return "Total loses in dollars:" + convertor.convertTotalDollarsLosesToPrintVersion();
    }
}

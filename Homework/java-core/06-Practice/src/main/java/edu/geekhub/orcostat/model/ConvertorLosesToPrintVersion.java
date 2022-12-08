package edu.geekhub.orcostat.model;

import edu.geekhub.orcostat.OrcoStatService;

import java.util.Objects;

public class ConvertorLosesToPrintVersion {
    private final OrcoStatService orcoStatService;
    private final TrivialCollection dailyLosses;
    private int widthOfFirstColumn;
    private int widthOfSecondColumn;

    public ConvertorLosesToPrintVersion(OrcoStatService orcoStatService) {
        if (Objects.isNull(orcoStatService)) {
            throw new IllegalArgumentException("OrcoStatService cannot be null");
        }

        this.orcoStatService = orcoStatService;
        dailyLosses = orcoStatService.getDailyLosses();
    }

    public String convertDailyLosesToPrintVersion() {
        String[] titleNames = {"troops", "tanks", "drones", "missiles"};
        StringBuilder result = new StringBuilder();
        widthOfFirstColumn = 12;

        for (int i = 0; i < 4; i++) {
            int max = orcoStatService.getMaxCountFor(i);
            widthOfSecondColumn = (" " + titleNames[i] + " count ").length();
            result.append(getTitle("period", titleNames[i]));

            for (Object dailyLossStatistic : dailyLosses.getData()) {
                result.append("| ")
                    .append(((DailyLossStatistic) dailyLossStatistic).getDate())
                    .append(" | ");

                int count = orcoStatService.getCountFor(dailyLossStatistic, i);
                result.append(count)
                    .append(" ".repeat((titleNames[i] + " count").length())).append("|")
                    .append(getChartLine(count, max))
                    .append(getBorder(widthOfFirstColumn, widthOfSecondColumn));
            }
            result.append(System.lineSeparator());
        }

        return result.toString();
    }

    public String convertTotalLosesToPrintVersion() {
        String[] rowsNames = {"troops", "tanks", "drones", "missiles"};
        StringBuilder result = new StringBuilder();
        int max = orcoStatService.getMaxLosesCount();
        widthOfFirstColumn = " type of loss ".length();
        widthOfSecondColumn = Math.max(" count ".length(), (" " + max + " ").length());

        result.append(getTitle("type of loss", ""));

        for (int i = 0; i < 4; i++) {
            result.append("| ")
                .append(rowsNames[i])
                .append(" ".repeat(widthOfFirstColumn - rowsNames[i].length() - 1));

            int count = orcoStatService.getTotalCountFor(i);
            result.append("| ")
                .append(count)
                .append(" ".repeat(widthOfSecondColumn - (count + "").length() - 1))
                .append("|")
                .append(getChartLine(count, max))
                .append(getBorder(widthOfFirstColumn, widthOfSecondColumn));
        }

        return result.toString();
    }

    public String convertDailyDollarsLosesToPrintVersion() {
        StringBuilder result = new StringBuilder();
        int max = orcoStatService.getMaxLosesCountInDollars();
        widthOfFirstColumn = 12;
        widthOfSecondColumn = Math.max((max + "$  ").length(), " money count ".length());

        result.append(getTitle("period", "money"));
        int i = 0;
        for (Object dailyLossStatistic : dailyLosses.getData()) {
            result.append("| ")
                .append(((DailyLossStatistic) dailyLossStatistic).getDate())
                .append(" | ");

            int count = orcoStatService.getLosesInDollarsByConcreteDate(i);
            result.append(count)
                .append("$")
                .append(" ".repeat(widthOfSecondColumn - (count + "").length() - 2))
                .append("|")
                .append(getChartLine(count, max))
                .append(getBorder(widthOfFirstColumn, widthOfSecondColumn));
            i++;
        }

        return result.toString();
    }

    public String convertTotalDollarsLosesToPrintVersion() {
        StringBuilder result = new StringBuilder();
        int count = orcoStatService.getLosesInDollars();
        widthOfFirstColumn = 8;
        widthOfSecondColumn = Math.max((count + "$  ").length(), " money count ".length());

        result.append(getTitle("period", "money"))
            .append("| total  | ")
            .append(count)
            .append("$")
            .append(" ".repeat(widthOfSecondColumn - (count + "$ ").length()))
            .append("|")
            .append(getChartLine(count, count))
            .append(getBorder(widthOfFirstColumn, widthOfSecondColumn));

        return result.toString();
    }

    private String getChartLine(int count, int max) {
        StringBuilder result = new StringBuilder();
        if (count == 0) {
            result.append(" -")
                .append(" ".repeat(98));
        } else {
            int countOfRepeat = (int) ((double) count / max * 100);
            result.append("#".repeat(countOfRepeat))
                .append(" ".repeat(100 - (countOfRepeat)));
        }
        result.append("|");

        return result.toString();
    }

    private String getTitle(String firstColName, String secondColName) {
        StringBuilder result = new StringBuilder();

        if (secondColName.length() != 0) {
            secondColName = " " + secondColName;
        }
        String repeat = " ".repeat((widthOfFirstColumn - firstColName.length()) / 2);
        result.append(getBorder(widthOfFirstColumn, (secondColName + " count ").length()))
            .append("|")
            .append(repeat)
            .append(firstColName)
            .append(repeat)
            .append("|")
            .append(secondColName)
            .append(" count |");

        int countOfRepeat = (100 - 6 - secondColName.length()) / 2;
        int remainder = (100 - 6 - secondColName.length()) % 2;
        result.append(" ".repeat(countOfRepeat))
            .append(secondColName)
            .append(" chart")
            .append(" ".repeat(countOfRepeat + remainder))
            .append("|")
            .append(getBorder(widthOfFirstColumn, widthOfSecondColumn));

        return result.toString();
    }

    private String getBorder(int firstColumnLength, int secondColumnLength) {
        return System.lineSeparator() +
            '+' +
            "-".repeat(firstColumnLength) +
            '+' +
            "-".repeat(secondColumnLength) +
            '+' +
            "-".repeat(100) +
            '+' +
            System.lineSeparator();
    }

}

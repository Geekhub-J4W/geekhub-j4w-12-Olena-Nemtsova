package edu.geekhub.homework;

import edu.geekhub.homework.downloading.SongDownloader;
import edu.geekhub.homework.logging.MyLogger;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ApplicationStarter {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SongDownloader downloader = new SongDownloader();
        downloader.downloadAll();

        MyLogger.saveToFile();

        showMenu();
    }

    static void showMenu() {
        int choice = getChoice("Show logs:\n1.All\n2.ERROR\n3.INFO");
        switch (choice) {
            case 1 -> {
                choice = getChoice("Order by:\n1.Date\n2.Log level");
                if (choice == 1) {
                    MyLogger.sortByDate();
                } else {
                    MyLogger.sortByLevel();
                }
                MyLogger.getAll().forEach(ApplicationStarter::print);
            }
            case 2 -> MyLogger.getOnlyERROR().forEach(ApplicationStarter::print);
            default -> MyLogger.getOnlyINFO().forEach(ApplicationStarter::print);
        }
    }

    static int getChoice(String message) {
        int choicesCount = (int) message.lines().count() - 1;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            print(message);
            try {
                if (!scanner.hasNextInt()) {
                    throw new IllegalArgumentException("Error! Incorrectly entered choice!");
                }

                int choice = scanner.nextInt();
                if (choice > choicesCount || choice < 0) {
                    throw new IllegalArgumentException("Error! Incorrectly entered choice!");
                }
                return choice;
            } catch (IllegalArgumentException ex) {
                print(ex.getMessage());
            }
        }
    }

    static void print(Object objectToPrint) {
        System.out.println(objectToPrint);
    }
}
